package io.leaderli.litool.core.util;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Date;

class DateUtilTest {

    @Test
    void format() {

        String yyyy = DateUtil.format("yyyyMMdd");
        String yyyy2 = DateUtil.format("yyyyMMdd", LocalDate.now());
        String yyyy3 = DateUtil.format("yyyyMMdd", new Date());
        String yyyy4 = DateUtil.format("yyyyMMdd", LocalDateTime.now());

        Assertions.assertEquals(yyyy, yyyy2);
        Assertions.assertEquals(yyyy, yyyy3);
        Assertions.assertEquals(yyyy, yyyy4);
    }

    @Test
    void parser() {
        System.out.println(DateUtil.parser2LocalDate("yyyyMMdd HH", "19910101 11"));
        System.out.println(DateUtil.parser2LocalDate("yyyyMMdd HHmmss", "19910102 112235"));
        System.out.println(DateUtil.parser2LocalDateTime("yyyyMMdd HHmmss", "19910103 112233"));
        System.out.println(DateUtil.parser2LocalDateTime("HHmmss", "112236"));
        System.out.println(DateUtil.parser2LocalDateTime("yyyyMMdd", "19910104"));
        System.out.println(DateUtil.parser2Date("yyyyMMdd", "19910103"));
        System.out.println(DateUtil.parser2Date("yyyyMMdd HHmmss", "19910103 112235"));
    }
}
