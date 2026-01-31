# ctbrec (25.11.2) – Snapshot

This folder contains a compiled snapshot of a Java/JavaFX desktop application. It is useful for UI styling tweaks (JavaFX CSS) and documentation, but it does not include the Java source code or build configuration.

## What’s here

- `ui/` – Application UI classes (compil JavaFX CSS assets
  - `ui/controls/` (e.g., `Popover.css`)
  - `ui/settings/` (e.g., `ColorSettingsPane.css`)
  - `ui/tabs/` (e.g., `ThumbCell.css`)
  - `ui/sites/<site>/` – Per-site UI modules (compiled classes)
- `ui/io/json/{dto,mapper}` – Event DTOs and JSON mappers (compiled)
- `docs/` – Compiled doc server classes
- Top-level compiled classes: `RecordingDownload.class`, `Launcher.class`, etc.

## What you can safely change

- JavaFX CSS under `ui/controls/**`, `ui/settings/**`, `ui/tabs/**`.
- Documentation files you add (this README) or future static assets.
- Do not modify or remove `.class` files (compiled artifacts).

## What’s not included

- Source code (Java files), build tool (Maven/Gradle) configs, or test suites. Running or debugging the app from this folder alone is not supported.

## Contributing / Full source

- Please provide the source repository URL, build tool (Maven/Gradle), and JDK/JavaFX versions to enable full builds and code changes.
- Once available, we can add build/run steps here.

## For AI coding agents

- See `.github/copilot-instructions.md` for codebase-specific guidance and safe-edit rules.
- For CSS tweaks, keep existing selectors/tokens and JavaFX `-fx-*` properties.

## Verify JDK/JavaFX locally

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
javap -verbose ui\CamrecApplication.class | Select-String "major version"
```

- JavaFX version isn’t encoded in class files; confirm from your build files (Maven/Gradle) or dependency list.

## Build/Run (when source is available)

- Preconditions: JDK 21 and JavaFX 21.x (confirm from your source/build files).
- Discover JavaFX modules used (to configure dependencies):

```cmd
jdeps -q -s -multi-release 21 -cp . ui\CamrecApplication.class
```

- Maven example (outline):
  - Use OpenJFX 21 deps and configure your launcher class (likely `ui.Launcher`, confirm in source).
  - Run:

```cmd
mvn -q clean javafx:run
```

- Gradle example (outline):
  - Apply `org.openjfx.javafxplugin` and set `mainClass` to your launcher.
  - Run:

<!-- Gradle run: platform-specific examples -->
**Windows (Command Prompt):**

## JavaFX Configuration Example

For builds using JavaFX, specify the version and required modules in your build tool configuration. Example (Kotlin DSL):

```kotlin
javafx {
    version = "21.0.4"
    modules = listOf("javafx.controls", "javafx.graphics", "javafx.media")
}
```

Refer to your build tool documentation for exact syntax and integration steps.

## Contributing

See `CONTRIBUTING.md` for safe-edit scope, CSS workflow, and how to provide source/build details so we can add exact instructions.

## Build templates

When the source repository is available, see `docs/BUILD_TEMPLATES.md` for Maven/Gradle examples targeting JDK 21 and JavaFX 21.
