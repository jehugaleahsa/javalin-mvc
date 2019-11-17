package com.truncon.javalin.mvc.test;

import com.truncon.javalin.mvc.test.controllers.PrimitiveArrayParameterController;
import org.junit.Assert;
import org.junit.Test;

import static com.truncon.javalin.mvc.test.QueryUtils.*;
import static com.truncon.javalin.mvc.test.RouteBuilder.*;

public final class PrimitiveArrayParameterTest {
    @Test
    public void testBoolean() throws Exception {
        AsyncTestUtils.runTest(app -> {
            String route = buildRoute(
            PrimitiveArrayParameterController.BOOLEAN_ROUTE,
            pathParams(),
            queryParams(param("value", "false"), param("value", "true")));
            boolean[] actual = getGetJsonResponse(route, boolean[].class);
            boolean[] expected = new boolean[] { false, true };
            Assert.assertArrayEquals(expected, actual);
        }).join();
    }

    @Test
    public void testInteger() throws Exception {
        AsyncTestUtils.runTest(app -> {
            String route = buildRoute(
                PrimitiveArrayParameterController.INTEGER_ROUTE,
                pathParams(),
                queryParams(param("value", "123"), param("value", "456")));
            int[] actual = getGetJsonResponse(route, int[].class);
            int[] expected = new int[] { 123, 456 };
            Assert.assertArrayEquals(expected, actual);
        }).join();
    }

    @Test
    public void testDouble() throws Exception {
        AsyncTestUtils.runTest(app -> {
            String route = buildRoute(
                PrimitiveArrayParameterController.DOUBLE_ROUTE,
                pathParams(),
                queryParams(
                    param("value", Double.toString(Double.MIN_VALUE)),
                    param("value", Double.toString(Double.MAX_VALUE))));
            double[] actual = getGetJsonResponse(route, double[].class);
            double[] expected = new double[] { Double.MIN_VALUE, Double.MAX_VALUE };
            Assert.assertArrayEquals(expected, actual, 1.0);
        }).join();
    }

    @Test
    public void testString() throws Exception {
        AsyncTestUtils.runTest(app -> {
            String route = buildRoute(
                PrimitiveArrayParameterController.STRING_ROUTE,
                pathParams(),
                queryParams(param("value", "Hello"), param("value", "Goodbye")));
            String[] actual = getGetJsonResponse(route, String[].class);
            String[] expected = new String[] { "Hello", "Goodbye" };
            Assert.assertArrayEquals(expected, actual);
        }).join();
    }

    @Test
    public void testByte() throws Exception {
        AsyncTestUtils.runTest(app -> {
            String route = buildRoute(
                PrimitiveArrayParameterController.BYTE_ROUTE,
                pathParams(),
                queryParams(
                    param("value", Byte.toString(Byte.MIN_VALUE)),
                    param("value", Byte.toString(Byte.MAX_VALUE))));
            byte[] actual = getGetJsonResponse(route, byte[].class);
            byte[] expected = new byte[] { Byte.MIN_VALUE, Byte.MAX_VALUE };
            Assert.assertArrayEquals(expected, actual);
        }).join();
    }

    @Test
    public void testShort() throws Exception {
        AsyncTestUtils.runTest(app -> {
            String route = buildRoute(
                PrimitiveArrayParameterController.SHORT_ROUTE,
                pathParams(),
                queryParams(
                    param("value", Short.toString(Short.MIN_VALUE)),
                    param("value", Short.toString(Short.MAX_VALUE))));
            short[] actual = getGetJsonResponse(route, short[].class);
            short[] expected = new short[] { Short.MIN_VALUE, Short.MAX_VALUE };
            Assert.assertArrayEquals(expected, actual);
        }).join();
    }

    @Test
    public void testFloat() throws Exception {
        AsyncTestUtils.runTest(app -> {
            String route = buildRoute(
                PrimitiveArrayParameterController.FLOAT_ROUTE,
                pathParams(),
                queryParams(
                    param("value", Float.toString(Float.MIN_VALUE)),
                    param("value", Float.toString(Float.MAX_VALUE))));
            float[] actual = getGetJsonResponse(route, float[].class);
            float[] expected = new float[] { Float.MIN_VALUE, Float.MAX_VALUE };
            Assert.assertArrayEquals(expected, actual, 1.0f);
        }).join();
    }

    @Test
    public void testChar() throws Exception {
        AsyncTestUtils.runTest(app -> {
            String route = buildRoute(
                PrimitiveArrayParameterController.CHAR_ROUTE,
                pathParams(),
                queryParams(
                    param("value", Character.toString(Character.MIN_VALUE)),
                    param("value", Character.toString(Character.MAX_VALUE))));
            char[] actual = getGetJsonResponse(route, char[].class);
            char[] expected = new char[] { Character.MIN_VALUE, Character.MAX_VALUE };
            Assert.assertArrayEquals(expected, actual);
        }).join();
    }

    @Test
    public void testLong() throws Exception {
        AsyncTestUtils.runTest(app -> {
            String route = buildRoute(
                PrimitiveArrayParameterController.LONG_ROUTE,
                pathParams(),
                queryParams(
                    param("value", Long.toString(Long.MIN_VALUE)),
                    param("value", Long.toString(Long.MAX_VALUE))));
            long[] actual = getGetJsonResponse(route, long[].class);
            long[] expected = new long[] { Long.MIN_VALUE, Long.MAX_VALUE };
            Assert.assertArrayEquals(expected, actual);
        }).join();
    }
}
