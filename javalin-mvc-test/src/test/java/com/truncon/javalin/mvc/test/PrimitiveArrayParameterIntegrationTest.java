package com.truncon.javalin.mvc.test;

import com.truncon.javalin.mvc.test.controllers.PrimitiveArrayParameterController;
import org.junit.Assert;
import org.junit.Test;

import static com.truncon.javalin.mvc.test.QueryUtils.*;
import static com.truncon.javalin.mvc.test.RouteBuilder.*;

public final class PrimitiveArrayParameterIntegrationTest {
    @Test
    public void testBoolean() throws Exception {
        try (AppHost app = AppHost.startNew()) {
            String route = buildRoute(
            PrimitiveArrayParameterController.BOOLEAN_ROUTE,
            pathParams(),
            queryParams(param("value", "false"), param("value", "true")));
            boolean[] actual = getJsonResponse(route, boolean[].class);
            boolean[] expected = new boolean[] { false, true };
            Assert.assertArrayEquals(expected, actual);
        }
    }

    @Test
    public void testInteger() throws Exception {
        try (AppHost app = AppHost.startNew()) {
            String route = buildRoute(
                PrimitiveArrayParameterController.INTEGER_ROUTE,
                pathParams(),
                queryParams(param("value", "123"), param("value", "456")));
            int[] actual = getJsonResponse(route, int[].class);
            int[] expected = new int[] { 123, 456 };
            Assert.assertArrayEquals(expected, actual);
        }
    }

    @Test
    public void testDouble() throws Exception {
        try (AppHost app = AppHost.startNew()) {
            String route = buildRoute(
                PrimitiveArrayParameterController.DOUBLE_ROUTE,
                pathParams(),
                queryParams(
                    param("value", Double.toString(Double.MIN_VALUE)),
                    param("value", Double.toString(Double.MAX_VALUE))));
            double[] actual = getJsonResponse(route, double[].class);
            double[] expected = new double[] { Double.MIN_VALUE, Double.MAX_VALUE };
            Assert.assertArrayEquals(expected, actual, 1.0);
        }
    }

    @Test
    public void testString() throws Exception {
        try (AppHost app = AppHost.startNew()) {
            String route = buildRoute(
                PrimitiveArrayParameterController.STRING_ROUTE,
                pathParams(),
                queryParams(param("value", "Hello"), param("value", "Goodbye")));
            String[] actual = getJsonResponse(route, String[].class);
            String[] expected = new String[] { "Hello", "Goodbye" };
            Assert.assertArrayEquals(expected, actual);
        }
    }

    @Test
    public void testByte() throws Exception {
        try (AppHost app = AppHost.startNew()) {
            String route = buildRoute(
                PrimitiveArrayParameterController.BYTE_ROUTE,
                pathParams(),
                queryParams(
                    param("value", Byte.toString(Byte.MIN_VALUE)),
                    param("value", Byte.toString(Byte.MAX_VALUE))));
            byte[] actual = getJsonResponse(route, byte[].class);
            byte[] expected = new byte[] { Byte.MIN_VALUE, Byte.MAX_VALUE };
            Assert.assertArrayEquals(expected, actual);
        }
    }

    @Test
    public void testShort() throws Exception {
        try (AppHost app = AppHost.startNew()) {
            String route = buildRoute(
                PrimitiveArrayParameterController.SHORT_ROUTE,
                pathParams(),
                queryParams(
                    param("value", Short.toString(Short.MIN_VALUE)),
                    param("value", Short.toString(Short.MAX_VALUE))));
            short[] actual = getJsonResponse(route, short[].class);
            short[] expected = new short[] { Short.MIN_VALUE, Short.MAX_VALUE };
            Assert.assertArrayEquals(expected, actual);
        }
    }

    @Test
    public void testFloat() throws Exception {
        try (AppHost app = AppHost.startNew()) {
            String route = buildRoute(
                PrimitiveArrayParameterController.FLOAT_ROUTE,
                pathParams(),
                queryParams(
                    param("value", Float.toString(Float.MIN_VALUE)),
                    param("value", Float.toString(Float.MAX_VALUE))));
            float[] actual = getJsonResponse(route, float[].class);
            float[] expected = new float[] { Float.MIN_VALUE, Float.MAX_VALUE };
            Assert.assertArrayEquals(expected, actual, 1.0f);
        }
    }

    @Test
    public void testChar() throws Exception {
        try (AppHost app = AppHost.startNew()) {
            String route = buildRoute(
                PrimitiveArrayParameterController.CHAR_ROUTE,
                pathParams(),
                queryParams(
                    param("value", Character.toString(Character.MIN_VALUE)),
                    param("value", Character.toString(Character.MAX_VALUE))));
            char[] actual = getJsonResponse(route, char[].class);
            char[] expected = new char[] { Character.MIN_VALUE, Character.MAX_VALUE };
            Assert.assertArrayEquals(expected, actual);
        }
    }

    @Test
    public void testLong() throws Exception {
        try (AppHost app = AppHost.startNew()) {
            String route = buildRoute(
                PrimitiveArrayParameterController.LONG_ROUTE,
                pathParams(),
                queryParams(
                    param("value", Long.toString(Long.MIN_VALUE)),
                    param("value", Long.toString(Long.MAX_VALUE))));
            long[] actual = getJsonResponse(route, long[].class);
            long[] expected = new long[] { Long.MIN_VALUE, Long.MAX_VALUE };
            Assert.assertArrayEquals(expected, actual);
        }
    }
}
