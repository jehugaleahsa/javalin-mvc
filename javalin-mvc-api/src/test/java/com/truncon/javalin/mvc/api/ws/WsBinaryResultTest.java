package com.truncon.javalin.mvc.api.ws;

import org.junit.Assert;
import org.junit.Test;

import java.nio.ByteBuffer;

public final class WsBinaryResultTest {
    @Test
    public void testCtor_byteArray() {
        byte[] data = new byte[0];
        WsBinaryResult result = new WsBinaryResult(data);
        Assert.assertSame(data, result.getData().array());
    }

    @Test
    public void testCtor_rangedByteArray() {
        byte[] data = new byte[0];
        WsBinaryResult result = new WsBinaryResult(data, 0, 0);
        ByteBuffer buffer = result.getData();
        Assert.assertSame(data, buffer.array());
        Assert.assertEquals(0, buffer.arrayOffset());
        Assert.assertEquals(0, buffer.limit());
    }

    @Test
    public void testCtor_buffer() {
        ByteBuffer buffer = ByteBuffer.allocate(0);
        WsBinaryResult result = new WsBinaryResult(buffer);
        Assert.assertSame(buffer, result.getData());
    }

    @Test
    public void testExecute() {
        ByteBuffer buffer = ByteBuffer.allocate(0);
        WsBinaryResult result = new WsBinaryResult(buffer);
        MockWsContext context = new MockWsContext();
        result.execute(context);
        Assert.assertSame(buffer, context.getResponse().getBinary());
    }
}
