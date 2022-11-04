package com.truncon.javalin.mvc.api.ws;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.nio.ByteBuffer;

final class WsBinaryResultTest {
    @Test
    void testCtor_byteArray() {
        byte[] data = new byte[0];
        WsBinaryResult result = new WsBinaryResult(data);
        Assertions.assertSame(data, result.getData().array());
    }

    @Test
    void testCtor_rangedByteArray() {
        byte[] data = new byte[0];
        WsBinaryResult result = new WsBinaryResult(data, 0, 0);
        ByteBuffer buffer = result.getData();
        Assertions.assertSame(data, buffer.array());
        Assertions.assertEquals(0, buffer.arrayOffset());
        Assertions.assertEquals(0, buffer.limit());
    }

    @Test
    void testCtor_buffer() {
        ByteBuffer buffer = ByteBuffer.allocate(0);
        WsBinaryResult result = new WsBinaryResult(buffer);
        Assertions.assertSame(buffer, result.getData());
    }

    @Test
    void testExecute() {
        ByteBuffer buffer = ByteBuffer.allocate(0);
        WsBinaryResult result = new WsBinaryResult(buffer);
        MockWsContext context = new MockWsContext();
        result.execute(context);
        Assertions.assertSame(buffer, context.getResponse().getBinary());
    }
}
