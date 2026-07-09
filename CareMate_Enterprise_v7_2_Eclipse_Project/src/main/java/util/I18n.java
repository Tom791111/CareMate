package util;

import java.util.Locale;
import java.util.MissingResourceException;
import java.util.ResourceBundle;

/**
 * CareMate Enterprise v7.2 International Edition
 * 全域多語系工具：外籍照顧者登入前於 LanguageFrame 選擇語言，登入後由各 Frame 讀取文字。
 */
public class I18n {
    private static Locale currentLocale = Locale.TAIWAN;
    private static ResourceBundle bundle = loadBundle(currentLocale);

    private I18n() {}

    public static void setLanguage(String code) {
        if (code == null) code = "zh_TW";
        switch (code) {
            case "id": currentLocale = new Locale("id"); break;
            case "vi": currentLocale = new Locale("vi"); break;
            case "fil": currentLocale = new Locale("fil"); break;
            case "en": currentLocale = Locale.ENGLISH; break;
            case "zh_TW":
            default: currentLocale = Locale.TAIWAN; break;
        }
        bundle = loadBundle(currentLocale);
    }

    public static Locale getCurrentLocale() { return currentLocale; }

    public static String text(String key) {
        try {
            return bundle.getString(key);
        } catch (MissingResourceException e) {
            try {
                return loadBundle(Locale.TAIWAN).getString(key);
            } catch (Exception ignore) {
                return key;
            }
        }
    }

    public static String t(String key) { return text(key); }

    private static ResourceBundle loadBundle(Locale locale) {
        return ResourceBundle.getBundle("i18n.messages", locale);
    }
}
