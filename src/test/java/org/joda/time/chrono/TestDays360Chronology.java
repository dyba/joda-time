package org.joda.time.chrono;

import junit.framework.TestCase;
import junit.framework.TestSuite;

import org.joda.time.Chronology;
import org.joda.time.DateTime;
import org.joda.time.DateTimeConstants;
import org.joda.time.DateTimeField;
import org.joda.time.DateTimeUtils;
import org.joda.time.DateTimeZone;
import org.joda.time.DurationField;
import org.joda.time.DurationFieldType;

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
        SKIP = 1 * MILLIS_PER_DAY;
        return new TestSuite(TestDays360Chronology.class);
    }

    public TestDays360Chronology(String name) {
        super(name);
    }

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

    private static final int MILLIS_PER_DAY = DateTimeConstants.MILLIS_PER_DAY;
    private static final DateTimeZone PARIS = DateTimeZone.forID("Europe/Paris");
    private static final DateTimeZone LONDON = DateTimeZone.forID("Europe/London");
    private static final DateTimeZone TOKYO = DateTimeZone.forID("Asia/Tokyo");
    private static final Chronology DAYS360_UTC = Days360Chronology.getInstanceUTC();
    private static final Chronology JULIAN_UTC = JulianChronology.getInstanceUTC();
    private static final Chronology ISO_UTC = ISOChronology.getInstanceUTC();

    private static long SKIP = 1 * MILLIS_PER_DAY;

    long y2002days = 365 + 365 + 366 + 365 + 365 + 365 + 366 + 365 + 365 + 365 +
            366 + 365 + 365 + 365 + 366 + 365 + 365 + 365 + 366 + 365 +
            365 + 365 + 366 + 365 + 365 + 365 + 366 + 365 + 365 + 365 +
            366 + 365;
    // 2002-06-09
    private long TEST_TIME_NOW =
            (y2002days + 31L + 28L + 31L + 30L + 31L + 9L -1L) * MILLIS_PER_DAY;

    private DateTimeZone originalDateTimeZone = null;
    private TimeZone originalTimeZone = null;
    private Locale originalLocale = null;

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
        assertEquals(false, days360.weeks().isPrecise());
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
        assertEquals(new DateTime(284, 8, 29, 0, 0, 0, 0, JULIAN_UTC), epoch.withChronology(JULIAN_UTC));
    }

    //-----------------------------------------------------------------------
    /**
     * Tests era, year, monthOfYear, dayOfMonth and dayOfWeek.
     */
    public void testCalendar() {
        if (TestAll.FAST) {
            return;
        }
        System.out.println("\nTestDays360Chronology.testCalendar");
        DateTime epoch = new DateTime(1, 1, 1, 0, 0, 0, 0, DAYS360_UTC);
        long millis = epoch.getMillis();
        long end = new DateTime(3000, 1, 1, 0, 0, 0, 0, ISO_UTC).getMillis();
        DateTimeField dayOfWeek = DAYS360_UTC.dayOfWeek();
        DateTimeField dayOfYear = DAYS360_UTC.dayOfYear();
        DateTimeField dayOfMonth = DAYS360_UTC.dayOfMonth();
        DateTimeField monthOfYear = DAYS360_UTC.monthOfYear();
        DateTimeField year = DAYS360_UTC.year();
        DateTimeField yearOfEra = DAYS360_UTC.yearOfEra();
        DateTimeField era = DAYS360_UTC.era();
        int expectedDOW = new DateTime(284, 8, 29, 0, 0, 0, 0, JULIAN_UTC).getDayOfWeek();
        int expectedDOY = 1;
        int expectedDay = 1;
        int expectedMonth = 1;
        int expectedYear = 1;
        while (millis < end) {
            int dowValue = dayOfWeek.get(millis);
            int doyValue = dayOfYear.get(millis);
            int dayValue = dayOfMonth.get(millis);
            int monthValue = monthOfYear.get(millis);
            int yearValue = year.get(millis);
            int yearOfEraValue = yearOfEra.get(millis);
            int monthLen = dayOfMonth.getMaximumValue(millis);
            if (monthValue < 1 || monthValue > 13) {
                fail("Bad month: " + millis);
            }

            // test era
            assertEquals(1, era.get(millis));
            assertEquals("AM", era.getAsText(millis));
            assertEquals("AM", era.getAsShortText(millis));

            // test date
            assertEquals(expectedYear, yearValue);
            assertEquals(expectedYear, yearOfEraValue);
            assertEquals(expectedMonth, monthValue);
            assertEquals(expectedDay, dayValue);
            assertEquals(expectedDOW, dowValue);
            assertEquals(expectedDOY, doyValue);

            // test leap year
            assertEquals(yearValue % 4 == 3, year.isLeap(millis));

            // test month length
            if (monthValue == 13) {
                assertEquals(yearValue % 4 == 3, monthOfYear.isLeap(millis));
                if (yearValue % 4 == 3) {
                    assertEquals(6, monthLen);
                } else {
                    assertEquals(5, monthLen);
                }
            } else {
                assertEquals(30, monthLen);
            }

            // recalculate date
            expectedDOW = (((expectedDOW + 1) - 1) % 7) + 1;
            expectedDay++;
            expectedDOY++;
            if (expectedDay == 31 && expectedMonth < 13) {
                expectedDay = 1;
                expectedMonth++;
            } else if (expectedMonth == 13) {
                if (expectedYear % 4 == 3 && expectedDay == 7) {
                    expectedDay = 1;
                    expectedMonth = 1;
                    expectedYear++;
                    expectedDOY = 1;
                } else if (expectedYear % 4 != 3 && expectedDay == 6) {
                    expectedDay = 1;
                    expectedMonth = 1;
                    expectedYear++;
                    expectedDOY = 1;
                }
            }
            millis += SKIP;
        }
    }

    public void testSampleDate() {
        DateTime dt = new DateTime(2004, 6, 9, 0, 0, 0, 0, ISO_UTC).withChronology(DAYS360_UTC);
        assertEquals(18, dt.getCenturyOfEra());  // TODO confirm
        assertEquals(20, dt.getYearOfCentury());
        assertEquals(1720, dt.getYearOfEra());

        assertEquals(1720, dt.getYear());
        DateTime.Property fld = dt.year();
        assertEquals(false, fld.isLeap());
        assertEquals(0, fld.getLeapAmount());
        assertEquals(DurationFieldType.days(), fld.getLeapDurationField().getType());
        assertEquals(new DateTime(1721, 10, 2, 0, 0, 0, 0, DAYS360_UTC), fld.addToCopy(1));

        assertEquals(10, dt.getMonthOfYear());
        fld = dt.monthOfYear();
        assertEquals(false, fld.isLeap());
        assertEquals(0, fld.getLeapAmount());
        assertEquals(DurationFieldType.days(), fld.getLeapDurationField().getType());
        assertEquals(1, fld.getMinimumValue());
        assertEquals(1, fld.getMinimumValueOverall());
        assertEquals(13, fld.getMaximumValue());
        assertEquals(13, fld.getMaximumValueOverall());
        assertEquals(new DateTime(1721, 1, 2, 0, 0, 0, 0, DAYS360_UTC), fld.addToCopy(4));
        assertEquals(new DateTime(1720, 1, 2, 0, 0, 0, 0, DAYS360_UTC), fld.addWrapFieldToCopy(4));

        assertEquals(2, dt.getDayOfMonth());
        fld = dt.dayOfMonth();
        assertEquals(false, fld.isLeap());
        assertEquals(0, fld.getLeapAmount());
        assertEquals(null, fld.getLeapDurationField());
        assertEquals(1, fld.getMinimumValue());
        assertEquals(1, fld.getMinimumValueOverall());
        assertEquals(30, fld.getMaximumValue());
        assertEquals(30, fld.getMaximumValueOverall());
        assertEquals(new DateTime(1720, 10, 3, 0, 0, 0, 0, DAYS360_UTC), fld.addToCopy(1));

        assertEquals(DateTimeConstants.WEDNESDAY, dt.getDayOfWeek());
        fld = dt.dayOfWeek();
        assertEquals(false, fld.isLeap());
        assertEquals(0, fld.getLeapAmount());
        assertEquals(null, fld.getLeapDurationField());
        assertEquals(1, fld.getMinimumValue());
        assertEquals(1, fld.getMinimumValueOverall());
        assertEquals(7, fld.getMaximumValue());
        assertEquals(7, fld.getMaximumValueOverall());
        assertEquals(new DateTime(1720, 10, 3, 0, 0, 0, 0, DAYS360_UTC), fld.addToCopy(1));

        assertEquals(9 * 30 + 2, dt.getDayOfYear());
        fld = dt.dayOfYear();
        assertEquals(false, fld.isLeap());
        assertEquals(0, fld.getLeapAmount());
        assertEquals(null, fld.getLeapDurationField());
        assertEquals(1, fld.getMinimumValue());
        assertEquals(1, fld.getMinimumValueOverall());
        assertEquals(365, fld.getMaximumValue());
        assertEquals(366, fld.getMaximumValueOverall());
        assertEquals(new DateTime(1720, 10, 3, 0, 0, 0, 0, DAYS360_UTC), fld.addToCopy(1));

        assertEquals(0, dt.getHourOfDay());
        assertEquals(0, dt.getMinuteOfHour());
        assertEquals(0, dt.getSecondOfMinute());
        assertEquals(0, dt.getMillisOfSecond());
    }

    public void testSampleDateWithZone() {
        DateTime dt = new DateTime(2004, 6, 9, 12, 0, 0, 0, PARIS).withChronology(DAYS360_UTC);
        assertEquals(1720, dt.getYear());
        assertEquals(1720, dt.getYearOfEra());
        assertEquals(10, dt.getMonthOfYear());
        assertEquals(2, dt.getDayOfMonth());
        assertEquals(10, dt.getHourOfDay());  // PARIS is UTC+2 in summer (12-2=10)
        assertEquals(0, dt.getMinuteOfHour());
        assertEquals(0, dt.getSecondOfMinute());
        assertEquals(0, dt.getMillisOfSecond());
    }

    public void testDurationYear() {
        // Leap 1723
        DateTime dt20 = new DateTime(1720, 10, 2, 0, 0, 0, 0, DAYS360_UTC);
        DateTime dt21 = new DateTime(1721, 10, 2, 0, 0, 0, 0, DAYS360_UTC);
        DateTime dt22 = new DateTime(1722, 10, 2, 0, 0, 0, 0, DAYS360_UTC);
        DateTime dt23 = new DateTime(1723, 10, 2, 0, 0, 0, 0, DAYS360_UTC);
        DateTime dt24 = new DateTime(1724, 10, 2, 0, 0, 0, 0, DAYS360_UTC);

        DurationField fld = dt20.year().getDurationField();
        assertEquals(DAYS360_UTC.years(), fld);
        assertEquals(1L * 365L * MILLIS_PER_DAY, fld.getMillis(1, dt20.getMillis()));
        assertEquals(2L * 365L * MILLIS_PER_DAY, fld.getMillis(2, dt20.getMillis()));
        assertEquals(3L * 365L * MILLIS_PER_DAY, fld.getMillis(3, dt20.getMillis()));
        assertEquals((4L * 365L + 1L) * MILLIS_PER_DAY, fld.getMillis(4, dt20.getMillis()));

        assertEquals(((4L * 365L + 1L) * MILLIS_PER_DAY) / 4, fld.getMillis(1));
        assertEquals(((4L * 365L + 1L) * MILLIS_PER_DAY) / 2, fld.getMillis(2));

        assertEquals(1L * 365L * MILLIS_PER_DAY, fld.getMillis(1L, dt20.getMillis()));
        assertEquals(2L * 365L * MILLIS_PER_DAY, fld.getMillis(2L, dt20.getMillis()));
        assertEquals(3L * 365L * MILLIS_PER_DAY, fld.getMillis(3L, dt20.getMillis()));
        assertEquals((4L * 365L + 1L) * MILLIS_PER_DAY, fld.getMillis(4L, dt20.getMillis()));

        assertEquals(((4L * 365L + 1L) * MILLIS_PER_DAY) / 4, fld.getMillis(1L));
        assertEquals(((4L * 365L + 1L) * MILLIS_PER_DAY) / 2, fld.getMillis(2L));

        assertEquals(((4L * 365L + 1L) * MILLIS_PER_DAY) / 4, fld.getUnitMillis());

        assertEquals(0, fld.getValue(1L * 365L * MILLIS_PER_DAY - 1L, dt20.getMillis()));
        assertEquals(1, fld.getValue(1L * 365L * MILLIS_PER_DAY, dt20.getMillis()));
        assertEquals(1, fld.getValue(1L * 365L * MILLIS_PER_DAY + 1L, dt20.getMillis()));
        assertEquals(1, fld.getValue(2L * 365L * MILLIS_PER_DAY - 1L, dt20.getMillis()));
        assertEquals(2, fld.getValue(2L * 365L * MILLIS_PER_DAY, dt20.getMillis()));
        assertEquals(2, fld.getValue(2L * 365L * MILLIS_PER_DAY + 1L, dt20.getMillis()));
        assertEquals(2, fld.getValue(3L * 365L * MILLIS_PER_DAY - 1L, dt20.getMillis()));
        assertEquals(3, fld.getValue(3L * 365L * MILLIS_PER_DAY, dt20.getMillis()));
        assertEquals(3, fld.getValue(3L * 365L * MILLIS_PER_DAY + 1L, dt20.getMillis()));
        assertEquals(3, fld.getValue((4L * 365L + 1L) * MILLIS_PER_DAY - 1L, dt20.getMillis()));
        assertEquals(4, fld.getValue((4L * 365L + 1L) * MILLIS_PER_DAY, dt20.getMillis()));
        assertEquals(4, fld.getValue((4L * 365L + 1L) * MILLIS_PER_DAY + 1L, dt20.getMillis()));

        assertEquals(dt21.getMillis(), fld.add(dt20.getMillis(), 1));
        assertEquals(dt22.getMillis(), fld.add(dt20.getMillis(), 2));
        assertEquals(dt23.getMillis(), fld.add(dt20.getMillis(), 3));
        assertEquals(dt24.getMillis(), fld.add(dt20.getMillis(), 4));

        assertEquals(dt21.getMillis(), fld.add(dt20.getMillis(), 1L));
        assertEquals(dt22.getMillis(), fld.add(dt20.getMillis(), 2L));
        assertEquals(dt23.getMillis(), fld.add(dt20.getMillis(), 3L));
        assertEquals(dt24.getMillis(), fld.add(dt20.getMillis(), 4L));
    }

    public void testDurationMonth() {
        // Leap 1723
        DateTime dt11 = new DateTime(1723, 11, 2, 0, 0, 0, 0, DAYS360_UTC);
        DateTime dt12 = new DateTime(1723, 12, 2, 0, 0, 0, 0, DAYS360_UTC);
        DateTime dt13 = new DateTime(1723, 13, 2, 0, 0, 0, 0, DAYS360_UTC);
        DateTime dt01 = new DateTime(1724, 1, 2, 0, 0, 0, 0, DAYS360_UTC);

        DurationField fld = dt11.monthOfYear().getDurationField();
        assertEquals(DAYS360_UTC.months(), fld);
        assertEquals(1L * 30L * MILLIS_PER_DAY, fld.getMillis(1, dt11.getMillis()));
        assertEquals(2L * 30L * MILLIS_PER_DAY, fld.getMillis(2, dt11.getMillis()));
        assertEquals((2L * 30L + 6L) * MILLIS_PER_DAY, fld.getMillis(3, dt11.getMillis()));
        assertEquals((3L * 30L + 6L) * MILLIS_PER_DAY, fld.getMillis(4, dt11.getMillis()));

        assertEquals(1L * 30L * MILLIS_PER_DAY, fld.getMillis(1));
        assertEquals(2L * 30L * MILLIS_PER_DAY, fld.getMillis(2));
        assertEquals(13L * 30L * MILLIS_PER_DAY, fld.getMillis(13));

        assertEquals(1L * 30L * MILLIS_PER_DAY, fld.getMillis(1L, dt11.getMillis()));
        assertEquals(2L * 30L * MILLIS_PER_DAY, fld.getMillis(2L, dt11.getMillis()));
        assertEquals((2L * 30L + 6L) * MILLIS_PER_DAY, fld.getMillis(3L, dt11.getMillis()));
        assertEquals((3L * 30L + 6L) * MILLIS_PER_DAY, fld.getMillis(4L, dt11.getMillis()));

        assertEquals(1L * 30L * MILLIS_PER_DAY, fld.getMillis(1L));
        assertEquals(2L * 30L * MILLIS_PER_DAY, fld.getMillis(2L));
        assertEquals(13L * 30L * MILLIS_PER_DAY, fld.getMillis(13L));

        assertEquals(0, fld.getValue(1L * 30L * MILLIS_PER_DAY - 1L, dt11.getMillis()));
        assertEquals(1, fld.getValue(1L * 30L * MILLIS_PER_DAY, dt11.getMillis()));
        assertEquals(1, fld.getValue(1L * 30L * MILLIS_PER_DAY + 1L, dt11.getMillis()));
        assertEquals(1, fld.getValue(2L * 30L * MILLIS_PER_DAY - 1L, dt11.getMillis()));
        assertEquals(2, fld.getValue(2L * 30L * MILLIS_PER_DAY, dt11.getMillis()));
        assertEquals(2, fld.getValue(2L * 30L * MILLIS_PER_DAY + 1L, dt11.getMillis()));
        assertEquals(2, fld.getValue((2L * 30L + 6L) * MILLIS_PER_DAY - 1L, dt11.getMillis()));
        assertEquals(3, fld.getValue((2L * 30L + 6L) * MILLIS_PER_DAY, dt11.getMillis()));
        assertEquals(3, fld.getValue((2L * 30L + 6L) * MILLIS_PER_DAY + 1L, dt11.getMillis()));
        assertEquals(3, fld.getValue((3L * 30L + 6L) * MILLIS_PER_DAY - 1L, dt11.getMillis()));
        assertEquals(4, fld.getValue((3L * 30L + 6L) * MILLIS_PER_DAY, dt11.getMillis()));
        assertEquals(4, fld.getValue((3L * 30L + 6L) * MILLIS_PER_DAY + 1L, dt11.getMillis()));

        assertEquals(dt12.getMillis(), fld.add(dt11.getMillis(), 1));
        assertEquals(dt13.getMillis(), fld.add(dt11.getMillis(), 2));
        assertEquals(dt01.getMillis(), fld.add(dt11.getMillis(), 3));

        assertEquals(dt12.getMillis(), fld.add(dt11.getMillis(), 1L));
        assertEquals(dt13.getMillis(), fld.add(dt11.getMillis(), 2L));
        assertEquals(dt01.getMillis(), fld.add(dt11.getMillis(), 3L));
    }

    public void testNotLeapOnLeapYear() {
        Chronology chrono = Days360Chronology.getInstance();
        DateTime dt = new DateTime(2000, 1, 1, 0, 0, chrono);
        assertEquals(false, dt.year().isLeap());
        assertEquals(false, dt.monthOfYear().isLeap());
        assertEquals(false, dt.dayOfMonth().isLeap());
        assertEquals(false, dt.dayOfYear().isLeap());
    }

}
