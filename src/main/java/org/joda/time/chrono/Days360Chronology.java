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
public class Days360Chronology extends BasicFixedMonthChronology {

    /** The lowest year that can be fully supported. */
    private static final int MIN_YEAR = -292275054;

    /** The highest year that can be fully supported. */
    private static final int MAX_YEAR = 292278993;

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
                        // First create without a lower limit.
                        chrono = new Days360Chronology(null, null, minDaysInFirstWeek);
                        // Impose a lower limit and make another Days360Chronology.
                        DateTime lowerLimit = new DateTime(1, 1, 1, 0, 0, 0, 0, chrono);
                        chrono = new Days360Chronology
                            (LimitChronology.getInstance(chrono, lowerLimit, null),
                             null, minDaysInFirstWeek);
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

    long calculateFirstDayOfYearMillis(int year) {
        return (year * 360L) * DateTimeConstants.MILLIS_PER_DAY;
    }

    @Override
    boolean isLeapYear(int year) {
        return false;
    }

    int getMinYear() {
        return MIN_YEAR;
    }

    int getMaxYear() {
        return MAX_YEAR;
    }

    long getApproxMillisAtEpochDividedByTwo() {
        return (1970L * MILLIS_PER_YEAR) / 2;
    }
}
