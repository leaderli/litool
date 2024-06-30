package io.leaderli.litool.core.util;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.Calendar;
import java.util.Date;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class DateUtilTest {

    @Test
    void format() {

        String yyyy = DateUtil.now("yyyyMMdd");
        String yyyy3 = DateUtil.format("yyyyMMdd", new Date());
        String yyyy4 = DateUtil.format("yyyyMMdd", LocalDateTime.now());

        Assertions.assertEquals(yyyy, yyyy3);
        Assertions.assertEquals(yyyy, yyyy4);

        Assertions.assertNotNull(DateUtil.now("yyyyMMdd"));
        Assertions.assertNotNull(DateUtil.now("HHMMss"));
        Assertions.assertNotNull(DateUtil.now("yyyyMMddHHMMss"));
        Assertions.assertNotNull(DateUtil.now("yyyyMMddHHMMssSSS"));

        Assertions.assertNotNull(DateUtil.format("hhMMss", new Date()));
        Assertions.assertNotNull(DateUtil.format("yyyyMMdd", new Date()));
        Assertions.assertNotNull(DateUtil.format("yyyyMMddHHmmss", new Date()));
        Assertions.assertNotNull(DateUtil.format("yyyyMMddHHmmssSSS", new Date()));

        Assertions.assertNotNull(DateUtil.format("hhMMss", LocalDateTime.now()));
        Assertions.assertNotNull(DateUtil.format("yyyyMMdd", LocalDateTime.now()));
        Assertions.assertNotNull(DateUtil.format("yyyyMMddHHmmss", LocalDateTime.now()));
        Assertions.assertNotNull(DateUtil.format("yyyyMMddHHmmssSSS", LocalDateTime.now()));
    }

    @Test
    void parse() {
        Assertions.assertNotNull(DateUtil.parse("19910103", "yyyyMMdd"));
        Assertions.assertNotNull(DateUtil.parse("19910103 112235", "yyyyMMdd HHmmss"));
        Assertions.assertEquals("1991/01/03", DateUtil.parse("19910103", "yyyyMMdd", "yyyy/MM/dd"));
        Assertions.assertEquals("19910103", DateUtil.parse("19910103 112235", "yyyyMMdd HHmmss", "yyyyMMdd"));
        Assertions.assertThrows(RuntimeException.class, () -> DateUtil.parse("1991/01/03 112235", "yyyyMMdd HHmmss", "yyyyMMdd"));
        Assertions.assertThrows(RuntimeException.class, () -> DateUtil.parse("1991/01/03 112235", new SimpleDateFormat("yyyyMMdd")));
        Assertions.assertThrows(RuntimeException.class, () -> DateUtil.parse("1991/01/03 112235", "yyyyMMdd"));
        Assertions.assertTrue(DateUtil.between("yyyyMMdd", "1990101", "20990101"));
        Assertions.assertTrue(DateUtil.between("yyyyMMdd", "20240101", "20240101", "20240103"));
        Assertions.assertTrue(DateUtil.between("yyyyMMdd", "20240102", "20240101", "20240103"));
        Assertions.assertFalse(DateUtil.between("yyyyMMdd", "20240103", "20240101", "20240103"));
        Assertions.assertFalse(DateUtil.between("yyyyMMdd", "20231231", "20240101", "20240103"));
    }


    @Test
    void between() {

        String format = "yyyy-MM-dd HH:mm:ss";
        String now = "2022-01-01 00:00:00";
        String before = "2021-01-01 00:00:00";
        String after = "2121-12-31 23:59:59";

        assertTrue(DateUtil.between(format, now, before, after));
        format = "HHmm";
        now = "0001";
        before = "0000";
        after = "2359";

        assertTrue(DateUtil.between(format, now, before, after));
        format = "HHmm";
        now = "0759";
        before = "0800";
        after = "2000";

        assertFalse(DateUtil.between(format, now, before, after));
        now = "2001";
        assertFalse(DateUtil.between(format, now, before, after));

        format = "MM";
        before = "01";
        now = "02";
        after = "12";

        assertTrue(DateUtil.between(format, now, before, after));
        format = "HH";
        before = "01";
        now = "02";
        after = "23";

        assertTrue(DateUtil.between(format, now, before, after));
        format = "HH";
        before = "08";
        now = "07";
        after = "20";

        assertFalse(DateUtil.between(format, now, before, after));
        now = "20";
        assertFalse(DateUtil.between(format, now, before, after));
    }

    @Test
    void calc() {

        Assertions.assertEquals("20010102", DateUtil.calc("20010101", "yyyyMMdd", Calendar.DAY_OF_YEAR, 1));
        Assertions.assertEquals("20001231", DateUtil.calc("20010101", "yyyyMMdd", Calendar.DAY_OF_YEAR, -1));
    }
}
