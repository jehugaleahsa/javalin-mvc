package com.truncon.javalin.mvc.test.controllers;

import com.truncon.javalin.mvc.api.*;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.time.*;
import java.util.Date;
import java.util.UUID;

@Controller
public class BoxedArrayParameterController {
    public static final String BOOLEAN_ROUTE = "/api/boxed-arrays/boolean";
    @HttpGet(route = BOOLEAN_ROUTE)
    public ActionResult getBoolean(@Named("value") Boolean[] values) {
        return new JsonResult(values);
    }

    public static final String INTEGER_ROUTE = "/api/boxed-arrays/int";
    @HttpGet(route = INTEGER_ROUTE)
    public ActionResult getInt(@Named("value") Integer[] values) {
        return new JsonResult(values);
    }

    public static final String DOUBLE_ROUTE = "/api/boxed-arrays/double";
    @HttpGet(route = DOUBLE_ROUTE)
    public ActionResult getDouble(@Named("value") Double[] values) {
        return new JsonResult(values);
    }

    public static final String BYTE_ROUTE = "/api/boxed-arrays/byte";
    @HttpGet(route = BYTE_ROUTE)
    public ActionResult getByte(@Named("value") Byte[] values) {
        return new JsonResult(values);
    }

    public static final String SHORT_ROUTE = "/api/boxed-arrays/short";
    @HttpGet(route = SHORT_ROUTE)
    public ActionResult getShort(@Named("value") Short[] values) {
        return new JsonResult(values);
    }

    public static final String FLOAT_ROUTE = "/api/boxed-arrays/float";
    @HttpGet(route = FLOAT_ROUTE)
    public ActionResult getFloat(@Named("value") Float[] values) {
        return new JsonResult(values);
    }

    public static final String CHAR_ROUTE = "/api/boxed-arrays/char";
    @HttpGet(route = CHAR_ROUTE)
    public ActionResult getChar(@Named("value") Character[] values) {
        return new JsonResult(values);
    }

    public static final String LONG_ROUTE = "/api/boxed-arrays/long";
    @HttpGet(route = LONG_ROUTE)
    public ActionResult getChar(@Named("value") Long[] values) {
        return new JsonResult(values);
    }

    public static final String DATE_ROUTE = "/api/boxed-arrays/date";
    @HttpGet(route = DATE_ROUTE)
    public ActionResult getDate(@Named("value") Date[] values) {
        return new JsonResult(values);
    }

    public static final String INSTANT_ROUTE = "/api/boxed-arrays/instant";
    @HttpGet(route = INSTANT_ROUTE)
    public ActionResult getInstant(@Named("value") Instant[] values) {
        return new JsonResult(values);
    }

    public static final String ZONED_DATETIME_ROUTE = "/api/boxed-arrays/zoned-date-time";
    @HttpGet(route = ZONED_DATETIME_ROUTE)
    public ActionResult getZonedDateTime(@Named("value") ZonedDateTime[] values) {
        return new JsonResult(values);
    }

    public static final String OFFSET_DATETIME_ROUTE = "/api/boxed-arrays/offset-date-time";
    @HttpGet(route = OFFSET_DATETIME_ROUTE)
    public ActionResult getOffsetDateTime(@Named("value") OffsetDateTime[] values) {
        return new JsonResult(values);
    }

    public static final String LOCAL_DATETIME_ROUTE = "/api/boxed-arrays/local-date-time";
    @HttpGet(route = LOCAL_DATETIME_ROUTE)
    public ActionResult getLocalDateTime(@Named("value") LocalDateTime[] values) {
        return new JsonResult(values);
    }

    public static final String LOCAL_DATE_ROUTE = "/api/boxed-arrays/local-date";
    @HttpGet(route = LOCAL_DATE_ROUTE)
    public ActionResult getLocalDate(@Named("value") LocalDate[] values) {
        return new JsonResult(values);
    }

    public static final String YEAR_MONTH_ROUTE = "/api/boxed-arrays/year-month";
    @HttpGet(route = YEAR_MONTH_ROUTE)
    public ActionResult getYearMonth(@Named("value") YearMonth[] values) {
        return new JsonResult(values);
    }

    public static final String YEAR_ROUTE = "/api/boxed-arrays/year";
    @HttpGet(route = YEAR_ROUTE)
    public ActionResult getYear(@Named("value") Year[] values) {
        return new JsonResult(values);
    }

    public static final String BIG_INTEGER_ROUTE = "/api/boxed-arrays/big-integer";
    @HttpGet(route = BIG_INTEGER_ROUTE)
    public ActionResult getBigInteger(@Named("value") BigInteger[] values) {
        return new JsonResult(values);
    }

    public static final String BIG_DECIMAL_ROUTE = "/api/boxed-arrays/big-decimal";
    @HttpGet(route = BIG_DECIMAL_ROUTE)
    public ActionResult getBigDecimal(@Named("value") BigDecimal[] values) {
        return new JsonResult(values);
    }

    public static final String UUID_ROUTE = "/api/boxed-arrays/uuid";
    @HttpGet(route = UUID_ROUTE)
    public ActionResult getBigDecimal(@Named("value") UUID[] values) {
        return new JsonResult(values);
    }
}
