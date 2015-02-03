package org.joda.time.chrono;

import org.joda.time.Chronology;
import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.joda.time.DateTimeConstants;

import java.util.concurrent.ConcurrentHashMap;

/**
 * Implements the 360-day calendar system, which defines every month to be a
 * 30 day month. There are no leap years. The year is broken down into 12 months,
 * each 30 days in length.
 * <p>
 * The 360 day calendar begins at the same time as the Gregorian calendar.
 * <p>
 * This implementation defines a day as midnight to midnight exactly as per
 * the ISO chronology.
 * <p>
 * Days360Chronology is thread-safe and immutable.
 *
 * @see <a href="http://en.wikipedia.org/wiki/360-day_calendar">Wikipedia</a>
 * @see GregorianChronology
 *
 * @author Daniel Dyba
 * @since 2.8
 *
 */
public final class Days360Chronology extends BasicGJChronology {

    /** The lowest year that can be fully supported. Should be the same as the Gregorian calendar */
    private static final int MIN_YEAR = -292275054;

    /** The highest year that can be fully supported. Should be the same as the Gregorian calendar */
    private static final int MAX_YEAR = 292278993;

    private static final int DAYS_0000_TO_1970 = 719527;

    private static final long MILLIS_PER_YEAR = (long) (365.2425 * DateTimeConstants.MILLIS_PER_DAY);

    private static  final long MILLIS_PER_MONTH = (long) (365.2425 * DateTimeConstants.MILLIS_PER_DAY / 12);

    /** Singleton instance of a UTC Days360Chronology */
    private static final Days360Chronology INSTANCE_UTC;

    /** Cache of zone to chronology arrays */
    private static final ConcurrentHashMap<DateTimeZone, Days360Chronology[]> cCache = new ConcurrentHashMap<DateTimeZone, Days360Chronology[]>();

    static {
        INSTANCE_UTC = getInstance(DateTimeZone.UTC);
    }

    public static Days360Chronology getInstanceUTC() {
        return INSTANCE_UTC;
    }

    public static Days360Chronology getInstance() {
        return getInstance(DateTimeZone.getDefault(), 4);
    }

    public static Days360Chronology getInstance(DateTimeZone zone) {
        return getInstance(zone, 4);
    }

    public static Days360Chronology getInstance(DateTimeZone zone, int minDaysInFirstWeek) {
        if (zone == null) {
            zone = DateTimeZone.getDefault();
        }
        Days360Chronology chrono;
        Days360Chronology[] chronos = cCache.get(zone);
        if (chronos == null) {
            chronos = new Days360Chronology[7];
            Days360Chronology[] oldChronos = cCache.putIfAbsent(zone, chronos);
            if (oldChronos != null) {
                chronos = oldChronos;
            }
        }
        try {
            chrono = chronos[minDaysInFirstWeek - 1];
        } catch (ArrayIndexOutOfBoundsException e) {
            throw new IllegalArgumentException
                    ("Invalid min days in first week: " + minDaysInFirstWeek);
        }
        if (chrono == null) {
            synchronized (chronos) {
                chrono = chronos[minDaysInFirstWeek - 1];
                if (chrono == null) {
                    if (zone == DateTimeZone.UTC) {
                        chrono = new Days360Chronology(null, null, minDaysInFirstWeek);
                    } else {
                        chrono = getInstance(DateTimeZone.UTC, minDaysInFirstWeek);
                        chrono = new Days360Chronology
                                (ZonedChronology.getInstance(chrono, zone), null, minDaysInFirstWeek);
                    }
                    chronos[minDaysInFirstWeek - 1] = chrono;
                }
            }
        }
        return chrono;
    }

    // Constructors and instance variables
    //-----------------------------------------------------------------------

    /**
     * Restricted constructor
     */
    private Days360Chronology(Chronology base, Object param, int minDaysInFirstWeek) {
        super(base, param, minDaysInFirstWeek);
    }

    /**
     * Serialization singleton
     */
    private Object readResolve() {
        Chronology base = getBase();
        int minDays = getMinimumDaysInFirstWeek();
        minDays = (minDays == 0 ? 4 : minDays);  // handle rename of BaseGJChronology
        return base == null ?
                getInstance(DateTimeZone.UTC, minDays) :
                getInstance(base.getZone(), minDays);
    }

    // Conversion
    //-----------------------------------------------------------------------
    /**
     * Gets the Chronology in the UTC time zone.
     *
     * @return the chronology in UTC
     */
    public Chronology withUTC() {
        return INSTANCE_UTC;
    }

    /**
     * Gets the Chronology in a specific time zone.
     *
     * @param zone  the zone to get the chronology in, null is default
     * @return the chronology
     */
    public Chronology withZone(DateTimeZone zone) {
        if (zone == null) {
            zone = DateTimeZone.getDefault();
        }
        if (zone == getZone()) {
            return this;
        }
        return getInstance(zone);
    }


    boolean isLeapYear(int year) {
        return ((year & 3) == 0) && ((year % 100) != 0 || (year % 400) == 0);
    }

    long calculateFirstDayOfYearMillis(int year) {
        // Initial value is just temporary.
        int leapYears = year / 100;
        if (year < 0) {
            // Add 3 before shifting right since /4 and >>2 behave differently
            // on negative numbers. When the expression is written as
            // (year / 4) - (year / 100) + (year / 400),
            // it works for both positive and negative values, except this optimization
            // eliminates two divisions.
            leapYears = ((year + 3) >> 2) - leapYears + ((leapYears + 3) >> 2) - 1;
        } else {
            leapYears = (year >> 2) - leapYears + (leapYears >> 2);
            if (isLeapYear(year)) {
                leapYears--;
            }
        }

        return (year * 365L + (leapYears - DAYS_0000_TO_1970)) * DateTimeConstants.MILLIS_PER_DAY;
    }

    int getMinYear() {
        return MIN_YEAR;
    }

    int getMaxYear() {
        return MAX_YEAR;
    }

    long getAverageMillisPerYear() {
        return MILLIS_PER_YEAR;
    }

    long getAverageMillisPerMonth() {
        return MILLIS_PER_MONTH;
    }

    long getAverageMillisPerYearDividedByTwo() {
        return MILLIS_PER_YEAR / 2;
    }

    long getApproxMillisAtEpochDividedByTwo() {
        return (1970L * MILLIS_PER_YEAR) / 2;
    }
}
