import java.util.prefs.Preferences;

public class SessionManager {
    private static final String PREFS_NODE = "com/yourapp/auth";
    private static final String KEY_USERNAME = "username";
    private static final String KEY_REMEMBER_ME = "rememberMe";

    public static void saveLoginState(String username, boolean rememberMe) {
        Preferences prefs = Preferences.userRoot().node(PREFS_NODE);
        prefs.put(KEY_USERNAME, rememberMe ? username : "");
        prefs.putBoolean(KEY_REMEMBER_ME, rememberMe);
    }

    public static boolean shouldAutoLogin() {
        Preferences prefs = Preferences.userRoot().node(PREFS_NODE);
        return prefs.getBoolean(KEY_REMEMBER_ME, false);
    }

    public static String getSavedUsername() {
        Preferences prefs = Preferences.userRoot().node(PREFS_NODE);
        return prefs.get(KEY_USERNAME, "");
    }

    public static void clearSavedLogin() {
        Preferences prefs = Preferences.userRoot().node(PREFS_NODE);
        prefs.remove(KEY_USERNAME);
        prefs.putBoolean(KEY_REMEMBER_ME, false);
    }
}