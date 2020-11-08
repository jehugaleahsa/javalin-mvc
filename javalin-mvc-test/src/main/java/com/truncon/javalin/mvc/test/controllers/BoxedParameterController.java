package com.truncon.javalin.mvc.test.controllers;

import com.truncon.javalin.mvc.api.ActionResult;
import com.truncon.javalin.mvc.api.ContentResult;
import com.truncon.javalin.mvc.api.Controller;
import com.truncon.javalin.mvc.api.HttpGet;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.*;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.Objects;
import java.util.TimeZone;
import java.util.UUID;

@Controller
public class BoxedParameterController {
    public static final String STRING_ROUTE = "/api/primitives/string/:value";
    @HttpGet(route = STRING_ROUTE)
    public ActionResult getString(String value) {
        return new ContentResult(value);
    }

    public static final String BOOLEAN_ROUTE = "/api/boxed/boolean/:value";
    @HttpGet(route = BOOLEAN_ROUTE)
    public ActionResult getBoolean(Boolean value) {
        return new ContentResult(Objects.toString(value));
    }

    public static final String INTEGER_ROUTE = "/api/boxed/int/:value";
    @HttpGet(route = INTEGER_ROUTE)
    public ActionResult getInt(Integer value) {
        return new ContentResult(Objects.toString(value));
    }

    public static final String DOUBLE_ROUTE = "/api/boxed/double/:value";
    @HttpGet(route = DOUBLE_ROUTE)
    public ActionResult getDouble(Double value) {
        return new ContentResult(Objects.toString(value));
    }

    public static final String BYTE_ROUTE = "/api/boxed/byte/:value";
    @HttpGet(route = BYTE_ROUTE)
    public ActionResult getString(Byte value) {
        return new ContentResult(Objects.toString(value));
    }

    public static final String SHORT_ROUTE = "/api/boxed/short/:value";
    @HttpGet(route = SHORT_ROUTE)
    public ActionResult getShort(Short value) {
        return new ContentResult(Objects.toString(value));
    }

    public static final String FLOAT_ROUTE = "/api/boxed/float/:value";
    @HttpGet(route = FLOAT_ROUTE)
    public ActionResult getFloat(Float value) {
        return new ContentResult(Objects.toString(value));
    }

    public static final String CHAR_ROUTE = "/api/boxed/char/:value";
    @HttpGet(route = CHAR_ROUTE)
    public ActionResult getChar(Character value) {
        return new ContentResult(Objects.toString(value));
    }

    public static final String LONG_ROUTE = "/api/boxed/long/:value";
    @HttpGet(route = LONG_ROUTE)
    public ActionResult getChar(Long value) {
        return new ContentResult(Objects.toString(value));
    }

    public static final String DATE_ROUTE = "/api/boxed/date/:value";
    public static final DateFormat DATE_FORMAT = getDateFormatter();
    @HttpGet(route = DATE_ROUTE)
    public ActionResult getDate(Date value) {
        return new ContentResult(DATE_FORMAT.format(value));
    }

    private static DateFormat getDateFormatter() {
        TimeZone timeZone = TimeZone.getTimeZone("UTC");
        DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'"); // Quoted "Z" to indicate UTC, no timezone offset
        formatter.setTimeZone(timeZone);
        return formatter;
    }

    public static final String INSTANT_ROUTE = "/api/boxed/instant/:value";
    public static final DateTimeFormatter INSTANT_FORMAT = DateTimeFormatter.ISO_INSTANT;
    @HttpGet(route = INSTANT_ROUTE)
    public ActionResult getInstant(Instant value) {
        return new ContentResult(INSTANT_FORMAT.format(value));
    }

    public static final String ZONED_DATETIME_ROUTE = "/api/boxed/zoned-date-time/:value";
    public static final DateTimeFormatter ZONED_DATETIME_FORMAT = DateTimeFormatter.ISO_ZONED_DATE_TIME;
    @HttpGet(route = ZONED_DATETIME_ROUTE)
    public ActionResult getZonedDateTime(ZonedDateTime value) {
        return new ContentResult(ZONED_DATETIME_FORMAT.format(value));
    }

    public static final String OFFSET_DATETIME_ROUTE = "/api/boxed/offset-date-time/:value";
    public static final DateTimeFormatter OFFSET_DATETIME_FORMAT = DateTimeFormatter.ISO_OFFSET_DATE_TIME;
    @HttpGet(route = OFFSET_DATETIME_ROUTE)
    public ActionResult getOffsetDateTime(OffsetDateTime value) {
        return new ContentResult(OFFSET_DATETIME_FORMAT.format(value));
    }

    public static final String LOCAL_DATETIME_ROUTE = "/api/boxed/local-date-time/:value";
    public static final DateTimeFormatter LOCAL_DATETIME_FORMAT = DateTimeFormatter.ISO_LOCAL_DATE_TIME;
    @HttpGet(route = LOCAL_DATETIME_ROUTE)
    public ActionResult getLocalDateTime(LocalDateTime value) {
        return new ContentResult(LOCAL_DATETIME_FORMAT.format(value));
    }

    public static final String LOCAL_DATE_ROUTE = "/api/boxed/local-date/:value";
    public static final DateTimeFormatter LOCAL_DATE_FORMAT = DateTimeFormatter.ISO_LOCAL_DATE;
    @HttpGet(route = LOCAL_DATE_ROUTE)
    public ActionResult getLocalDate(LocalDate value) {
        return new ContentResult(LOCAL_DATE_FORMAT.format(value));
    }

    public static final String YEAR_MONTH_ROUTE = "/api/boxed/year-month/:value";
    public static final DateTimeFormatter YEAR_MONTH_FORMAT = DateTimeFormatter.ofPattern("yyyy-MM");
    @HttpGet(route = YEAR_MONTH_ROUTE)
    public ActionResult getYearMonth(YearMonth value) {
        return new ContentResult(YEAR_MONTH_FORMAT.format(value));
    }

    public static final String YEAR_ROUTE = "/api/boxed/year/:value";
    public static final DateTimeFormatter YEAR_FORMAT = DateTimeFormatter.ofPattern("yyyy");
    @HttpGet(route = YEAR_ROUTE)
    public ActionResult getYear(Year value) {
        return new ContentResult(YEAR_FORMAT.format(value));
    }

    public static final String BIG_INTEGER_ROUTE = "/api/boxed/big-integer/:value";
    @HttpGet(route = BIG_INTEGER_ROUTE)
    public ActionResult getBigInteger(BigInteger value) {
        return new ContentResult(Objects.toString(value));
    }

    public static final String BIG_DECIMAL_ROUTE = "/api/boxed/big-decimal/:value";
    @HttpGet(route = BIG_DECIMAL_ROUTE)
    public ActionResult getBigDecimal(BigDecimal value) {
        return new ContentResult(Objects.toString(value));
    }

    public static final String UUID_ROUTE = "/api/boxed/uuid/:value";
    @HttpGet(route = UUID_ROUTE)
    public ActionResult getBigDecimal(UUID value) {
        return new ContentResult(Objects.toString(value));
    }
}
