/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package recutil.timeclioption;

import java.util.ArrayList;
import java.util.List;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.OptionGroup;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.apache.commons.lang3.Range;
import org.apache.commons.lang3.builder.ReflectionToStringBuilder;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;
import org.slf4j.Logger;
import recutil.loggerconfigurator.LoggerConfigurator;

/**
 *
 * @author normal
 */
public class TimeCliOptionTest {

    private static final Logger LOG = LoggerConfigurator.getCallerLogger();

    public TimeCliOptionTest() {
    }

    @BeforeClass
    public static void setUpClass() {
    }

    @AfterClass
    public static void tearDownClass() {
    }

    @Before
    public void setUp() {
    }

    @After
    public void tearDown() {
    }
    public static final Range<Long> RANGE_LONG = Range.between(0L, Long.MAX_VALUE);

    private boolean optionCheck(Option target, final TimeCliOption.OPTION_NAME optionName, final String desc) {
        if (!target.getOpt().equals(optionName.getShortOption())) {
            return false;
        }
        if (!target.getLongOpt().equals(optionName.getLongOption())) {
            return false;
        }
        if (!target.isRequired()) {
            return false;
        }
        if (!target.getDescription().equals(desc)) {
            return false;
        }
        if (!target.hasArg()) {
            return false;
        }
        if (!(target.getType() == Long.class)) {
            return false;
        }
        return true;
    }

    private boolean CheckSecondOption(Option target) {
        return this.optionCheck(target, TimeCliOption.OPTION_NAME.SECOND, DESC_SECOND);
    }

    private boolean CheckMinuteOption(Option target) {
        return this.optionCheck(target, TimeCliOption.OPTION_NAME.MINUTE, DESC_MINUTE);
    }

    private boolean CheckHourOption(Option target) {
        return this.optionCheck(target, TimeCliOption.OPTION_NAME.HOUR, DESC_HOUR);
    }

    private static final String DESC_SECOND = "test_s";
    private static final String DESC_MINUTE = "test_m";
    private static final String DESC_HOUR = "test_h";

    private TimeCliOption getTestClass() {
        return new TimeCliOption(DESC_SECOND, DESC_MINUTE, DESC_HOUR);
    }

    /**
     * Test of getSecondRangeOption method, of class TimeCliOption.
     */
    @Test
    public void testGetSecondRangeOption() {
        LOG.info("getSecondRangeOption");
        TimeCliOption instance = getTestClass();
        Option result = instance.getSecondRangeOption();
        assertEquals(true, this.CheckSecondOption(result));
    }

    /**
     * Test of getMinuteRangeOption method, of class TimeCliOption.
     */
    @Test
    public void testGetMinuteRangeOption() {
        LOG.info("getMinuteRangeOption");
        TimeCliOption instance = getTestClass();
        Option result = instance.getMinuteRangeOption();
        assertEquals(true, this.CheckMinuteOption(result));
    }

    /**
     * Test of getHourRangeOption method, of class TimeCliOption.
     */
    @Test
    public void testGetHourRangeOption() {
        LOG.info("getHourRangeOption");
        TimeCliOption instance = getTestClass();
        Option result = instance.getHourRangeOption();
        assertEquals(true, this.CheckHourOption(result));
    }

    /**
     * Test of getTimeOptionGroup method, of class TimeCliOption.
     */
    @Test
    public void testGetTimeOptionGroup() {
        LOG.info("getTimeOptionGroup");
        boolean required = true;
        TimeCliOption instance = getTestClass();
        OptionGroup result = instance.getTimeOptionGroup(required);
        assertEquals(true, result.isRequired());
        List<Option> opts = new ArrayList<>(result.getOptions());
        if (opts.size() != 3) {
            fail("オプション数相違。");
        }
        boolean s = true, m = true, h = true;
        for (Option o : opts) {
            s = s || this.CheckSecondOption(o);
            m = m || this.CheckMinuteOption(o);
            h = h || this.CheckHourOption(o);
        }
        assertTrue(s && m && h);
    }

    private long parseTime(TimeCliOption instance, String[] args) throws ParseException, TimeParseException {
        LOG.info("引数 = {}", ReflectionToStringBuilder.toString(args));
        Options opts = new Options();
        opts.addOptionGroup(instance.getTimeOptionGroup(true));
        CommandLineParser parser = new DefaultParser();
        HelpFormatter help = new HelpFormatter();
        CommandLine cl;
        cl = parser.parse(opts, args);
        return instance.getValueBySecond(cl);
    }

    private String[] getArg_second(long val) {
        String[] args = {"-s", Long.toString(val)};
        return args;
    }

    private String[] getSecond_minus1() {
        return getArg_second(-1);
    }

    private String[] getSecond_zero() {
        return getArg_second(0);
    }

    private String[] getSecond_one() {
        return getArg_second(1);
    }

