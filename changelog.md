# Changelog

## [Sprint 4] - 2025-06-15

- feat: Java Swing GUI providing month view, day detail dialog, and visual event creation/editing
- feat: CSV import for bulk event upload compatible with Google Calendar format
- feat: Unified controller now supports CLI, headless scripts, and GUI modes from single JAR (`Assignment6.jar`)
- feat: Complete CI/CD pipeline with GitHub Actions for automated testing, coverage, and quality gates
- feat: JaCoCo integration for line coverage reporting with 85% threshold enforcement
- feat: Enhanced PIT mutation testing with XML/HTML reports and coverage thresholds
- feat: Automated JAR artifact generation and test report archiving (30-90 day retention)
- docs: Added CI/CD badges, coverage integration, and comprehensive testing documentation
- docs: Updated README with GUI screenshots, usage guide, and new architecture diagrams
- chore: Added release tag `Ass6` and automated GitHub Actions build for JAR artifact

## [Sprint 3] - 2025-01-15

- feat: Multi-calendar support with unique names and IANA timezones
- feat: Calendar creation, selection, and editing commands
- feat: Event copy operations between calendars with automatic timezone conversion
- feat: Automatic timezone migration when editing calendar properties
- feat: Universal auto-decline conflict detection enabled by default for all events
- feat: JAR-based distribution

## [Sprint 2] - 2025-06-02

- MVC redesign - cleaner service layer, SOLID-compliant controllers.
- See full Sprint2 design notes in [`docs/Sprint2.md`](docs/Sprint2.md). 