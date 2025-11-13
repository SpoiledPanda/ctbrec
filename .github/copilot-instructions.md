# Copilot Instructions for this Repo

These guidelines help AI coding agents work productively in this codebase snapshot.

## Big Picture
- This is a Java/JavaFX desktop app (compiled classes present). UI styling uses JavaFX CSS (`-fx-*`).
- The UI layer is organized under `ui/`, with per-site modules under `ui/sites/<site>/` (e.g., `chaturbate`, `camsoda`). Class names indicate consistent roles: `*SiteUi`, `*ConfigUi`, `*TabProvider`, `*UpdateService`.
- Events and I/O: runtime events like `PlayerStartedEvent` (`ui/event/PlayerStartedEvent.class`) have JSON DTOs and mappers in `ui/io/json/{dto,mapper}` (`PlayerStartedEventDto.class`, `PlayerStartedEventMapper.class`, `PlayerStartedEventMapperImpl.class`).
- Core app classes include `CamrecApplication`, `Launcher`, `Player`, `JavaFxModel`, `JavaFxRecording`, and top-level `RecordingDownload.class`.

## What You Can Safely Change Here
- JavaFX CSS under `ui/controls/**`, `ui/settings/**`, `ui/tabs/**` (e.g., `Popover.css`, `ColorSettingsPane.css`, `ThumbCell.css`). Keep existing `-fx-` tokens and selector structure.
- Static assets or documentation if added in future under `docs/` (currently compiled classes like `DocServer.class` only).
- Do not attempt to modify `.class` files; they are compiled artifacts.

## Patterns and Conventions
- Per-site UI modules mirror a common shape:
  - `ui/sites/<site>/<Site>SiteUi.class` and `<Site>TabProvider.class` define views/tabs.
  - `<Site>ConfigUi.class` hosts site-specific settings panes.
  - `<Site>UpdateService.class` handles background updates/polling.
- Event → DTO → JSON mapping follows:
  - Domain event: `ui/event/PlayerStartedEvent.class`
  - DTO: `ui/io/json/dto/PlayerStartedEventDto.class`
  - Mapper: `ui/io/json/mapper/PlayerStartedEventMapper(.class|Impl.class)`
- UI styling: JavaFX CSS files use component-scoped classes (e.g., `.popover`, `.popover-frame`) and `-fx-background-*`, `-fx-effect`, `-fx-shape` for custom chrome.

## Developer Workflows (Current Snapshot Limits)
- Build/test configs (Maven/Gradle, unit tests) are not present in this snapshot; only compiled `.class` files are included.
- Running or debugging Java code from this folder is likely not feasible without the original source repo and build toolchain (JavaFX modules/jars, classpath).
- If asked to add or refactor Java, first request the source repository, JDK version, and build tool (Maven/Gradle) before proceeding.

## Detected Build Info
- Build tool files: none present (no `pom.xml`, `build.gradle*`, or wrapper scripts).
- JDK target: classfile major `65` (from `ui/CamrecApplication.class`) → Java 21.
- JavaFX: UI uses JavaFX; exact version not in snapshot. Likely aligns with JDK 21 (e.g., JavaFX 21.x); confirm in source repo.

## Verify Locally
- Check Java (cmd):

```cmd
java -version
javac -version
```

- Inspect classfile major version (expected 65 → Java 21):
- cmd:

```cmd
javap -verbose ui\CamrecApplication.class | find "major version"
```

- PowerShell:

```powershell
javap -verbose ui/CamrecApplication.class | Select-String "major version"
```

## Build/Run (When Source Available)
- Preconditions: JDK 21 and JavaFX 21.x; confirm in source repo.
- Discover modules with `jdeps` to pick JavaFX modules:

```cmd
jdeps -q -s -multi-release 21 -cp . ui\CamrecApplication.class
```

- Maven (example outline):
  - Use OpenJFX 21 dependencies and configure your launcher (likely `ui.Launcher`, confirm in source).
  - Run:

```cmd
mvn -q clean javafx:run -Dexec.args="" -Dprism.order=sw
```

- Gradle (example outline):
  - Apply `org.openjfx.javafxplugin` and set `mainClass` to your launcher.
  - Run:

```cmd
gradlew run --warning-mode all
```

More detailed templates (Maven/Gradle) are in `docs/BUILD_TEMPLATES.md`.

## Examples You Can Follow
- Styling tweak: update `ui/controls/Popover.css` selectors like `.popover-title` or `.popover .button` to adjust typography while preserving `-fx-` tokens.
- Site-specific UI: inspect `ui/sites/chaturbate/*` to mirror naming/placement for other site modules (e.g., `<Site>SiteUi`, `<Site>TabProvider`).
- Event export: keep `PlayerStartedEvent` data shape aligned with `PlayerStartedEventDto` and its mapper when documenting or integrating with external consumers.

## CSS Styling Guidance
- Prefer small, scoped tweaks. Keep selectors intact (e.g., `.popover`, `.popover-frame`, `.popover-title`).
- Preserve JavaFX tokens: `-fx-background-color`, `-fx-background-radius`, `-fx-effect`, `-fx-shape`, `-fx-text-fill`.
- Example references: `ui/controls/Popover.css` uses tooth variants `.left-tooth`/`.right-tooth` via `-fx-shape`; avoid renaming these selectors.
- Typography edits: adjust `-fx-font-size` in `.popover-title` or `.popover .button`; don’t change class names.
- Visual depth: tune `dropshadow(gaussian, rgba(...), radius, ...)` rather than replacing effects entirely.
- Test across themes if applicable; keep color usage tied to existing variables like `-fx-base`, `-fx-text-background-color`.

## Do / Don’t for Agents
- Do: Propose CSS/UI tweaks with precise file paths and selectors.
- Do: Reference exact class files when discussing behaviors (e.g., `ui/sites/chaturbate/ChaturbateUpdateService.class`).
- Don’t: Invent build commands or introduce Java sources without the build system.
- Don’t: Remove or rename compiled `.class` files.

## Open Questions (Please Confirm)
- What are the official build instructions (tooling, Java/JavaFX versions)?
- Where is the source repository for these compiled classes?
- Are there tests or CI workflows to reference?
- Any additional resource folders (icons, FXML) not included in this snapshot?
