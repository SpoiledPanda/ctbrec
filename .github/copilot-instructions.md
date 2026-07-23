
# Copilot Instructions for ctbrec

These instructions help AI coding agents work productively in this documentation repository.

## Big Picture
- This is a **documentation and build template repository** for a Java/JavaFX desktop application (ctbrec version 25.11.2).
- The repository contains build configuration templates, contribution guidelines, and setup documentation.
- **No source code, compiled classes, or application assets** are present in this repository.
- The `.gitignore` excludes `.snapshots/` and `*.class` files, indicating they are not part of version control.

## Repository Structure
- `README.md` – Main documentation describing the project and how to use build templates
- `CONTRIBUTING.md` – Contribution guidelines and CSS workflow guidance
- `docs/BUILD_TEMPLATES.md` – Maven and Gradle build configuration templates for JDK 21 + JavaFX 21.0.4
- `docs/test_BUILD_TEMPLATES.md` – Test configuration templates
- `build.gradle.kts` – Empty placeholder for Gradle builds
- `.github/copilot-instructions.md` – This file

## Safe Edit Scope
- **Allowed:** All documentation files (README.md, CONTRIBUTING.md, this file)
- **Allowed:** Build templates in `docs/` directory
- **Allowed:** Build configuration files (build.gradle.kts, future pom.xml if added)
- **Not applicable:** No compiled artifacts, CSS files, or source code present to modify

## Documentation Guidelines
- Keep build templates accurate for JDK 21 and JavaFX 21.0.4
- Maintain consistency between README.md, CONTRIBUTING.md, and build templates
- Ensure Windows cmd and PowerShell examples are both provided where applicable
- Keep Maven and Gradle examples in sync regarding versions and configuration

## Build Template Conventions
- Target JDK 21 (Java 21) with JavaFX 21.0.4
- Maven: Use `javafx-maven-plugin` version 0.0.8
- Gradle: Use `org.openjfx.javafxplugin` version 0.1.0
- Default JavaFX modules: javafx.controls, javafx.graphics, javafx.media
- Expected main class: `ui.Launcher` (confirm in actual source)

## Developer Workflows
- This repository provides templates and documentation only
- Actual application source code is in a separate repository
- Contributors should use these templates to set up builds when working with the source
- No build/test/run commands apply to this repository itself

## Do / Don't for Agents
- **Do:** Update documentation to improve clarity and accuracy
- **Do:** Enhance build templates with better examples or additional configurations
- **Do:** Keep documentation synchronized across files
- **Do:** Provide both Maven and Gradle examples when adding new configurations
- **Don't:** Add source code, compiled classes, or application assets
- **Don't:** Reference specific class files or UI components that don't exist in this repo
- **Don't:** Create actual build artifacts or attempt to run builds

## If Unclear
- This is a documentation-only repository
- For questions about the actual application source code, refer to the main ctbrec repository
- If you find inconsistencies in documentation, update all affected files to maintain coherence
