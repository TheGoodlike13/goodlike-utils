package eu.goodlike.test;

import org.joda.time.LocalDate;

import java.math.BigDecimal;

/**
 * <pre>
 * Used to create various, usually correct values of "fake" objects for testing; they are somewhat context aware, i.e.
 * an e-mail will have a structure of an e-mail, rather than just a random String, but other than that they are
 * completely made up
 *
 * The values are all as different from each other as possible, that is, for every id value a different kind of
 * result will be produced; if there are fewer values of expected object than id values, i.e. 31 days of month
 * and 2^63-1 of long values, then any set of adjacent ids will produce unique objects until the limit of expected
 * objects is reached, i.e. for any 31 adjacent ids, 31 different days of month will be produced; another example:
 * no two adjacent ids will produce the same boolean value;
 *
 * Also, when using the same id across multiple methods, the results will have certain similarities, which can allow
 * to distinguish that the same id was used for all the fields, i.e. date() should end with the same day of month as
 * day(); name() and surname() will have similar suffixes, and so on;
 *
 * Only non-negative ids are supported; negative ids may produce illegal objects, or violate the rules documented above
 * </pre>
 */
public final class Fake {

    public static String name(long id) {
        return "Name" + id;
    }

    public static String surname(long id) {
        return "Surname" + id;
    }

    public static String email(long id) {
        return "fake" + id + "@fake.lt";
    }

    public static String city(long id) {
        return "City" + id;
    }

    public static String phone(long id) {
        return "860000000" + id;
    }

    public static String picture(long id) {
        return "Picture" + id + ".jpg";
    }

    public static String document(long id) {
        return "Document" + id;
    }

    public static String comment(long id) {
        return "Comment" + id;
    }

    public static BigDecimal wage(long id) {
        return BigDecimal.valueOf(id).setScale(4, BigDecimal.ROUND_UNNECESSARY);
    }

    public static int day(long id) {
        if (id == 0) return 31;
        return (int)((id - 1) % 31) + 1;
    }

    public static String website(long id) {
        return "http://website" + id + ".eu/";
    }

    public static boolean Boolean(long id) {
        return (id & 1) == 1;
    }

    public static String languageLevel(long id) {
        return "English: C" + id;
    }

    public static int hour(long id) {
        return (int)(id % 24);
    }

    public static int minute(long id) {
        return (int)(id % 60);
    }

    public static int id(long id) {
        return (int)id;
    }

    public static LocalDate localDate(long id) {
        return LocalDate.parse("2015-01-" + dayString(id));
    }

    public static String dayString(long id) {
        String dayString = String.valueOf(day(id));
        if (dayString.length() == 1)
            dayString = 0 + dayString;
        return dayString;
    }

    public static String language(long id) {
        if (id < 0) id = -id;
        return "L" + (id % 100);
    }

    public static String password(long id) {
        return "canYouGuessMe?nah" + id;
    }

    public static int duration(long id) {
        return (int)(45 * id);
    }

    public static int code(long id) {
        long code = 3;
        while (code < 900000)
            code *= id + code;

        return (int)(id % 900000) + 100000;
    }

    public static long time(long id) {
        return id * 1000000000;
    }

    // PRIVATE

    private Fake() {
        throw new AssertionError("You should not be instantiating this class, use static methods/fields instead!");
    }

}
