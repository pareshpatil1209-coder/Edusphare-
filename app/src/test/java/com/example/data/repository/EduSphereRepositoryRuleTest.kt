package com.example.data.repository

import com.example.base.FirestoreEmulatorTestBase
import com.example.data.model.BookmarkItem
import com.example.data.model.UserProfile
import com.google.firebase.firestore.FirebaseFirestoreException
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withTimeout
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertTrue
import org.junit.Assert.fail
import org.junit.Test
import java.util.UUID

class EduSphereRepositoryRuleTest : FirestoreEmulatorTestBase() {

    @Test
    fun createOrUpdateUserProfile_validPayload_createsDocumentAndReadsBack() = runBlocking {
        val aliceUid = signInTestUser(ALICE_EMAIL)
        val repo = EduSphereRepository(firestore)

        val profile = UserProfile(
            uid = aliceUid,
            displayName = "Alice Wonder",
            email = ALICE_EMAIL,
            educationBoard = "CBSE",
            classLevel = "Class 10",
            stream = "General",
            onboardingCompleted = true
        )

        val createResult = withTimeout(DEFAULT_TIMEOUT_MS) {
            repo.createOrUpdateUserProfile(profile)
        }
        assertTrue("Profile creation should succeed", createResult.isSuccess)

        val readResult = withTimeout(DEFAULT_TIMEOUT_MS) {
            repo.getUserProfile(aliceUid)
        }
        assertTrue("Profile fetch should succeed", readResult.isSuccess)
        val loaded = readResult.getOrNull()
        assertNotNull(loaded)
        assertEquals("Alice Wonder", loaded?.displayName)
        assertEquals("CBSE", loaded?.educationBoard)
    }

    @Test
    fun addBookmark_and_observeBookmarks_success() = runBlocking {
        val aliceUid = signInTestUser(ALICE_EMAIL)
        val repo = EduSphereRepository(firestore)

        val bookmark = BookmarkItem(
            id = "bm_" + UUID.randomUUID().toString().take(8),
            userId = aliceUid,
            title = "Physics Laws of Motion",
            type = "note",
            subject = "Physics"
        )

        val addResult = withTimeout(DEFAULT_TIMEOUT_MS) {
            repo.addBookmark(bookmark)
        }
        if (addResult.isFailure) {
            println("ADD BOOKMARK FAILURE: ${addResult.exceptionOrNull()?.message}")
            addResult.exceptionOrNull()?.printStackTrace()
        }
        assertTrue("Adding bookmark should succeed: ${addResult.exceptionOrNull()?.message}", addResult.isSuccess)

        val bookmarks = withTimeout(FLOW_TIMEOUT_MS) {
            repo.observeBookmarks().first { list -> list.any { it.title == "Physics Laws of Motion" } }
        }
        assertTrue(bookmarks.isNotEmpty())
    }

    @Test
    fun crossUserAccess_failsWithPermissionDenied() = runBlocking {
        val aliceUid = signInTestUser(ALICE_EMAIL)
        val aliceRepo = EduSphereRepository(firestore)

        val profile = UserProfile(
            uid = aliceUid,
            displayName = "Alice Secret",
            email = ALICE_EMAIL,
            onboardingCompleted = true
        )
        withTimeout(DEFAULT_TIMEOUT_MS) {
            aliceRepo.createOrUpdateUserProfile(profile)
        }

        // Switch to Bob
        signInTestUser(BOB_EMAIL)
        try {
            withTimeout(DEFAULT_TIMEOUT_MS) {
                firestore.collection("users").document(aliceUid).get().await()
            }
            fail("Expected PERMISSION_DENIED on cross-user read")
        } catch (e: FirebaseFirestoreException) {
            assertEquals(FirebaseFirestoreException.Code.PERMISSION_DENIED, e.code)
        } catch (e: Exception) {
            val cause = e.cause as? FirebaseFirestoreException
            if (cause != null) {
                assertEquals(FirebaseFirestoreException.Code.PERMISSION_DENIED, cause.code)
            } else {
                assertTrue(e.message?.contains("PERMISSION_DENIED") == true)
            }
        }
    }

    @Test
    fun unauthenticatedUser_failsWithPermissionDenied() = runBlocking {
        auth.signOut()
        try {
            withTimeout(DEFAULT_TIMEOUT_MS) {
                firestore.collection("users").document("random_uid").get().await()
            }
            fail("Expected PERMISSION_DENIED on unauthenticated read")
        } catch (e: FirebaseFirestoreException) {
            assertEquals(FirebaseFirestoreException.Code.PERMISSION_DENIED, e.code)
        } catch (e: Exception) {
            val cause = e.cause as? FirebaseFirestoreException
            if (cause != null) {
                assertEquals(FirebaseFirestoreException.Code.PERMISSION_DENIED, cause.code)
            } else {
                assertTrue(e.message?.contains("PERMISSION_DENIED") == true)
            }
        }
    }

    private companion object {
        const val ALICE_EMAIL = "alice_student@test.com"
        const val BOB_EMAIL = "bob_student@test.com"
        const val DEFAULT_TIMEOUT_MS = 8000L
        const val FLOW_TIMEOUT_MS = 6000L
    }
}
