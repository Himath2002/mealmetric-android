# Changelog

Notable changes to MealMetric are recorded here. The project follows
[Semantic Versioning](https://semver.org/spec/v2.0.0.html).

## [Unreleased]

## [1.1.1] - 2026-07-22

### Fixed

- Aligned the GitHub Actions runtime with Android Lint's Java 21 runtime
  requirement, preventing an internal `BidirectionalTextDetector` crash on CI.
- Synchronized the Android package version with the published patch release.

### Verified

- Clean debug APK assembly on JDK 21.
- Android lint with no reported issues on JDK 21.

## [1.1.0] - 2026-07-21

### Changed

- Organized the Android source into explicit UI, ViewModel, model, local-data,
  repository, and remote-data packages without changing application behavior.
- Replaced the product walkthrough with a precisely aligned three-step flow.
- Expanded the architecture visual to document Room, Nutritionix, and Android
  document-provider boundaries independently.
- Moved the exported Room schema alongside the database's new canonical package.

### Verified

- Clean debug APK assembly.
- Android lint with no reported issues.
- An unchanged Room schema identity across the package move.

## [1.0.0] - 2026-07-21

### Added

- Local-first daily meal logging backed by Room.
- Live calorie and meal-count summaries.
- Optional Nutritionix natural-language lookup with local-only credentials.
- Provider-managed photo selection with persisted read access.
- Light and dark Material 3 themes, CI, dependency review, and publication docs.

[Unreleased]: https://github.com/Himath2002/mealmetric-android/compare/v1.1.1...HEAD
[1.1.1]: https://github.com/Himath2002/mealmetric-android/compare/v1.1.0...v1.1.1
[1.1.0]: https://github.com/Himath2002/mealmetric-android/compare/v1.0.0...v1.1.0
[1.0.0]: https://github.com/Himath2002/mealmetric-android/releases/tag/v1.0.0
