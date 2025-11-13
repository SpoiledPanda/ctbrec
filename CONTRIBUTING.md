# Contributing

This folder is a compiled snapshot of a Java/JavaFX desktop app. It is useful for UI styling tweaks (JavaFX CSS) and docs, but does not include the Java source or build configuration.

## Scope of contributions here

- Safe to change: JavaFX CSS under `ui/controls/**`, `ui/settings/**`, `ui/tabs/**`.
- Not safe to change: compiled `.class` files; do not modify or remove them.
- Documentation updates welcome: `README.md`, `.github/copilot-instructions.md`, this file.

## Prerequisites (when working with source)

- JDK: 21 (classfile major 65 detected).
- JavaFX: likely 21.x (confirm in source `pom.xml`/`build.gradle`).

Verify locally (Windows)

```cmd
java -version
javac -version
javap -verbose ui\CamrecApplication.class | find "major version"
```

PowerShell alternative

```powershell
javap -verbose ui/CamrecApplication.class | Select-String "major version"
```

## CSS tweak workflow

1. Identify target selectors in `ui/**.css` (e.g., `ui/controls/Popover.css`).
2. Make scoped adjustments: keep selectors and `-fx-*` tokens intact.
3. Prefer adjusting sizes/colors/effects over structural selector changes.
4. Test in-app (requires full source build) or via your team’s preview workflow.
5. Keep changes minimal and consistent with existing style.

## Build/Run (with source repo)

- Discover JavaFX modules used:

```cmd
jdeps -q -s -multi-release 21 -cp . ui\CamrecApplication.class
```

- Maven (outline): add OpenJFX 21 deps; set launcher (likely `ui.Launcher`).

```cmd
mvn -q clean javafx:run
```

- Gradle (outline): apply `org.openjfx.javafxplugin`; set `mainClass`.

```cmd
gradlew run
```

## Source-repo checklist

- Provide: repo URL, build tool (Maven/Gradle), JavaFX version, launcher class, run args.
- With that, we’ll add exact build/run/debug instructions here and in the README.
