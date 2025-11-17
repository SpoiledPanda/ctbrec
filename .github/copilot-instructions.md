# Copilot / AI Agent Instructions for CTB Recorder (packaged distribution)

Purpose

- Short: help AI agents understand this repository is a packaged distribution of CTB Recorder (Java app + embedded browser), not the full upstream source tree.

Big picture

- This folder layout is a runnable bundle: `ctbrec.bat` (Windows launcher) sits at repo root and starts the Java app.
- A complete JRE is embedded under `jre/` (runtime only). Treat `jre/` as a binary dependency — do not attempt to rebuild it here.
- The Electron/browser UI sources live under `lib/browser/resources/app/` and include a small Node/Electron app (see its `README.md`).
- `ffmpeg/` contains native ffmpeg binaries used for recording/processing — treat them as external binaries.

What agents should know first

- This is primarily a distribution; most work will be packaging, docs, small UI tweaks in `lib/browser/resources/app/`, and launcher scripts.
- There is no build system for Java sources here. For Java development or full source changes, refer to the upstream source (see "Upstream Java sources" below). Do not assume Java sources are present in this bundle.

Key files & dirs

- `ctbrec.bat` — Windows start script (modify to change JVM options or classpath used by the bundled binary).
- `jre/` — embedded Java runtime (binary). Replace only when intentionally updating the bundled JRE.
- `lib/browser/resources/app/README.md` — build instructions for the Electron/browser component (use `npm install` and `electron-packager`).
- `ffmpeg/` — native ffmpeg binaries used by the app.
- `LICENSE.txt` and top-level `README.md` — licensing and high-level project description.

Build / run / debug workflows (what works here)

