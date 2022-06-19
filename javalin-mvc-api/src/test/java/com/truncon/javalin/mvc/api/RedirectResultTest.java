package com.truncon.javalin.mvc.api;

import org.junit.Assert;
import org.junit.Test;

public final class RedirectResultTest {
    @Test
    public void testCtor_location() {
        RedirectResult result = new RedirectResult("/index");
        Assert.assertEquals("/index", result.getLocation());
        Assert.assertFalse(result.isPermanent());
        Assert.assertFalse(result.isMethodPreserved());
    }

    @Test
    public void testCtor_locationPermanent() {
        RedirectResult result = new RedirectResult("/index", true);
        Assert.assertEquals("/index", result.getLocation());
        Assert.assertTrue(result.isPermanent());
        Assert.assertFalse(result.isMethodPreserved());
    }

    @Test
    public void testCtor_locationPermanentPreserveMethod() {
        RedirectResult result = new RedirectResult("/index", false, true);
        Assert.assertEquals("/index", result.getLocation());
        Assert.assertFalse(result.isPermanent());
        Assert.assertTrue(result.isMethodPreserved());
    }

    @Test
    public void testSetPermanent() {
        RedirectResult result = new RedirectResult("/index");
        result.setPermanent(true);
        Assert.assertTrue(result.isPermanent());
    }

    @Test
    public void testSetMethodPreserved() {
        RedirectResult result = new RedirectResult("/index");
        result.setMethodPreserved(true);
        Assert.assertTrue(result.isMethodPreserved());
    }

    @Test
    public void testExecute() {
        RedirectResult result = new RedirectResult("/index");
        MockHttpContext context = new MockHttpContext();
        result.execute(context);
        MockHttpResponse response = context.getResponse();
        Assert.assertEquals("/index", response.getRedirectLocation());
        Assert.assertEquals(302, response.getStatusCode());
    }

    @Test
    public void testExecute_permanent() {
        RedirectResult result = new RedirectResult("/index", true);
        MockHttpContext context = new MockHttpContext();
        result.execute(context);
        MockHttpResponse response = context.getResponse();
        Assert.assertEquals("/index", response.getRedirectLocation());
        Assert.assertEquals(301, response.getStatusCode());
    }

    @Test
    public void testExecute_preserveMethod() {
        RedirectResult result = new RedirectResult("/index", false, true);
        MockHttpContext context = new MockHttpContext();
        result.execute(context);
        MockHttpResponse response = context.getResponse();
        Assert.assertEquals("/index", response.getRedirectLocation());
        Assert.assertEquals(307, response.getStatusCode());
    }

    @Test
    public void testExecute_permanentPreserveMethod() {
        RedirectResult result = new RedirectResult("/index", true, true);
        MockHttpContext context = new MockHttpContext();
        result.execute(context);
        MockHttpResponse response = context.getResponse();
        Assert.assertEquals("/index", response.getRedirectLocation());
        Assert.assertEquals(308, response.getStatusCode());
    }
}
