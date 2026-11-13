package gs1;

public class Gs1Util {
    //kontrolne cifre, SSCC, GTIN

    /** Kontrolna cifra po GS1 modulo-10 algoritmu (za GTIN, SSCC...). */
    public static int checkDigit(String digitsWithoutCheck) {
        int sum = 0;
        boolean triple = true; // zdesna: prva cifra se množi sa 3
        for (int i = digitsWithoutCheck.length() - 1; i >= 0; i--) {
            int d = digitsWithoutCheck.charAt(i) - '0';
            sum += triple ? d * 3 : d;
            triple = !triple;
        }
        return (10 - (sum % 10)) % 10;
    }

    /** SSCC = extension digit (1) + GS1 company prefix + serijski broj, ukupno 17 cifara + kontrolna. */
    public static String sscc(int extensionDigit, String companyPrefix, long serial) {
        int serialLen = 16 - companyPrefix.length();
        String base = extensionDigit
                + companyPrefix
                + String.format("%0" + serialLen + "d", serial);
        return base + checkDigit(base);
    }

    public static boolean isValid(String full) {
        return checkDigit(full.substring(0, full.length() - 1))
                == full.charAt(full.length() - 1) - '0';
    }
}