- Run the packaged app on Windows: `.\\ctbrec.bat` (double-click also works). On Unix-like bundles use their `ctbrec.sh`.
- Build the Electron/browser UI (inside `lib/browser/resources/app`):
  - `npm install`
  - `npm install electron-packager --save-dev`
  - `node node_modules/electron-packager/bin/electron-packager.js --overwrite . ctbrec-minimal-browser`
  - Cross-platform packaging: add `--platform` and `--arch` flags (see that folder's README).
- Packaging tasks for releases: update docs, swap bundled `jre/`, update `ffmpeg/` binaries, or rebuild the Electron package — do not attempt to recompile Java classes here unless you have the upstream source.

Project-specific conventions & patterns

- Bundled runtime: distributions ending with `-jre` are complete packages containing a private `jre/` folder.
- Treat `lib/` and `ffmpeg/` as payload directories (resources, binaries). Changes here should be conservative and tested by running `ctbrec.bat`.
- Electron app is self-contained under `lib/browser/resources/app/`. Use that folder's README for build and packaging flags.

Integration points & external dependencies

- Java runtime: app uses the bundled JRE in `jre/` (Java >= 10 per top-level README).
- Electron/Node: browser UI requires Node.js and npm to build locally.
- FFmpeg: used for encoding/recording — ensure platform-appropriate binaries are present in `ffmpeg/`.

Safety & security notes

- Credentials: the top-level README warns credentials are stored unencrypted. Avoid suggesting changes that would expose or mishandle credentials; highlight the risk when making changes to settings or storage.

When to ask the user

- If you need to modify Java source code or change program logic beyond packaging/docs, ask where the authoritative upstream source tree is — this repo is the packaged distribution.
- Ask before replacing `jre/` or `ffmpeg/` binaries — these are sensitive release artifacts.

If you make edits

- Keep changes minimal and document them in the top-level `README.md` or a changelog file.
- After modifying packaging or browser resources, verify by running `ctbrec.bat` on Windows or the rebuilt Electron package locally.

Examples (copyable)

- Run packaged app (Windows):
  - `ctbrec.bat`
- Build browser UI (cmd):
  - `cd lib\\browser\\resources\\app`
  - `npm install`
  - `node node_modules/electron-packager/bin/electron-packager.js --overwrite . ctbrec-minimal-browser`

<!-- Copilot / AI Agent Instructions for CTB Recorder (packaged distribution) -->

# Copilot / AI Agent Instructions for CTB Recorder (packaged distribution)

Purpose

- Short: help AI agents and contributors work safely and effectively with this packaged distribution (Java app + embedded browser). This repo is a runnable bundle, not the upstream source tree.

Overview

- This folder is a complete distribution: `ctbrec.bat` runs the bundled JAR using the embedded JRE under `jre/`.
- Editable browser UI sources live in `lib/browser/resources/app/` (Node/Electron). The Java application is distributed as `ctbrec-<version>.jar` and does not include editable Java sources here.
- `ffmpeg/` contains native ffmpeg binaries used for recording/processing — treat them as release payloads.

Quick facts (what to assume)

- Use the bundled `jre/` to reproduce runtime issues. Do not replace or rebuild `jre/` unless explicitly instructed.
- Java sources are not present; for source-level changes ask for the upstream repository or permission to decompile and patch.
- Treat `lib/` and `ffmpeg/` as binary payloads. Small UI/packaging/docs changes are the usual scope of work.

Key files

- `ctbrec.bat` — launcher (Windows).
- `ctbrec-5.3.0.jar` — packaged Java application.
- `jre/` — embedded runtime (binary).
- `lib/browser/resources/app/` — Electron/browser UI sources and packaging scripts.
- `ffmpeg/` — platform ffmpeg binaries.

Common tasks & commands (Windows `cmd.exe`)

- Run the packaged app (captures stdout/stderr):
  - `ctbrec.bat > run.log 2>&1`
- Run the JAR directly with the bundled JRE:
  - `jre\bin\java -Xmx1g -Dfile.encoding=utf-8 -jar ctbrec-5.3.0.jar > run.log 2>&1`
- Start with JDWP (listen on 5005, no suspend):
  - `jre\\bin\\java -Xmx1g -agentlib:jdwp=transport=dt_socket,server=y,suspend=n,address=*:5005 -jar ctbrec-5.3.0.jar`
- Start suspended (useful for debugger attach):
  - `jre\\bin\\java -Xmx1g -agentlib:jdwp=transport=dt_socket,server=y,suspend=y,address=5005 -jar ctbrec-5.3.0.jar`
- Verbose class loading (diagnose missing classes):
  - `jre\\bin\\java -verbose:class -Xmx1g -Dfile.encoding=utf-8 -jar ctbrec-5.3.0.jar > verbose.log 2>&1`

Build the Electron/browser UI (inside `lib\\browser\\resources\\app`)

- `cd lib\\browser\\resources\\app`
- `npm install`
- `npm install electron-packager --save-dev`
- `node node_modules\\electron-packager\\bin\\electron-packager.js --overwrite . ctbrec-minimal-browser`

Debugging guidance

- Preferred approach: run the JVM suspended (`suspend=y`) and attach from your IDE (VS Code Java extension or IntelliJ). Use `.vscode/launch.json` with an "Attach" config for port 5005.
- If using `jdb`, attach with `jre\\bin\\jdb -connect com.sun.jdi.SocketAttach:hostname=localhost,port=5005` and inspect `locals` at the crash site.
- Capture logs when reproducing issues and include: `run.log`, `verbose.log`, and JVM stdout/stderr.

Safety & security

- Credentials: the app stores some settings (including credentials) unencrypted under `%APPDATA%\\ctbrec\\<version>\\settings.json`. Never commit or expose these files. Redact secrets before sharing logs.
- Don’t replace `jre/` or `ffmpeg/` binaries without permission from a release owner.

When to ask the user (permission/inputs you must request)

- For any source-level Java change: ask where the authoritative upstream repository is (we need sources to rebuild). If user permits, agent can decompile the JAR, propose a patch, and request a signed-off source change.
- Before replacing or upgrading `jre/` or `ffmpeg/` in the distribution.

Troubleshooting checklist (quick)

- Reproduce with the bundled JRE and capture `run.log`.
- If you see crashes during initialization, run suspended with JDWP and attach a debugger to inspect locals at the failing line.
- For missing native errors, confirm platform ffmpeg binaries exist under `ffmpeg/` and executable permissions are correct.
- For packaging problems, capture `npm install` and `electron-packager` output (`npm-install.log`, `pack.log`) and share them.

Release notes & packaging

- Packaging tasks usually include updating docs, swapping `ffmpeg/` binaries, and re-running the Electron packager for the browser UI.
- Use Launch4j or CI workflows (there's an example GitHub Actions workflow in `.github/workflows/`) to generate Windows EXE wrappers if needed.
- Recommended smoke tests on a clean VM: run `ctbrec.bat`, confirm UI loads, recording works, `ffmpeg` invocations succeed, and `%APPDATA%\\ctbrec\\<version>\\settings.json` is created.

If you want me to extend this file

- Tell me which areas to expand (debugging steps, CI commands, signing steps, or upstream repo links). I can also add ready-to-run `cmd` scripts for common tasks.

<!-- End -->

Notes for code edits and debugging

- This workspace is a distribution bundle; if you need to change Java source, look for the upstream repository (packaged JARs indicate source is likely not present here).
- For runtime issues, reproduce exactly using the bundled JRE to avoid environmental differences: `jre\bin\java -Xmx1g -Dfile.encoding=utf-8 -jar ctbrec-5.3.0.jar`.
- When modifying UI in the Electron/browser component, run the `npm install` + `electron-packager` flow in `lib\browser\resources\app` and test the resulting app.

Patterns & conventions to follow when making changes

- Preserve the bundled-launcher behavior: do not change the expected jar name or command-line flags without updating `ctbrec.bat` and any packaging scripts.
- Keep user credentials handling in mind (they are stored unencrypted); avoid code changes that could leak or mishandle these files.
- Respect GPLv3 licensing for any distributed artifacts.

Integration points to watch for

- The UI/browser component and the Java core are distinct packaging units; changes to one may require rebuilding and re-packaging the final distribution.
- Native packaging and cross-platform builds are done via `electron-packager` for the browser — check `lib/browser/resources/app/README.md` for platform flags.

If anything is unclear or you want this extended (examples, debugging checklist, or a note about where full Java sources live), tell me what to add.

- Reproduce with the bundled JRE (Windows):

```
ctbrec.bat
```

Or run the exact JVM command used by the launcher to capture logs directly:

```
jre\bin\java -Xmx1g -Dfile.encoding=utf-8 -jar ctbrec-5.3.0.jar > run.log 2>&1
```

- Capture classloader or verbose output (helps missing resource/class issues):

```
jre\bin\java -verbose:class -Xmx1g -Dfile.encoding=utf-8 -jar ctbrec-5.3.0.jar > verbose.log 2>&1
```

- Enable heap dumps on OOME and capture path (useful for memory problems):

```
jre\bin\java -Xmx1g -XX:+HeapDumpOnOutOfMemoryError -XX:HeapDumpPath=.\\heapdump.hprof -jar ctbrec-5.3.0.jar
```

- Enable remote debugger (attach a JDWP debugger on port 5005):

```
jre\bin\java -Xmx1g -agentlib:jdwp=transport=dt_socket,server=y,suspend=n,address=*:5005 -jar ctbrec-5.3.0.jar
```

    - Then attach from IDE (IntelliJ/VS Code) to `localhost:5005` to set breakpoints and inspect state.

- Use the bundled `logging.properties` if you need to change Java logging behaviour; it's located under `jre\conf\logging.properties` — pass a system property to point at a custom file if needed:

```
jre\bin\java -Djava.util.logging.config.file=jre\\conf\\logging.properties -jar ctbrec-5.3.0.jar
```

- Browser/Electron troubleshooting (UI component):
  - Build or reproduce the browser component in `lib\browser\resources\app` using the README commands (`npm install` + `electron-packager`).
  - If you need to iterate locally, run the Electron packaging steps and inspect the packaged app logs; there isn't a provided `npm start` script in the bundled README, so prefer packaging for a test run.

# VS Code Debugger Attach Example (Port 5006)

To attach the VS Code Java debugger to CTB Recorder running with JDWP on port 5006:

1. Start the application with JDWP enabled on port 5006:

```
jre\bin\java -Xmx1g -agentlib:jdwp=transport=dt_socket,server=y,suspend=n,address=*:5006 -jar ctbrec-5.3.0.jar
```

2. In VS Code, open the Run and Debug view and create or edit `.vscode/launch.json` with an "Attach" configuration for Java. Example config:

```
{
    "version": "0.2.0",
    "configurations": [
        {
            "type": "java",
            "name": "Attach to CTB Recorder (5006)",
            "request": "attach",
            "hostName": "localhost",
            "port": 5006
        }
    ]
}
```

3. Select the "Attach to CTB Recorder (5006)" configuration and click the green Start/Play button to attach the debugger.
4. Set breakpoints in the Java source you have locally (or attach symbol/source mappings if debugging remote/out-of-source jars).
