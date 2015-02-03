package org.joda.time.chrono;

import junit.framework.TestCase;
import junit.framework.TestSuite;

import org.joda.time.*;

import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

/**
 * This class is a Junit unit test for Days360Chronology.
 *
 * @author Daniel Dyba
 */
public class TestDays360Chronology extends TestCase {

    public static void main(String[] args) {
        junit.textui.TestRunner.run(suite());
    }

    public static TestSuite suite() {
        return new TestSuite(TestDays360Chronology.class);
    }

    public TestDays360Chronology(String name) {
        super(name);
    }

    private static final int MILLIS_PER_DAY = DateTimeConstants.MILLIS_PER_DAY;
    private static final DateTimeZone PARIS = DateTimeZone.forID("Europe/Paris");
    private static final DateTimeZone LONDON = DateTimeZone.forID("Europe/London");
    private static final DateTimeZone TOKYO = DateTimeZone.forID("Asia/Tokyo");
    private static final Chronology DAYS360_UTC = Days360Chronology.getInstanceUTC();
    private static final Chronology GREGORIAN_UTC = GregorianChronology.getInstanceUTC();
    private static final Chronology ISO_UTC = ISOChronology.getInstanceUTC();

    long y2002days = 365 + 365 + 366 + 365 + 365 + 365 + 366 + 365 + 365 + 365 +
            366 + 365 + 365 + 365 + 366 + 365 + 365 + 365 + 366 + 365 +
            365 + 365 + 366 + 365 + 365 + 365 + 366 + 365 + 365 + 365 +
            366 + 365;
    // 2002-06-09
    private long TEST_TIME_NOW =
            (y2002days + 31L + 28L + 31L + 30L + 31L + 9L -1L) * DateTimeConstants.MILLIS_PER_DAY;

    private DateTimeZone originalDateTimeZone = null;
    private TimeZone originalTimeZone = null;
    private Locale originalLocale = null;

    protected void setUp() throws Exception {
        DateTimeUtils.setCurrentMillisFixed(TEST_TIME_NOW);
        originalDateTimeZone = DateTimeZone.getDefault();
        originalTimeZone = TimeZone.getDefault();
        originalLocale = Locale.getDefault();
        DateTimeZone.setDefault(LONDON);
        TimeZone.setDefault(TimeZone.getTimeZone("Europe/London"));
        Locale.setDefault(Locale.UK);
    }

    protected void tearDown() throws Exception {
        DateTimeUtils.setCurrentMillisSystem();
        DateTimeZone.setDefault(originalDateTimeZone);
        TimeZone.setDefault(originalTimeZone);
        Locale.setDefault(originalLocale);
        originalDateTimeZone = null;
        originalTimeZone = null;
        originalLocale = null;
    }

    //-----------------------------------------------------------------------
    public void testFactoryUTC() {
        assertEquals(DateTimeZone.UTC, Days360Chronology.getInstanceUTC().getZone());
        assertSame(Days360Chronology.class, Days360Chronology.getInstanceUTC().getClass());
    }

    public void testFactory() {
        assertEquals(LONDON, Days360Chronology.getInstance().getZone());
        assertSame(Days360Chronology.class, Days360Chronology.getInstance().getClass());
    }

    public void testFactory_Zone() {
        assertEquals(TOKYO, Days360Chronology.getInstance(TOKYO).getZone());
        assertEquals(PARIS, Days360Chronology.getInstance(PARIS).getZone());
        assertEquals(LONDON, Days360Chronology.getInstance(null).getZone());
        assertSame(Days360Chronology.class, Days360Chronology.getInstance(TOKYO).getClass());
    }

    //-----------------------------------------------------------------------
    public void testEquality() {
        assertSame(Days360Chronology.getInstance(TOKYO), Days360Chronology.getInstance(TOKYO));
        assertSame(Days360Chronology.getInstance(LONDON), Days360Chronology.getInstance(LONDON));
        assertSame(Days360Chronology.getInstance(PARIS), Days360Chronology.getInstance(PARIS));
        assertSame(Days360Chronology.getInstanceUTC(), Days360Chronology.getInstanceUTC());
        assertSame(Days360Chronology.getInstance(), Days360Chronology.getInstance(LONDON));
    }

