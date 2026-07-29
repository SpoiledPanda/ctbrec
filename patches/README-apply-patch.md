This folder contains the generated patch file `patch-httpclient-cookie-loading.patch` which
fixes two issues observed when running the packaged Windows distribution:

- Guards against `System.setProperty(null, ...)` calls in `HttpClient` (prevents NPE on
  startup).
- Loads pre-existing cookies from `%APPDATA%\ctbrec` (cookies-*.json) into the HTTP
  client's cookie jar so site clients can reuse logins during testing.

How to apply
-------------

1. Prefer `git am` if you are working against the canonical Java source repository that
   has the same history/paths used to generate the patch. From the repo root:

```cmd
git am patches\patch-httpclient-cookie-loading.patch
```

2. If `git am` fails (different history), try `git apply` which will apply hunks to the
   working tree without committing (you can review and commit manually):

```cmd
git apply --index patches\patch-httpclient-cookie-loading.patch
git add -A
git commit -m "io: guard System.setProperty and load cookies (applied patch)"
```

3. If paths differ (packaged distribution vs upstream source), open the patch file and
   manually copy the changes into these files in your source tree (expected locations):

- `src/main/java/ctbrec/io/HttpClient.java`
- `src/main/java/ctbrec/sites/streamate/StreamateHttpClient.java`

Build instructions
------------------

- Maven:

```cmd
mvn -DskipTests=true package
```

- Gradle (Windows wrapper):

```cmd
gradlew.bat build -x test
```

Verification / smoke test
-------------------------

1. Run unit tests (optional) or run the application locally and watch logs.
2. Expected observable behaviour: during startup the application should log lines like
   `Loaded N cookies from ...\cookies-*.json` (these confirm the cookie seeding code ran).
3. Packaged-distribution verification (what we tested locally): we launched the packaged
   app with the patched classes placed earlier on the classpath; see the captured log at

   `work/run_with_cookies_afterpatch.log` (in the workspace) for example output.

Notes & caveats
---------------

- The patch was generated from a packaged distribution snapshot and may need path
  adjustments for the canonical source layout.
- During our local testing we observed many `java.net.SocketException: Permission denied`
  errors when exercising web requests; that is an environment/network restriction and
  is unrelated to the patch itself. Please verify networking/firewall settings before
  concluding functional behavior of remote network requests.
- If you prefer, we can submit a source-level PR directly against the upstream repo —
  provide the upstream source URL and we will create the branch and PR with the same
  changes.

Contact / context
-----------------

This patch was created from the packaged distribution in the `fix/httpclient-null-proxy-properties`
work branch. See the workspace artifacts for runtime evidence and logs:

- `work/patch-httpclient-cookie-loading.patch`  — generated patch
- `work/run_with_cookies_afterpatch.log`        — runtime log showing cookie loads

If you need any adjustments to the patch, tell me which upstream repo and I will apply
and open a source PR for you.
