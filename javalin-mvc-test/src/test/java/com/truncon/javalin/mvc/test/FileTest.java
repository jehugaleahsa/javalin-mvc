package com.truncon.javalin.mvc.test;

import com.truncon.javalin.mvc.test.controllers.FileController;
import com.truncon.javalin.mvc.test.models.FileUploadDetails;
import org.apache.commons.io.IOUtils;
import org.apache.http.Header;
import org.apache.http.HttpResponse;
import org.apache.http.client.fluent.Request;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

final class FileTest {
    @Test
    void testFileStream() {
        AsyncTestUtils.runTest(app -> {
            String route = RouteBuilder.buildRoute(FileController.GET_STREAM_ROUTE);
            String actual = QueryUtils.getStringForGet(route);
            Path path = Paths.get("./public/index.html");
            String expected = IOUtils.toString(Files.newInputStream(path), Charset.defaultCharset());
            Assertions.assertEquals(expected, actual);
        });
    }

    @Test
    void testFileDownload() throws Exception {
        AsyncTestUtils.runTest(app -> {
            String route = RouteBuilder.buildRoute(FileController.GET_DOWNLOAD_ROUTE);
            HttpResponse response = Request.Get(route).execute().returnResponse();
            Header dispositionHeader = response.getFirstHeader("Content-Disposition");
            String disposition = dispositionHeader.getValue();
            Assertions.assertEquals("attachment;fileName=" + FileController.CONTENT_DISPOSITION, disposition);
            String actual = QueryUtils.getStringForGet(route);
            Path path = Paths.get("./public/index.html");
            String expected = IOUtils.toString(Files.newInputStream(path), Charset.defaultCharset());
            Assertions.assertEquals(expected, actual);
        });
    }

    @Test
    void testFileUpload() throws Exception {
        AsyncTestUtils.runTest(app -> {
            String route = RouteBuilder.buildRoute(FileController.POST_FILE_UPLOAD);
            Path path = Paths.get("./public/index.html");
            byte[] fileData = IOUtils.toByteArray(Files.newInputStream(path));
            MultipartEntityBuilder uploadBuilder = MultipartEntityBuilder.create();
            uploadBuilder.addBinaryBody("file", fileData, ContentType.TEXT_HTML, "index.html");
            uploadBuilder.setMode(HttpMultipartMode.BROWSER_COMPATIBLE);
            String json = Request.Post(route)
                .body(uploadBuilder.build())
                .execute()
                .returnContent()
                .asString(StandardCharsets.UTF_8);
            FileUploadDetails actual = QueryUtils.parseJson(json, FileUploadDetails.class);
            Assertions.assertNotNull(actual);
            Assertions.assertEquals(ContentType.TEXT_HTML.toString(), actual.getContentType());
            Assertions.assertEquals("index.html", actual.getFileName());
            Assertions.assertEquals(fileData.length, actual.getLength());
        });
    }
}