    private String[] getSecond_max() {
        return getArg_second(TimeCliOption.MAX_SECOND);
    }

    /**
     * Test of getValueBySecond method, of class TimeCliOption.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testGetValueBySecond_minus1() throws Exception {
        LOG.info("testGetValueBySecond_minus1");
        try {
            TimeCliOption instance = getTestClass();
            long expResult = 0L;
            long result = this.parseTime(instance, this.getSecond_minus1());
            assertEquals(expResult, result);
        } catch (Exception ex) {
            LOG.warn("エラー。", ex);
            throw ex;
        }
    }

    /**
     * Test of getValueBySecond method, of class TimeCliOption.
     */
    @Test
    public void testGetValueBySecond_zero() throws Exception {
        LOG.info("getValueBySecond_zero");
        try {
            TimeCliOption instance = getTestClass();
            long expResult = 0L;
            long result = this.parseTime(instance, this.getSecond_zero());
            assertEquals(expResult, result);
        } catch (Exception ex) {
            LOG.warn("エラー。", ex);
            throw ex;
        }
    }

    /**
     * Test of getValueBySecond method, of class TimeCliOption.
     */
    @Test
    public void testGetValueBySecond_one() throws Exception {
        LOG.info("testGetValueBySecond_one");
        try {
            TimeCliOption instance = getTestClass();
            long expResult = 1L;
            long result = this.parseTime(instance, this.getSecond_one());
            assertEquals(expResult, result);
        } catch (Exception ex) {
            LOG.warn("エラー。", ex);
            throw ex;
        }
    }

    /**
     * Test of getValueBySecond method, of class TimeCliOption.
     */
    @Test
    public void testGetValueBySecond_max() throws Exception {
        LOG.info("getValueBySecond_max");
        try {
            TimeCliOption instance = getTestClass();
            long result = this.parseTime(instance, this.getSecond_max());
            assertTrue(RANGE_LONG.contains(result));

        } catch (Exception ex) {
            LOG.warn("エラー。", ex);
            throw ex;
        }
    }

    private String[] getArg_minute(long val) {
        String[] args = {"-m", Long.toString(val)};
        return args;
    }

    private String[] getMinute_minus1() {
        return getArg_minute(-1);
    }

    private String[] getMinute_zero() {
        return getArg_minute(0);
    }

    private String[] getMinute_one() {
        return getArg_minute(1);
    }

    private String[] getMinute_max() {
        return getArg_minute(TimeCliOption.MAX_MINUTE);
    }

    private String[] getMinute_maxPlus1() {
        return getArg_minute(TimeCliOption.MAX_MINUTE + 1);
    }

    /**
     * Test of getValueBySecond method, of class TimeCliOption.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testGetValueByMinute_minus1() throws Exception {
        LOG.info("testGetValueByMinute_minus1");
        try {
            TimeCliOption instance = getTestClass();
            long expResult = 0L;
            long result = this.parseTime(instance, this.getMinute_minus1());
            assertEquals(expResult, result);
        } catch (Exception ex) {
            LOG.warn("エラー。", ex);
            throw ex;
        }
    }

    /**
     * Test of getValueBySecond method, of class TimeCliOption.
     */
    @Test
    public void testGetValueByMinute_zero() throws Exception {
        LOG.info("testGetValueByMinute_zero");
        try {
            TimeCliOption instance = getTestClass();
            long expResult = 0L;
            long result = this.parseTime(instance, this.getMinute_zero());
            assertEquals(expResult, result);
        } catch (Exception ex) {
            LOG.warn("エラー。", ex);
            throw ex;
        }
    }

    /**
     * Test of getValueBySecond method, of class TimeCliOption.
     */
    @Test
    public void testGetValueByMinute_one() throws Exception {
        LOG.info("testGetValueByMinute_one");
        try {
            TimeCliOption instance = getTestClass();
            long expResult = 60L;
            long result = this.parseTime(instance, this.getMinute_one());
            assertEquals(expResult, result);
        } catch (Exception ex) {
            LOG.warn("エラー。", ex);
            throw ex;
        }
    }

    /**
     * Test of getValueBySecond method, of class TimeCliOption.
     */
    @Test
    public void testGetValueByMinute_max() throws Exception {
        LOG.info("testGetValueByMinute_max");
        try {
            TimeCliOption instance = getTestClass();
            long result = this.parseTime(instance, this.getMinute_max());
            assertTrue(RANGE_LONG.contains(result));

        } catch (Exception ex) {
            LOG.warn("エラー。", ex);
            throw ex;
        }
    }

