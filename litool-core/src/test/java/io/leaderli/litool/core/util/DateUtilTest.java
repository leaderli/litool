package io.leaderli.litool.core.util;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.Date;

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
    void parser() {
        Assertions.assertNotNull(DateUtil.parse("19910103", "yyyyMMdd"));
        Assertions.assertNotNull(DateUtil.parse("19910103 112235", "yyyyMMdd HHmmss"));
        Assertions.assertEquals("1991/01/03", DateUtil.parse("19910103", "yyyyMMdd", "yyyy/MM/dd"));
        Assertions.assertEquals("19910103", DateUtil.parse("19910103 112235", "yyyyMMdd HHmmss", "yyyyMMdd"));
    }
}
