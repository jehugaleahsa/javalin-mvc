package com.truncon.javalin.mvc.test;

import com.truncon.javalin.mvc.test.controllers.ws.reference.BigDecimalParameterController;
import com.truncon.javalin.mvc.test.controllers.ws.reference.BigIntegerParameterController;
import com.truncon.javalin.mvc.test.controllers.ws.reference.DateParameterController;
import com.truncon.javalin.mvc.test.controllers.ws.reference.LocalDateParameterController;
import com.truncon.javalin.mvc.test.controllers.ws.reference.LocalDateTimeParameterController;
import com.truncon.javalin.mvc.test.controllers.ws.reference.OffsetDateTimeParameterController;
import com.truncon.javalin.mvc.test.controllers.ws.reference.StringParameterController;
import com.truncon.javalin.mvc.test.controllers.ws.reference.UuidParameterController;
import com.truncon.javalin.mvc.test.controllers.ws.reference.YearMonthParameterController;
import com.truncon.javalin.mvc.test.controllers.ws.reference.YearParameterController;
import com.truncon.javalin.mvc.test.controllers.ws.reference.ZonedDateTimeQueryController;
import org.junit.Assert;
import org.junit.Test;

import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.time.Year;
import java.time.YearMonth;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.UUID;

import static com.truncon.javalin.mvc.test.RouteBuilder.buildWsRouteWithPathParams;
import static com.truncon.javalin.mvc.test.RouteBuilder.buildWsRouteWithQueryParams;
import static com.truncon.javalin.mvc.test.RouteBuilder.param;
import static com.truncon.javalin.mvc.test.RouteBuilder.pathParams;
import static com.truncon.javalin.mvc.test.RouteBuilder.queryParams;

public final class WsReferenceParameterTest {
    @Test
    public void testString() throws IOException {
        String value = "hello";
        String route = buildWsRouteWithPathParams(StringParameterController.ROUTE, pathParams(
            param("value", value)
        ));
        AsyncTestUtils.runTestAsync(app ->
            WsTestUtils.ws(route, session -> session.sendStringAndAwaitResponse("").thenAccept(response ->
                Assert.assertEquals(value, response)
            ))
        );
    }

    @Test
    public void testDate() throws IOException {
        Date date = new Date();
        String value = DateParameterController.FORMATTER.format(date);
        String route = buildWsRouteWithPathParams(DateParameterController.ROUTE, pathParams(
            param("value", value)
        ));
        AsyncTestUtils.runTestAsync(app ->
            WsTestUtils.ws(route, session -> session.sendStringAndAwaitResponse("").thenAccept(response ->
                Assert.assertEquals(value, response)
            ))
        );
    }

    @Test
    public void testOffsetDateTime() throws IOException {
        OffsetDateTime now = OffsetDateTime.now();
        String value = DateTimeFormatter.ISO_DATE_TIME.format(now);
        String route = buildWsRouteWithPathParams(OffsetDateTimeParameterController.ROUTE, pathParams(
            param("value", value)
        ));
        AsyncTestUtils.runTestAsync(app ->
            WsTestUtils.ws(route, session -> session.sendStringAndAwaitResponse("").thenAccept(response ->
                Assert.assertEquals(value, response)
            ))
        );
    }

    @Test
    public void testZonedDateTime() throws IOException {
        ZonedDateTime now = ZonedDateTime.now();
        String value = DateTimeFormatter.ISO_ZONED_DATE_TIME.format(now);
        String route = buildWsRouteWithQueryParams(ZonedDateTimeQueryController.ROUTE, queryParams(
            param("value", value)
        ));
        AsyncTestUtils.runTestAsync(app ->
            WsTestUtils.ws(route, session -> session.sendStringAndAwaitResponse("").thenAccept(response ->
                Assert.assertEquals(value, response)
            ))
        );
    }

    @Test
    public void testLocalDateTime() throws IOException {
        LocalDateTime now = LocalDateTime.now();
        String value = DateTimeFormatter.ISO_LOCAL_DATE_TIME.format(now);
        String route = buildWsRouteWithPathParams(LocalDateTimeParameterController.ROUTE, pathParams(
            param("value", value)
        ));
        AsyncTestUtils.runTestAsync(app ->
            WsTestUtils.ws(route, session -> session.sendStringAndAwaitResponse("").thenAccept(response ->
                Assert.assertEquals(value, response)
            ))
        );
    }

    @Test
    public void testLocalDate() throws IOException {
        LocalDate now = LocalDate.now();
        String value = DateTimeFormatter.ISO_LOCAL_DATE.format(now);
        String route = buildWsRouteWithPathParams(LocalDateParameterController.ROUTE, pathParams(
            param("value", value)
        ));
        AsyncTestUtils.runTestAsync(app ->
            WsTestUtils.ws(route, session -> session.sendStringAndAwaitResponse("").thenAccept(response ->
                Assert.assertEquals(value, response)
            ))
        );
    }

    @Test
    public void testYearMonth() throws IOException {
        YearMonth now = YearMonth.now();
        String value = YearMonthParameterController.FORMATTER.format(now);
        String route = buildWsRouteWithPathParams(YearMonthParameterController.ROUTE, pathParams(
            param("value", value)
        ));
        AsyncTestUtils.runTestAsync(app ->
            WsTestUtils.ws(route, session -> session.sendStringAndAwaitResponse("").thenAccept(response ->
                Assert.assertEquals(value, response)
            ))
        );
    }

    @Test
    public void testYear() throws IOException {
        Year now = Year.now();
        String value = YearParameterController.FORMATTER.format(now);
        String route = buildWsRouteWithPathParams(YearParameterController.ROUTE, pathParams(
            param("value", value)
        ));
        AsyncTestUtils.runTestAsync(app ->
            WsTestUtils.ws(route, session -> session.sendStringAndAwaitResponse("").thenAccept(response ->
                Assert.assertEquals(value, response)
            ))
        );
    }

    @Test
    public void testBigInteger() throws IOException {
        String value = "12345678901234567890";
        String route = buildWsRouteWithPathParams(BigIntegerParameterController.ROUTE, pathParams(
            param("value", value)
        ));
        AsyncTestUtils.runTestAsync(app ->
            WsTestUtils.ws(route, session -> session.sendStringAndAwaitResponse("").thenAccept(response ->
                Assert.assertEquals(value, response)
            ))
        );
    }

    @Test
    public void testBigDecimal() throws IOException {
        String value = "12345678901234567890.123456789";
        String route = buildWsRouteWithPathParams(BigDecimalParameterController.ROUTE, pathParams(
            param("value", value)
        ));
        AsyncTestUtils.runTestAsync(app ->
            WsTestUtils.ws(route, session -> session.sendStringAndAwaitResponse("").thenAccept(response ->
                Assert.assertEquals(value, response)
            ))
        );
    }

    @Test
    public void testUuid() throws IOException {
        String value = UUID.randomUUID().toString();
        String route = buildWsRouteWithPathParams(UuidParameterController.ROUTE, pathParams(
            param("value", value)
        ));
        AsyncTestUtils.runTestAsync(app ->
            WsTestUtils.ws(route, session -> session.sendStringAndAwaitResponse("").thenAccept(response ->
                Assert.assertEquals(value, response)
            ))
        );
    }
}