    public void testWithUTC() {
        assertSame(Days360Chronology.getInstanceUTC(), Days360Chronology.getInstance(LONDON).withUTC());
        assertSame(Days360Chronology.getInstanceUTC(), Days360Chronology.getInstance(TOKYO).withUTC());
        assertSame(Days360Chronology.getInstanceUTC(), Days360Chronology.getInstanceUTC().withUTC());
        assertSame(Days360Chronology.getInstanceUTC(), Days360Chronology.getInstance().withUTC());
    }

    public void testWithZone() {
        assertSame(Days360Chronology.getInstance(TOKYO), Days360Chronology.getInstance(TOKYO).withZone(TOKYO));
        assertSame(Days360Chronology.getInstance(LONDON), Days360Chronology.getInstance(TOKYO).withZone(LONDON));
        assertSame(Days360Chronology.getInstance(PARIS), Days360Chronology.getInstance(TOKYO).withZone(PARIS));
        assertSame(Days360Chronology.getInstance(LONDON), Days360Chronology.getInstance(TOKYO).withZone(null));
        assertSame(Days360Chronology.getInstance(PARIS), Days360Chronology.getInstance().withZone(PARIS));
        assertSame(Days360Chronology.getInstance(PARIS), Days360Chronology.getInstanceUTC().withZone(PARIS));
    }

    public void testToString() {
        assertEquals("Days360Chronology[Europe/London]", Days360Chronology.getInstance(LONDON).toString());
        assertEquals("Days360Chronology[Asia/Tokyo]", Days360Chronology.getInstance(TOKYO).toString());
        assertEquals("Days360Chronology[Europe/London]", Days360Chronology.getInstance().toString());
        assertEquals("Days360Chronology[UTC]", Days360Chronology.getInstanceUTC().toString());
    }

    //-----------------------------------------------------------------------
    public void testDurationFields() {
        final Days360Chronology days360 = Days360Chronology.getInstance();
        assertEquals("eras", days360.eras().getName());
        assertEquals("centuries", days360.centuries().getName());
        assertEquals("years", days360.years().getName());
        assertEquals("weekyears", days360.weekyears().getName());
        assertEquals("months", days360.months().getName());
        assertEquals("weeks", days360.weeks().getName());
        assertEquals("days", days360.days().getName());
        assertEquals("halfdays", days360.halfdays().getName());
        assertEquals("hours", days360.hours().getName());
        assertEquals("minutes", days360.minutes().getName());
        assertEquals("seconds", days360.seconds().getName());
        assertEquals("millis", days360.millis().getName());

        assertEquals(false, days360.eras().isSupported());
        assertEquals(true, days360.centuries().isSupported());
        assertEquals(true, days360.years().isSupported());
        assertEquals(true, days360.weekyears().isSupported());
        assertEquals(true, days360.months().isSupported());
        assertEquals(true, days360.weeks().isSupported());
        assertEquals(true, days360.days().isSupported());
        assertEquals(true, days360.halfdays().isSupported());
        assertEquals(true, days360.hours().isSupported());
        assertEquals(true, days360.minutes().isSupported());
        assertEquals(true, days360.seconds().isSupported());
        assertEquals(true, days360.millis().isSupported());

        assertEquals(false, days360.centuries().isPrecise());
        assertEquals(false, days360.years().isPrecise());
        assertEquals(false, days360.weekyears().isPrecise());
        assertEquals(false, days360.months().isPrecise());
        assertEquals(false, days360.weeks().isPrecise()); // Why is this failing???
        assertEquals(false, days360.days().isPrecise());
        assertEquals(false, days360.halfdays().isPrecise());
        assertEquals(true, days360.hours().isPrecise());
        assertEquals(true, days360.minutes().isPrecise());
        assertEquals(true, days360.seconds().isPrecise());
        assertEquals(true, days360.millis().isPrecise());

        final Days360Chronology days360UTC = Days360Chronology.getInstanceUTC();
        assertEquals(false, days360UTC.centuries().isPrecise());
        assertEquals(false, days360UTC.years().isPrecise());
        assertEquals(false, days360UTC.weekyears().isPrecise());
        assertEquals(false, days360UTC.months().isPrecise());
        assertEquals(true, days360UTC.weeks().isPrecise());
        assertEquals(true, days360UTC.days().isPrecise());
        assertEquals(true, days360UTC.halfdays().isPrecise());
        assertEquals(true, days360UTC.hours().isPrecise());
        assertEquals(true, days360UTC.minutes().isPrecise());
        assertEquals(true, days360UTC.seconds().isPrecise());
        assertEquals(true, days360UTC.millis().isPrecise());

        final DateTimeZone gmt = DateTimeZone.forID("Etc/GMT");
        final Days360Chronology days360GMT = Days360Chronology.getInstance(gmt);
        assertEquals(false, days360GMT.centuries().isPrecise());
        assertEquals(false, days360GMT.years().isPrecise());
        assertEquals(false, days360GMT.weekyears().isPrecise());
        assertEquals(false, days360GMT.months().isPrecise());
        assertEquals(true, days360GMT.weeks().isPrecise());
        assertEquals(true, days360GMT.days().isPrecise());
        assertEquals(true, days360GMT.halfdays().isPrecise());
        assertEquals(true, days360GMT.hours().isPrecise());
        assertEquals(true, days360GMT.minutes().isPrecise());
        assertEquals(true, days360GMT.seconds().isPrecise());
        assertEquals(true, days360GMT.millis().isPrecise());
    }

