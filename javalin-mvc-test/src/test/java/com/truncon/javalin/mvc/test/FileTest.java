package com.truncon.javalin.mvc.test;

import com.truncon.javalin.mvc.test.controllers.FileController;
import org.apache.commons.io.IOUtils;
import org.apache.http.Header;
import org.apache.http.HttpResponse;
import org.apache.http.client.fluent.Request;
import org.junit.Assert;
import org.junit.Test;

import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public final class FileTest {
    @Test
    public void testFileStream() throws Exception {
        AsyncTestUtils.runTest(app -> {
            String route = RouteBuilder.buildRoute(FileController.GET_STREAM_ROUTE);
            String actual = QueryUtils.getStringResponse(route);
            Path path = Paths.get("./public/index.html");
            String expected = IOUtils.toString(Files.newInputStream(path), Charset.defaultCharset());
            Assert.assertEquals(expected, actual);
        });
    }

    @Test
    public void testFileDownload() throws Exception {
        AsyncTestUtils.runTest(app -> {
            String route = RouteBuilder.buildRoute(FileController.GET_DOWNLOAD_ROUTE);
            HttpResponse response = Request.Get(route).execute().returnResponse();
            Header dispositionHeader = response.getFirstHeader("Content-Disposition");
            String disposition = dispositionHeader.getElements()[0].getValue();
            Assert.assertEquals("attachment;fileName=" + FileController.CONTENT_DISPOSITION, disposition);
            String actual = QueryUtils.getStringResponse(route);
            Path path = Paths.get("./public/index.html");
            String expected = IOUtils.toString(Files.newInputStream(path), Charset.defaultCharset());
            Assert.assertEquals(expected, actual);
        });
    }
}