    /**
     * Test of getValueBySecond method, of class TimeCliOption.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testGetValueByMinute_maxPlus1() throws Exception {
        LOG.info("testGetValueByMinute_maxPlus1");
        try {
            TimeCliOption instance = getTestClass();
            long result = this.parseTime(instance, this.getMinute_maxPlus1());
            assertTrue(RANGE_LONG.contains(result));
        } catch (Exception ex) {
            LOG.warn("エラー。", ex);
            throw ex;
        }
    }

    private String[] getArg_hour(long val) {
        String[] args = {"-h", Long.toString(val)};
        return args;
    }

    private String[] getHour_minus1() {
        return getArg_hour(-1);
    }

    private String[] getHour_zero() {
        return getArg_hour(0);
    }

    private String[] getHour_one() {
        return getArg_hour(1);
    }

    private String[] getHour_max() {
        return getArg_hour(TimeCliOption.MAX_HOUR);
    }

    private String[] getHour_maxPlus1() {
        return getArg_hour(TimeCliOption.MAX_HOUR + 1);
    }

    /**
     * Test of getValueBySecond method, of class TimeCliOption.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testGetValueByHour_minus1() throws Exception {
        LOG.info("testGetValueByHour_minus1");
        try {
            TimeCliOption instance = getTestClass();
            long expResult = 0L;
            long result = this.parseTime(instance, this.getHour_minus1());
            assertEquals(expResult, result);
        } catch (Exception ex) {
            LOG.warn("エラー。", ex);
            throw ex;
        }
    }

    /**
     * Test of getValueBySecond method, of class TimeCliOption.
     */
    @Test
    public void testGetValueByHour_zero() throws Exception {
        LOG.info("testGetValueByHour_zero");
        try {
            TimeCliOption instance = getTestClass();
            long expResult = 0L;
            long result = this.parseTime(instance, this.getHour_zero());
            assertEquals(expResult, result);
        } catch (Exception ex) {
            LOG.warn("エラー。", ex);
            throw ex;
        }
    }

    /**
     * Test of getValueBySecond method, of class TimeCliOption.
     */
    @Test
    public void testGetValueByHour_one() throws Exception {
        LOG.info("testGetValueByHour_one");
        try {
            TimeCliOption instance = getTestClass();
            long expResult = 60 * 60;
            long result = this.parseTime(instance, this.getHour_one());
            assertEquals(expResult, result);
        } catch (Exception ex) {
            LOG.warn("エラー。", ex);
            throw ex;
        }
    }

    /**
     * Test of getValueBySecond method, of class TimeCliOption.
     */
    @Test
    public void testGetValueByHour_max() throws Exception {
        LOG.info("testGetValueByHour_max");
        try {
            TimeCliOption instance = getTestClass();
            long result = this.parseTime(instance, this.getHour_max());
            assertTrue(RANGE_LONG.contains(result));
        } catch (Exception ex) {
            LOG.warn("エラー。", ex);
            throw ex;
        }
    }

    /**
     * Test of getValueBySecond method, of class TimeCliOption.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testGetValueByHour_maxPlus1() throws Exception {
        LOG.info("testGetValueByHour_maxPlus1");
        try {
            TimeCliOption instance = getTestClass();
            long result = this.parseTime(instance, this.getHour_maxPlus1());
            assertTrue(RANGE_LONG.contains(result));
        } catch (Exception ex) {
            LOG.warn("エラー。", ex);
            throw ex;
        }
    }

    /**
     * Test of getValueBySecond method, of class TimeCliOption.
     */
    @Test(expected = TimeParseException.class)
    public void testGetValueByhour_wrong() throws Exception {
        LOG.info("testGetVatestGetValueByhour_wronglueByMinute_max");
        try {
            TimeCliOption instance = getTestClass();
            long result = this.parseTime(instance, new String[]{"-h", "xx"});
            assertTrue(RANGE_LONG.contains(result));
        } catch (Exception ex) {
            LOG.warn("エラー。", ex);
            throw ex;
        }
    }

    /**
     * Test of getValueBySecond method, of class TimeCliOption.
     */
    @Test(expected = TimeParseException.class)
    public void testGetValueByMinute_wrong() throws Exception {
        LOG.info("testGetValueByMinute_wrong");
        try {
            TimeCliOption instance = getTestClass();
            long result = this.parseTime(instance, new String[]{"-m", "xx"});
            assertTrue(RANGE_LONG.contains(result));
        } catch (Exception ex) {
            LOG.warn("エラー。", ex);
            throw ex;
        }
    }

    /**
     * Test of getValueBySecond method, of class TimeCliOption.
     */
    @Test(expected = TimeParseException.class)
    public void testGetValueBysecond_wrong() throws Exception {
        LOG.info("testGetValueBysecond_wrong");
        try {
            TimeCliOption instance = getTestClass();
            long result = this.parseTime(instance, new String[]{"-s", "xx"});
            assertTrue(RANGE_LONG.contains(result));
        } catch (Exception ex) {
            LOG.warn("エラー。", ex);
            throw ex;
        }
    }
}
