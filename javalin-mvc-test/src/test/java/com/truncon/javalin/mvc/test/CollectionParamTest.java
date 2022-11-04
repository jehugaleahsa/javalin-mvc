package com.truncon.javalin.mvc.test;

import com.truncon.javalin.mvc.test.controllers.CollectionParameterController;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.math.BigInteger;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.Comparator;
import java.util.UUID;

import static com.truncon.javalin.mvc.test.QueryUtils.getJsonResponseForGet;
import static com.truncon.javalin.mvc.test.RouteBuilder.param;
import static com.truncon.javalin.mvc.test.RouteBuilder.queryParams;

final class CollectionParamTest {
    @Test
    void testIterable() {
        AsyncTestUtils.runTest(app -> {
            String route = RouteBuilder.buildRouteWithQueryParams(
                CollectionParameterController.ITERABLE_ROUTE,
                queryParams(param("value", "1"), param("value", null), param("value", "2")));
            Integer[] actual = getJsonResponseForGet(route, Integer[].class);
            Integer[] expected = new Integer[] { 1, null, 2 };
            Assertions.assertArrayEquals(expected, actual);
        });
    }

    @Test
    void testCollection() {
        AsyncTestUtils.runTest(app -> {
            String route = RouteBuilder.buildRouteWithQueryParams(
                CollectionParameterController.COLLECTION_ROUTE,
                queryParams(param("value", "1.23"), param("value", null), param("value", "2.34")));
            Double[] actual = getJsonResponseForGet(route, Double[].class);
            Double[] expected = new Double[] { 1.23, null, 2.34 };
            Assertions.assertArrayEquals(expected, actual);
        });
    }

    @Test
    void testList() {
        AsyncTestUtils.runTest(app -> {
            String route = RouteBuilder.buildRouteWithQueryParams(
                CollectionParameterController.LIST_ROUTE,
                queryParams(param("value", "one"), param("value", null), param("value", "two")));
            String[] actual = getJsonResponseForGet(route, String[].class);
            String[] expected = new String[] { "one", null, "two" };
            Assertions.assertArrayEquals(expected, actual);
        });
    }

    @Test
    void testSet() {
        AsyncTestUtils.runTest(app -> {
            String route = RouteBuilder.buildRouteWithQueryParams(
                CollectionParameterController.SET_ROUTE,
                queryParams(param("value", "hello"), param("value", null), param("value", "goodbye")));
            String[] actual = getJsonResponseForGet(route, String[].class);
            String[] expected = new String[] { "hello", null, "goodbye" };
            Assertions.assertArrayEquals(expected, actual);
        });
    }

    @Test
    void testSortedSet() {
        AsyncTestUtils.runTest(app -> {
            // Default comparator doesn't handle nulls
            String route = RouteBuilder.buildRouteWithQueryParams(
                CollectionParameterController.SORTED_SET_ROUTE,
                queryParams(param("value", "hello"), param("value", "goodbye")));
            String[] actual = getJsonResponseForGet(route, String[].class);
            String[] expected = new String[] { "goodbye", "hello" };
            Assertions.assertArrayEquals(expected, actual);
        });
    }

    @Test
    void testArrayList() {
        AsyncTestUtils.runTest(app -> {
            UUID first = UUID.randomUUID();
            UUID second = UUID.randomUUID();
            String route = RouteBuilder.buildRouteWithQueryParams(
                CollectionParameterController.ARRAY_LIST_ROUTE,
                queryParams(param("value", first.toString()), param("value", null), param("value", second.toString())));
            UUID[] actual = getJsonResponseForGet(route, UUID[].class);
            UUID[] expected = new UUID[] { first, null, second };
            Assertions.assertArrayEquals(expected, actual);
        });
    }

    @Test
    void testLinkedList() {
        AsyncTestUtils.runTest(app -> {
            DateTimeFormatter formatter = DateTimeFormatter.ISO_LOCAL_DATE_TIME;
            LocalDateTime first = LocalDateTime.of(2022, 6, 27, 22, 55, 0);
            LocalDateTime second = LocalDateTime.of(2022, 6, 27, 22, 56, 12);
            String route = RouteBuilder.buildRouteWithQueryParams(
                CollectionParameterController.LINKED_LIST_ROUTE,
                queryParams(
                    param("value", formatter.format(first)),
                    param("value", null),
                    param("value", formatter.format(second))
                )
            );
            LocalDateTime[] actual = getJsonResponseForGet(route, LocalDateTime[].class);
            LocalDateTime[] expected = new LocalDateTime[] { first, null, second };
            Assertions.assertArrayEquals(expected, actual);
        });
    }

    @Test
    void testHashSet() {
        AsyncTestUtils.runTest(app -> {
            String route = RouteBuilder.buildRouteWithQueryParams(
                CollectionParameterController.HASH_SET_ROUTE,
                queryParams(
                    param("value", "hello"),
                    param("value", null),
                    param("value", "goodbye")
                )
            );
            String[] actual = getJsonResponseForGet(route, String[].class);
            Arrays.sort(actual, Comparator.nullsFirst(Comparator.naturalOrder()));
            String[] expected = new String[] { null, "goodbye", "hello" };
            Assertions.assertArrayEquals(expected, actual);
        });
    }

    @Test
    void testLinkedHashSet() {
        AsyncTestUtils.runTest(app -> {
            String route = RouteBuilder.buildRouteWithQueryParams(
                CollectionParameterController.LINKED_HASH_SET_ROUTE,
                queryParams(
                    param("value", "1234567890"),
                    param("value", null),
                    param("value", "2345678901")
                )
            );
            BigInteger[] actual = getJsonResponseForGet(route, BigInteger[].class);
            BigInteger[] expected = new BigInteger[] {
                new BigInteger("1234567890"), null, new BigInteger("2345678901")
            };
            Assertions.assertArrayEquals(expected, actual);
        });
    }

    @Test
    void testTreeSet() {
        AsyncTestUtils.runTest(app -> {
            // Default comparator doesn't handle nulls
            String route = RouteBuilder.buildRouteWithQueryParams(
                CollectionParameterController.TREE_SET_ROUTE,
                queryParams(
                    param("value", "1234567890"),
                    param("value", "2345678901")
                )
            );
            BigInteger[] actual = getJsonResponseForGet(route, BigInteger[].class);
            BigInteger[] expected = new BigInteger[] {
                new BigInteger("1234567890"), new BigInteger("2345678901")
            };
            Assertions.assertArrayEquals(expected, actual);
        });
    }
}
