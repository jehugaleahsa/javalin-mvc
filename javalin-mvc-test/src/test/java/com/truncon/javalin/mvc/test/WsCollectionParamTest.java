package com.truncon.javalin.mvc.test;

import com.truncon.javalin.mvc.test.controllers.ws.parameters.collections.ArrayListController;
import com.truncon.javalin.mvc.test.controllers.ws.parameters.collections.CollectionController;
import com.truncon.javalin.mvc.test.controllers.ws.parameters.collections.HashSetController;
import com.truncon.javalin.mvc.test.controllers.ws.parameters.collections.IterableController;
import com.truncon.javalin.mvc.test.controllers.ws.parameters.collections.LinkedHashSetController;
import com.truncon.javalin.mvc.test.controllers.ws.parameters.collections.LinkedListController;
import com.truncon.javalin.mvc.test.controllers.ws.parameters.collections.ListController;
import com.truncon.javalin.mvc.test.controllers.ws.parameters.collections.SetController;
import org.junit.Assert;
import org.junit.Test;

import java.math.BigInteger;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.Comparator;
import java.util.UUID;

import static com.truncon.javalin.mvc.test.RouteBuilder.param;
import static com.truncon.javalin.mvc.test.RouteBuilder.queryParams;

public final class WsCollectionParamTest {
    @Test
    public void testIterable() {
        AsyncTestUtils.runTest(app -> {
            String route = RouteBuilder.buildWsRouteWithQueryParams(
                IterableController.ROUTE,
                queryParams(param("value", "1"), param("value", null), param("value", "2")));
            WsTestUtils.ws(route, sessionManager ->
                sessionManager.sendStringAndAwaitJsonResponse("", Integer[].class).thenAccept((actual) -> {
                    Integer[] expected = new Integer[] { 1, null, 2 };
                    Assert.assertArrayEquals(expected, actual);
                })
            );
        });
    }

    @Test
    public void testCollection() {
        AsyncTestUtils.runTest(app -> {
            String route = RouteBuilder.buildWsRouteWithQueryParams(
                CollectionController.ROUTE,
                queryParams(param("value", "1.23"), param("value", null), param("value", "2.34")));
            WsTestUtils.ws(route, sessionManager ->
                sessionManager.sendStringAndAwaitJsonResponse("", Double[].class).thenAccept((actual) -> {
                    Double[] expected = new Double[] { 1.23, null, 2.34 };
                    Assert.assertArrayEquals(expected, actual);
                })
            );
        });
    }

    @Test
    public void testList() {
        AsyncTestUtils.runTest(app -> {
            String route = RouteBuilder.buildWsRouteWithQueryParams(
                ListController.ROUTE,
                queryParams(param("value", "one"), param("value", null), param("value", "two")));
            WsTestUtils.ws(route, sessionManager ->
                sessionManager.sendStringAndAwaitJsonResponse("", String[].class).thenAccept((actual) -> {
                    String[] expected = new String[] { "one", null, "two" };
                    Assert.assertArrayEquals(expected, actual);
                })
            );
        });
    }

    @Test
    public void testSet() {
        AsyncTestUtils.runTest(app -> {
            String route = RouteBuilder.buildWsRouteWithQueryParams(
                SetController.ROUTE,
                queryParams(param("value", "hello"), param("value", null), param("value", "goodbye")));
            WsTestUtils.ws(route, sessionManager ->
                sessionManager.sendStringAndAwaitJsonResponse("", String[].class).thenAccept((actual) -> {
                    String[] expected = new String[] { "hello", null, "goodbye" };
                    Assert.assertArrayEquals(expected, actual);
                })
            );
        });
    }

    @Test
    public void testArrayList() {
        AsyncTestUtils.runTest(app -> {
            UUID first = UUID.randomUUID();
            UUID second = UUID.randomUUID();
            String route = RouteBuilder.buildWsRouteWithQueryParams(
                ArrayListController.ROUTE,
                queryParams(param("value", first.toString()), param("value", null), param("value", second.toString())));
            WsTestUtils.ws(route, sessionManager ->
                sessionManager.sendStringAndAwaitJsonResponse("", UUID[].class).thenAccept((actual) -> {
                    UUID[] expected = new UUID[] { first, null, second };
                    Assert.assertArrayEquals(expected, actual);
                })
            );
        });
    }

    @Test
    public void testLinkedList() {
        AsyncTestUtils.runTest(app -> {
            DateTimeFormatter formatter = DateTimeFormatter.ISO_LOCAL_DATE_TIME;
            LocalDateTime first = LocalDateTime.of(2022, 6, 27, 22, 55, 0);
            LocalDateTime second = LocalDateTime.of(2022, 6, 27, 22, 56, 12);
            String route = RouteBuilder.buildWsRouteWithQueryParams(
                LinkedListController.ROUTE,
                queryParams(
                    param("value", formatter.format(first)),
                    param("value", null),
                    param("value", formatter.format(second))
                )
            );
            WsTestUtils.ws(route, sessionManager ->
                sessionManager.sendStringAndAwaitJsonResponse("", LocalDateTime[].class).thenAccept((actual) -> {
                    LocalDateTime[] expected = new LocalDateTime[] { first, null, second };
                    Assert.assertArrayEquals(expected, actual);
                })
            );
        });
    }

    @Test
    public void testHashSet() {
        AsyncTestUtils.runTest(app -> {
            String route = RouteBuilder.buildWsRouteWithQueryParams(
                HashSetController.ROUTE,
                queryParams(
                    param("value", "hello"),
                    param("value", null),
                    param("value", "goodbye")
                )
            );
            WsTestUtils.ws(route, sessionManager ->
                sessionManager.sendStringAndAwaitJsonResponse("", String[].class).thenAccept((actual) -> {
                    Arrays.sort(actual, Comparator.nullsFirst(Comparator.naturalOrder()));
                    String[] expected = new String[] { null, "goodbye", "hello" };
                    Assert.assertArrayEquals(expected, actual);
                })
            );
        });
    }

    @Test
    public void testLinkedHashSet() {
        AsyncTestUtils.runTest(app -> {
            String route = RouteBuilder.buildWsRouteWithQueryParams(
                LinkedHashSetController.ROUTE,
                queryParams(
                    param("value", "1234567890"),
                    param("value", null),
                    param("value", "2345678901")
                )
            );
            WsTestUtils.ws(route, sessionManager ->
                sessionManager.sendStringAndAwaitJsonResponse("", BigInteger[].class).thenAccept((actual) -> {
                    BigInteger[] expected = new BigInteger[] {
                        new BigInteger("1234567890"), null, new BigInteger("2345678901")
                    };
                    Assert.assertArrayEquals(expected, actual);
                })
            );
        });
    }
}
