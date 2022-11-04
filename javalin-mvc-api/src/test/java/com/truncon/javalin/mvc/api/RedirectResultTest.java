package com.truncon.javalin.mvc.api;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

final class RedirectResultTest {
    @Test
    void testCtor_location() {
        RedirectResult result = new RedirectResult("/index");
        Assertions.assertEquals("/index", result.getLocation());
        Assertions.assertFalse(result.isPermanent());
        Assertions.assertFalse(result.isMethodPreserved());
    }

    @Test
    void testCtor_locationPermanent() {
        RedirectResult result = new RedirectResult("/index", true);
        Assertions.assertEquals("/index", result.getLocation());
        Assertions.assertTrue(result.isPermanent());
        Assertions.assertFalse(result.isMethodPreserved());
    }

    @Test
    void testCtor_locationPermanentPreserveMethod() {
        RedirectResult result = new RedirectResult("/index", false, true);
        Assertions.assertEquals("/index", result.getLocation());
        Assertions.assertFalse(result.isPermanent());
        Assertions.assertTrue(result.isMethodPreserved());
    }

    @Test
    void testSetPermanent() {
        RedirectResult result = new RedirectResult("/index");
        result.setPermanent(true);
        Assertions.assertTrue(result.isPermanent());
    }

    @Test
    void testSetMethodPreserved() {
        RedirectResult result = new RedirectResult("/index");
        result.setMethodPreserved(true);
        Assertions.assertTrue(result.isMethodPreserved());
    }

    @Test
    void testExecute() {
        RedirectResult result = new RedirectResult("/index");
        MockHttpContext context = new MockHttpContext();
        result.execute(context);
        MockHttpResponse response = context.getResponse();
        Assertions.assertEquals("/index", response.getRedirectLocation());
        Assertions.assertEquals(302, response.getStatusCode());
    }

    @Test
    void testExecute_permanent() {
        RedirectResult result = new RedirectResult("/index", true);
        MockHttpContext context = new MockHttpContext();
        result.execute(context);
        MockHttpResponse response = context.getResponse();
        Assertions.assertEquals("/index", response.getRedirectLocation());
        Assertions.assertEquals(301, response.getStatusCode());
    }

    @Test
    void testExecute_preserveMethod() {
        RedirectResult result = new RedirectResult("/index", false, true);
        MockHttpContext context = new MockHttpContext();
        result.execute(context);
        MockHttpResponse response = context.getResponse();
        Assertions.assertEquals("/index", response.getRedirectLocation());
        Assertions.assertEquals(307, response.getStatusCode());
    }

    @Test
    void testExecute_permanentPreserveMethod() {
        RedirectResult result = new RedirectResult("/index", true, true);
        MockHttpContext context = new MockHttpContext();
        result.execute(context);
        MockHttpResponse response = context.getResponse();
        Assertions.assertEquals("/index", response.getRedirectLocation());
        Assertions.assertEquals(308, response.getStatusCode());
    }
}
