This pull request adds a ready-to-apply patch that fixes two problems observed when
running the packaged Windows distribution:

- Guards `System.setProperty` calls in `HttpClient` to avoid NullPointerException on
  startup.
- Loads cookie JSON (`cookies-*.json`) from `%APPDATA%\ctbrec` into the HTTP client's
  cookie jar to enable restoring logged-in sessions for site clients.

Patch file is included at `patches/patch-httpclient-cookie-loading.patch`.

How to apply
------------

1. If you have the upstream source repository available and the repo history matches:

```cmd
git am patches\patch-httpclient-cookie-loading.patch
```

2. If `git am` fails, apply with `git apply` and commit manually:

```cmd
git apply --index patches\patch-httpclient-cookie-loading.patch
git add -A
git commit -m "io: guard System.setProperty and load cookies (applied patch)"
```

3. If your source tree layout differs, copy the patch changes into the corresponding
   files (expected):

- `src/main/java/ctbrec/io/HttpClient.java`
- `src/main/java/ctbrec/sites/streamate/StreamateHttpClient.java`

Build & test
------------

- Maven: `mvn -DskipTests=true package`
- Gradle: `gradlew.bat build -x test`

Quick verification:

- After building and running, look for startup log lines like `Loaded N cookies from ...\cookies-*.json`.
- Example runtime evidence: see workspace `work/run_with_cookies_afterpatch.log` showing cookies loaded during our packaged-distribution tests.

Notes
-----

- The patch was generated from the packaged distribution snapshot rather than the
  canonical source repo; path differences may require manual application.
- During verification we observed `java.net.SocketException: Permission denied` in our
  environment — please ensure network/firewall settings permit outbound connections when testing.

If maintainers want, I can apply the changes and open a source-level PR directly to an
upstream repo (please provide the upstream source URL). Otherwise this patch and the
included instructions should be sufficient for maintainers to apply and verify.

Related: packaged-distribution test PR (adds the patch file here): https://github.com/SpoiledPanda/ctbrec/pull/3
