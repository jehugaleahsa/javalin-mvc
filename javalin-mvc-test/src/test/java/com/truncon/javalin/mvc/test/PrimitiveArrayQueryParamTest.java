package com.truncon.javalin.mvc.test;

import com.truncon.javalin.mvc.test.controllers.PrimitiveArrayParameterController;
import org.junit.Assert;
import org.junit.Test;

import static com.truncon.javalin.mvc.test.QueryUtils.getJsonResponseForGet;
import static com.truncon.javalin.mvc.test.RouteBuilder.param;
import static com.truncon.javalin.mvc.test.RouteBuilder.queryParams;

public final class PrimitiveArrayQueryParamTest {
    @Test
    public void testBoolean() {
        AsyncTestUtils.runTest(app -> {
            String route = RouteBuilder.buildRouteWithQueryParams(
            PrimitiveArrayParameterController.BOOLEAN_ROUTE,
            queryParams(param("value", "false"), param("value", "true")));
            boolean[] actual = getJsonResponseForGet(route, boolean[].class);
            boolean[] expected = new boolean[] { false, true };
            Assert.assertArrayEquals(expected, actual);
        }).join();
    }

    @Test
    public void testInteger() {
        AsyncTestUtils.runTest(app -> {
            String route = RouteBuilder.buildRouteWithQueryParams(
                PrimitiveArrayParameterController.INTEGER_ROUTE,
                queryParams(param("value", "123"), param("value", "456")));
            int[] actual = getJsonResponseForGet(route, int[].class);
            int[] expected = new int[] { 123, 456 };
            Assert.assertArrayEquals(expected, actual);
        }).join();
    }

    @Test
    public void testDouble() {
        AsyncTestUtils.runTest(app -> {
            String route = RouteBuilder.buildRouteWithQueryParams(
                PrimitiveArrayParameterController.DOUBLE_ROUTE,
                queryParams(
                    param("value", Double.toString(Double.MIN_VALUE)),
                    param("value", Double.toString(Double.MAX_VALUE))));
            double[] actual = getJsonResponseForGet(route, double[].class);
            double[] expected = new double[] { Double.MIN_VALUE, Double.MAX_VALUE };
            Assert.assertArrayEquals(expected, actual, 1.0);
        }).join();
    }

    @Test
    public void testString() {
        AsyncTestUtils.runTest(app -> {
            String route = RouteBuilder.buildRouteWithQueryParams(
                PrimitiveArrayParameterController.STRING_ROUTE,
                queryParams(param("value", "Hello"), param("value", "Goodbye")));
            String[] actual = getJsonResponseForGet(route, String[].class);
            String[] expected = new String[] { "Hello", "Goodbye" };
            Assert.assertArrayEquals(expected, actual);
        }).join();
    }

    @Test
    public void testByte() {
        AsyncTestUtils.runTest(app -> {
            String route = RouteBuilder.buildRouteWithQueryParams(
                PrimitiveArrayParameterController.BYTE_ROUTE,
                queryParams(
                    param("value", Byte.toString(Byte.MIN_VALUE)),
                    param("value", Byte.toString(Byte.MAX_VALUE))));
            byte[] actual = getJsonResponseForGet(route, byte[].class);
            byte[] expected = new byte[] { Byte.MIN_VALUE, Byte.MAX_VALUE };
            Assert.assertArrayEquals(expected, actual);
        }).join();
    }

    @Test
    public void testShort() {
        AsyncTestUtils.runTest(app -> {
            String route = RouteBuilder.buildRouteWithQueryParams(
                PrimitiveArrayParameterController.SHORT_ROUTE,
                queryParams(
                    param("value", Short.toString(Short.MIN_VALUE)),
                    param("value", Short.toString(Short.MAX_VALUE))));
            short[] actual = getJsonResponseForGet(route, short[].class);
            short[] expected = new short[] { Short.MIN_VALUE, Short.MAX_VALUE };
            Assert.assertArrayEquals(expected, actual);
        }).join();
    }

    @Test
    public void testFloat() {
        AsyncTestUtils.runTest(app -> {
            String route = RouteBuilder.buildRouteWithQueryParams(
                PrimitiveArrayParameterController.FLOAT_ROUTE,
                queryParams(
                    param("value", Float.toString(Float.MIN_VALUE)),
                    param("value", Float.toString(Float.MAX_VALUE))));
            float[] actual = getJsonResponseForGet(route, float[].class);
            float[] expected = new float[] { Float.MIN_VALUE, Float.MAX_VALUE };
            Assert.assertArrayEquals(expected, actual, 1.0f);
        }).join();
    }

    @Test
    public void testChar() {
        AsyncTestUtils.runTest(app -> {
            String route = RouteBuilder.buildRouteWithQueryParams(
                PrimitiveArrayParameterController.CHAR_ROUTE,
                queryParams(
                    param("value", Character.toString(Character.MIN_VALUE)),
                    param("value", Character.toString(Character.MAX_VALUE))));
            char[] actual = getJsonResponseForGet(route, char[].class);
            char[] expected = new char[] { Character.MIN_VALUE, Character.MAX_VALUE };
            Assert.assertArrayEquals(expected, actual);
        }).join();
    }

    @Test
    public void testLong() {
        AsyncTestUtils.runTest(app -> {
            String route = RouteBuilder.buildRouteWithQueryParams(
                PrimitiveArrayParameterController.LONG_ROUTE,
                queryParams(
                    param("value", Long.toString(Long.MIN_VALUE)),
                    param("value", Long.toString(Long.MAX_VALUE))));
            long[] actual = getJsonResponseForGet(route, long[].class);
            long[] expected = new long[] { Long.MIN_VALUE, Long.MAX_VALUE };
            Assert.assertArrayEquals(expected, actual);
        }).join();
    }
}
