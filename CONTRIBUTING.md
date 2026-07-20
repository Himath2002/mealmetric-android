# Contributing to MealMetric

Thank you for improving MealMetric. Keep changes focused, reviewable, and aligned with the app’s local-first scope.

## Workflow

1. Create a short-lived branch from `main`, such as `feat/date-navigation` or `fix/photo-permission`.
2. Keep commits small and use an imperative Conventional Commit subject.
3. Never add credentials, generated build output, local SDK paths, or signing material.
4. Run the verification command before opening a pull request:

   ```bash
   ./gradlew clean assembleDebug lint
   ```

5. Explain the user-facing change, implementation choice, and verification evidence in the pull request.

## Code expectations

- Centralize user-visible text and color values in Android resources.
- Keep database work off the main thread.
- Preserve the no-credentials manual logging path.
- Add a Room migration and schema update for database changes after version 1.
- Prefer platform APIs for local file access and request only essential permissions.

## Commit examples

```text
feat: add journal date navigation
fix: preserve selected photo access
docs: clarify Nutritionix configuration
```
