# LEVEL UP: REAL LIFE — Android MVP 1.0

A native Android app that gamifies everyday habits as a fantasy RPG. The MVP uses Java and Android SDK views, with local persistence through `SharedPreferences`; no account, server, or network permission is required.

## Included in this first stage
- Dark fantasy-inspired character dashboard
- Level, XP, coins, and skill points
- Daily quests with one claim per quest per local calendar day
- Add custom quests
- Warrior, Scholar, and Guardian skill branches
- Offline persistence across app restarts
- GitHub Actions workflow to build and upload a debug APK

## Build locally
Requirements: Android Studio/Android SDK, JDK 17, and Gradle 8.9.

From the project root:

```bash
gradle assembleDebug
```

APK output: `app/build/outputs/apk/debug/app-debug.apk`.

## Build using GitHub Actions
1. Create a new GitHub repository and upload the contents of this folder.
2. Open **Actions** and run **Build Android APK** (or push to `main`/`master`).
3. Open the successful workflow run and download the `level-up-real-life-debug-apk` artifact.

## Notes
- This is an MVP source project, not a claim that an APK has already been compiled or tested on a device.
- App data is stored locally. Clearing app storage or uninstalling the app can erase progress; export/import backup is not implemented in this stage.
- Notifications, boss missions, reward shop, data backup, and richer skill effects are planned for later stages.
