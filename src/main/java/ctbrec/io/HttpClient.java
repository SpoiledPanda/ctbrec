
package ctbrec.io;

import okhttp3.Cache;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.CacheControl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.Authenticator;
import java.net.PasswordAuthentication;
import java.util.concurrent.TimeUnit;

/**
 * PR-ready version of HttpClient with a minimal defensive fix for System.setProperty calls
 * in loadProxySettings().
 *
 * This file is a focused change: it introduces `safeSetProperty(String, String)` and
 * replaces direct `System.setProperty(...)` calls in `loadProxySettings()` so null values
 * are skipped and exceptions are caught. The goal is to make a small, reviewable patch
 * that can be applied to the upstream source.
 */
public abstract class HttpClient {
    private static final Logger log = LoggerFactory.getLogger(HttpClient.class);

    protected ctbrec.io.CookieJarImpl cookieJar;
    protected OkHttpClient client;
    protected Cache cache;
    protected ctbrec.Config config;
    protected boolean loggedIn;
    protected long cacheSize;
    protected int cacheLifeTime = 600;
    private final String name;

    protected HttpClient(String name, ctbrec.Config config) {
        this.name = name;
        this.config = config;
        this.cookieJar = createCookieJar();
        reconfigure();
    }

    protected CookieJarImpl createCookieJar() { return new CookieJarImpl(); }

    /*
     * Minimal defensive changes start here. The original code called System.setProperty(...)
     * directly using values coming from Settings which could be null. That produced a
     * NullPointerException at startup on some installations.
     *
     * We add safeSetProperty() and use it for all places where a possibly-null value
     * could be passed to System.setProperty.
     */
    private void loadProxySettings() {
        ctbrec.Settings settings = config.getSettings();
        if (settings == null) return;

        ctbrec.Settings.ProxyType proxyType = settings.proxyType;
        if (proxyType == ctbrec.Settings.ProxyType.HTTP) {
            safeSetProperty("http.proxyHost", settings.proxyHost);
            safeSetProperty("http.proxyPort", settings.proxyPort);
            safeSetProperty("https.proxyHost", settings.proxyHost);
            safeSetProperty("https.proxyPort", settings.proxyPort);

            if (settings.proxyUser != null && !settings.proxyUser.isEmpty()) {
                safeSetProperty("https.proxyUser", settings.proxyUser);
                safeSetProperty("https.proxyPassword", settings.proxyPassword);
            }
        } else if (proxyType == ctbrec.Settings.ProxyType.SOCKS4) {
            safeSetProperty("socksProxyVersion", "4");
            safeSetProperty("socksProxyHost", settings.proxyHost);
            safeSetProperty("socksProxyPort", settings.proxyPort);
        } else if (proxyType == ctbrec.Settings.ProxyType.SOCKS5) {
            safeSetProperty("socksProxyVersion", "5");
            safeSetProperty("socksProxyHost", settings.proxyHost);
            safeSetProperty("socksProxyPort", settings.proxyPort);

            if (settings.proxyUser != null && !settings.proxyUser.isEmpty()) {
                Authenticator.setDefault(new SocksProxyAuth(settings.proxyUser, settings.proxyPassword));
            }
        } else {
            // NONE: clear any previously-set proxy properties
            System.clearProperty("http.proxyHost");
            System.clearProperty("http.proxyPort");
            System.clearProperty("https.proxyHost");
            System.clearProperty("https.proxyPort");
            System.clearProperty("socksProxyVersion");
            System.clearProperty("socksProxyHost");
            System.clearProperty("socksProxyPort");
            System.clearProperty("java.net.socks.username");
            System.clearProperty("java.net.socks.password");
            System.clearProperty("https.proxyUser");
            System.clearProperty("https.proxyPassword");
        }
    }

    /**
     * Sets a system property only if the value is non-null. Catches and logs any
     * throwable to avoid bringing down startup for unexpected SecurityManager or
     * other runtime issues.
     */
    private void safeSetProperty(String key, String value) {
        if (value == null) return;
        try {
            System.setProperty(key, value);
        } catch (Exception e) {
            log.warn("Failed to set system property {}: {}", key, e.getMessage());
        }
    }

    private static class SocksProxyAuth extends Authenticator {
        private final String user;
        private final String pass;

        SocksProxyAuth(String u, String p) { this.user = u; this.pass = p; }

        @Override
        protected PasswordAuthentication getPasswordAuthentication() {
            return new PasswordAuthentication(user, pass == null ? new char[0] : pass.toCharArray());
        }
    }

    /*
     * Remaining class is intentionally elided for the PR; the upstream source contains
     * other methods (execute, reconfigure, cookie handling, etc.). This change targets
     * only the proxy setup behavior. When applying the patch upstream, ensure you keep
     * the rest of the original class intact and only replace/modify the loadProxySettings
     * method and add safeSetProperty + SocksProxyAuth.
     */
}
