# Repository Guidelines

## Project Structure & Module Organization
The Java playground lives under `app/`. Primary source sits in `app/src/main/java/org/example` with subpackages (e.g. `nanocli`, `rate_limit`, `zookeeper`) grouping feature demos and annotation processors like `CacheProcessor` and `BuilderProcessor`. Shared config files live in `app/src/main/resources` (Log4j2). Unit test fixtures live in `app/src/test/java/org/example`; keep helper factories alongside the tests they support. Microbenchmarks stay in `app/src/jmh/java`. Generated assets, Gradle outputs (`app/build`, `app/out`), and data dumps under `output/` should stay untracked, keeping practice runs reproducible. Use the root `docker-compose.yml` when experimenting with Redis or ZooKeeper integrations.

## Build, Test, and Development Commands
Run `./gradlew build` for a clean compile plus unit tests. Use `./gradlew test` when iterating on JUnit 5 suites; it streams stdout to aid debugging. Launch the sample CLI with `./gradlew run --args="..."` (default main class is `org.example.BuilderExample`). Execute microbenchmarks via `./gradlew jmh` and review the generated reports under `app/build/reports/jmh`. Start external services with `docker-compose up -d redis zookeeper` and shut them down with `docker-compose down`.

## Coding Style & Naming Conventions
Target Java 21 and 4-space indentation. Place each new feature in a dedicated subpackage under `org.example`; class names use UpperCamelCase, methods and fields use lowerCamelCase, and constants use UPPER_SNAKE_CASE. Favor immutability, keep annotation processors self-contained, and update `build.gradle.kts` if you introduce new processor entry points. Lombok is available but optional—mirror existing usage before adding annotations.

## Testing Guidelines
Tests rely on JUnit Jupiter with AssertJ available for fluent checks. Name test classes `*Test` or `*IT` and mirror package placement of the code under test. Keep external-service tests idempotent so they can run against disposable Redis/ZooKeeper containers. Collect new coverage with `./gradlew test`; add `@Disabled` along with a TODO if a test needs manual setup.

## Commit & Pull Request Guidelines
Keep a readable history using `type(scope): summary` (e.g. `feat(java/playground/nanocli): add option parser`). Types can include `feat`, `chore`, `refactor`, `wip`, or any label that helps you revisit work later. Write summaries in present tense and under 70 characters so `git log --oneline` stays scannable. Even if you are working solo, draft pull-request-style notes in the commit body: what changed, commands used for verification, and open questions. When branching for a larger practice session, jot down verification steps or TODOs in the PR description or a markdown scratchpad for future reference.

## Security & Configuration Tips
Load sensitive values through `.env` files consumed by the Dotenv library and keep those files out of version control. Rotate Redis and ZooKeeper credentials in development before demonstrating features, and avoid hard-coding connection strings—prefer configuration helpers already in the repo.
