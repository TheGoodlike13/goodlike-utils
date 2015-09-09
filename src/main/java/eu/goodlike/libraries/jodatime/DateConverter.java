package eu.goodlike.libraries.jodatime;

import org.joda.time.LocalDate;

/**
 * A subset of TimeConverter operations, used in a convenience methods of Time class
 */
public interface DateConverter {

    /**
     * <pre>
     * The assumption behind java.sql.Date is that it will be consumed using a String value, i.e.
     *      date.toString():
     * This uses the JAVA timezone to create a localized instance; therefore, the epoch milliseconds value
     * refers to the start of day at JAVA timezone, and the String value of the Date can get altered during
     * conversion; using toString() method, however, once again uses JAVA timezone, essentially allowing the value
     * to remain correct
     * </pre>
     */
    java.sql.Date toSqlDate();

    LocalDate toJodaLocalDate();

}
