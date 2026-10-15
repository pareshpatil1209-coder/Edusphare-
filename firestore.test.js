const {
  initializeTestEnvironment,
  assertFails,
  assertSucceeds,
} = require("@firebase/rules-unit-testing");
const { test, before, after, beforeEach } = require("node:test");
const fs = require("node:fs");

let testEnv;
const PROJECT_ID = process.env.GCP_PROJECT || "demo-no-project";
const ALICE_UID = "alice_123";
const BOB_UID = "bob_456";

const [emulatorHost, emulatorPortStr] = (process.env.FIRESTORE_EMULATOR_HOST || "127.0.0.1:8085").split(":");
const emulatorPort = parseInt(emulatorPortStr, 10);

before(async () => {
  const rules = fs.readFileSync("./firestore.rules", "utf8");
  testEnv = await initializeTestEnvironment({
    projectId: PROJECT_ID,
    firestore: {
      rules,
      host: emulatorHost,
      port: emulatorPort,
    },
  });
});

after(async () => {
  if (testEnv) {
    await testEnv.cleanup();
  }
});

beforeEach(async () => {
  if (testEnv) {
    await testEnv.clearFirestore();
  }
});

test("Unauthenticated user cannot read any student profile", async () => {
  const unauthDb = testEnv.unauthenticatedContext().firestore();
  await assertFails(unauthDb.collection("users").doc(ALICE_UID).get());
});

test("Unauthenticated user cannot write a student profile", async () => {
  const unauthDb = testEnv.unauthenticatedContext().firestore();
  await assertFails(unauthDb.collection("users").doc(ALICE_UID).set({
    uid: ALICE_UID,
    displayName: "Alice",
    email: "alice@example.com",
    onboardingCompleted: true,
    createdAt: new Date(),
    updatedAt: new Date()
  }));
});

test("Authenticated user can create their own profile", async () => {
  const aliceDb = testEnv.authenticatedContext(ALICE_UID).firestore();
  await assertSucceeds(aliceDb.collection("users").doc(ALICE_UID).set({
    uid: ALICE_UID,
    displayName: "Alice Student",
    email: "alice@example.com",
    onboardingCompleted: true,
    educationBoard: "CBSE",
    classLevel: "Class 10",
    createdAt: new Date(),
    updatedAt: new Date()
  }));
});

test("Authenticated user cannot create another user's profile", async () => {
  const aliceDb = testEnv.authenticatedContext(ALICE_UID).firestore();
  await assertFails(aliceDb.collection("users").doc(BOB_UID).set({
    uid: BOB_UID,
    displayName: "Bob Student",
    email: "bob@example.com",
    onboardingCompleted: true,
    createdAt: new Date(),
    updatedAt: new Date()
  }));
});

test("Authenticated user cannot read another user's profile or subcollections", async () => {
  // Seed Bob's profile as admin
  await testEnv.withSecurityRulesDisabled(async (context) => {
    await context.firestore().collection("users").doc(BOB_UID).set({
      uid: BOB_UID,
      displayName: "Bob Student",
      email: "bob@example.com",
      onboardingCompleted: true,
      createdAt: new Date(),
      updatedAt: new Date()
    });
    await context.firestore().collection("users").doc(BOB_UID).collection("bookmarks").doc("bm1").set({
      id: "bm1",
      userId: BOB_UID,
      title: "Physics Ch 1",
      type: "note",
      createdAt: new Date()
    });
  });

  const aliceDb = testEnv.authenticatedContext(ALICE_UID).firestore();
  await assertFails(aliceDb.collection("users").doc(BOB_UID).get());
  await assertFails(aliceDb.collection("users").doc(BOB_UID).collection("bookmarks").doc("bm1").get());
});

test("Authenticated user can create and read their own bookmarks", async () => {
  const aliceDb = testEnv.authenticatedContext(ALICE_UID).firestore();
  await assertSucceeds(aliceDb.collection("users").doc(ALICE_UID).collection("bookmarks").doc("bkm_1").set({
    id: "bkm_1",
    userId: ALICE_UID,
    title: "Math Formulas",
    type: "note",
    subject: "Mathematics",
    referenceId: "ref_101",
    createdAt: new Date()
  }));

  await assertSucceeds(aliceDb.collection("users").doc(ALICE_UID).collection("bookmarks").doc("bkm_1").get());
});