    public void testDateFields() {
        final Days360Chronology days360 = Days360Chronology.getInstance();
        assertEquals("era", days360.era().getName());
        assertEquals("centuryOfEra", days360.centuryOfEra().getName());
        assertEquals("yearOfCentury", days360.yearOfCentury().getName());
        assertEquals("yearOfEra", days360.yearOfEra().getName());
        assertEquals("year", days360.year().getName());
        assertEquals("monthOfYear", days360.monthOfYear().getName());
        assertEquals("weekyearOfCentury", days360.weekyearOfCentury().getName());
        assertEquals("weekyear", days360.weekyear().getName());
        assertEquals("weekOfWeekyear", days360.weekOfWeekyear().getName());
        assertEquals("dayOfYear", days360.dayOfYear().getName());
        assertEquals("dayOfMonth", days360.dayOfMonth().getName());
        assertEquals("dayOfWeek", days360.dayOfWeek().getName());

        assertEquals(true, days360.era().isSupported());
        assertEquals(true, days360.centuryOfEra().isSupported());
        assertEquals(true, days360.yearOfCentury().isSupported());
        assertEquals(true, days360.yearOfEra().isSupported());
        assertEquals(true, days360.year().isSupported());
        assertEquals(true, days360.monthOfYear().isSupported());
        assertEquals(true, days360.weekyearOfCentury().isSupported());
        assertEquals(true, days360.weekyear().isSupported());
        assertEquals(true, days360.weekOfWeekyear().isSupported());
        assertEquals(true, days360.dayOfYear().isSupported());
        assertEquals(true, days360.dayOfMonth().isSupported());
        assertEquals(true, days360.dayOfWeek().isSupported());

        assertEquals(days360.eras(), days360.era().getDurationField());
        assertEquals(days360.centuries(), days360.centuryOfEra().getDurationField());
        assertEquals(days360.years(), days360.yearOfCentury().getDurationField());
        assertEquals(days360.years(), days360.yearOfEra().getDurationField());
        assertEquals(days360.years(), days360.year().getDurationField());
        assertEquals(days360.months(), days360.monthOfYear().getDurationField());
        assertEquals(days360.weekyears(), days360.weekyearOfCentury().getDurationField());
        assertEquals(days360.weekyears(), days360.weekyear().getDurationField());
        assertEquals(days360.weeks(), days360.weekOfWeekyear().getDurationField());
        assertEquals(days360.days(), days360.dayOfYear().getDurationField());
        assertEquals(days360.days(), days360.dayOfMonth().getDurationField());
        assertEquals(days360.days(), days360.dayOfWeek().getDurationField());

        assertEquals(null, days360.era().getRangeDurationField());
        assertEquals(days360.eras(), days360.centuryOfEra().getRangeDurationField());
        assertEquals(days360.centuries(), days360.yearOfCentury().getRangeDurationField());
        assertEquals(days360.eras(), days360.yearOfEra().getRangeDurationField());
        assertEquals(null, days360.year().getRangeDurationField());
        assertEquals(days360.years(), days360.monthOfYear().getRangeDurationField());
        assertEquals(days360.centuries(), days360.weekyearOfCentury().getRangeDurationField());
        assertEquals(null, days360.weekyear().getRangeDurationField());
        assertEquals(days360.weekyears(), days360.weekOfWeekyear().getRangeDurationField());
        assertEquals(days360.years(), days360.dayOfYear().getRangeDurationField());
        assertEquals(days360.months(), days360.dayOfMonth().getRangeDurationField());
        assertEquals(days360.weeks(), days360.dayOfWeek().getRangeDurationField());
    }

