package adjie.made.cataloguemovie2.Utils;

import java.util.Locale;

public class Language {
    public static String getCountry() {
        String country = Locale.getDefault().getCountry().toLowerCase();

        switch (country) {
            case "id":
                break;

            default:
                country = "en";
                break;
        }

        return country;
    }
}