    public void testTimeFields() {
        final Days360Chronology days360 = Days360Chronology.getInstance();
        assertEquals("halfdayOfDay", days360.halfdayOfDay().getName());
        assertEquals("clockhourOfHalfday", days360.clockhourOfHalfday().getName());
        assertEquals("hourOfHalfday", days360.hourOfHalfday().getName());
        assertEquals("clockhourOfDay", days360.clockhourOfDay().getName());
        assertEquals("hourOfDay", days360.hourOfDay().getName());
        assertEquals("minuteOfDay", days360.minuteOfDay().getName());
        assertEquals("minuteOfHour", days360.minuteOfHour().getName());
        assertEquals("secondOfDay", days360.secondOfDay().getName());
        assertEquals("secondOfMinute", days360.secondOfMinute().getName());
        assertEquals("millisOfDay", days360.millisOfDay().getName());
        assertEquals("millisOfSecond", days360.millisOfSecond().getName());

        assertEquals(true, days360.halfdayOfDay().isSupported());
        assertEquals(true, days360.clockhourOfHalfday().isSupported());
        assertEquals(true, days360.hourOfHalfday().isSupported());
        assertEquals(true, days360.clockhourOfDay().isSupported());
        assertEquals(true, days360.hourOfDay().isSupported());
        assertEquals(true, days360.minuteOfDay().isSupported());
        assertEquals(true, days360.minuteOfHour().isSupported());
        assertEquals(true, days360.secondOfDay().isSupported());
        assertEquals(true, days360.secondOfMinute().isSupported());
        assertEquals(true, days360.millisOfDay().isSupported());
        assertEquals(true, days360.millisOfSecond().isSupported());
    }

    //-----------------------------------------------------------------------
    public void testEpoch() {
        DateTime epoch = new DateTime(1, 1, 1, 0, 0, 0, 0, DAYS360_UTC);
        assertEquals(new DateTime(1, 1, 1, 0, 0, 0, 0, GREGORIAN_UTC), epoch.withChronology(GREGORIAN_UTC));
    }

    //-----------------------------------------------------------------------
    /**
     * Tests era, year, monthOfYear, dayOfMonth and dayOfWeek.
     */

    public void testIgnoresLastDayOfTheMonthInCalculations() {
        DateTime dt0131 = new DateTime(2008, 1, 30, 0, 0, 0, 0, DAYS360_UTC);
        DateTime dt0201 = new DateTime(2008, 2, 1, 0, 0, 0, 0, DAYS360_UTC);
        DateTime dt0101 = new DateTime(2008, 1, 1, 0, 0, 0, 0, DAYS360_UTC);
        DateTime dt1231 = new DateTime(2008, 12, 31, 0, 0, 0, 0, DAYS360_UTC);

        Interval interval1 = new Interval(dt0131, dt0201);
        Days days1 = Days.daysIn(interval1);

        assertEquals(1, days1.getDays());

        Interval interval2 = new Interval(dt0101, dt1231);
        Days days2 = Days.daysIn(interval2);

        assertEquals(360, days2);

        Interval interval3 = new Interval(dt0101, dt0201);
        Days days3 = Days.daysIn(interval3);

        assertEquals(30, interval3);
    }

}
