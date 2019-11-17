package com.truncon.javalin.mvc.test.controllers;

import com.truncon.javalin.mvc.api.*;
import com.truncon.javalin.mvc.test.models.FileUploadDetails;
import org.apache.commons.io.IOUtils;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@Controller
public final class FileController {
    public static final String GET_STREAM_ROUTE = "/api/files/stream";
    @HttpGet(route = GET_STREAM_ROUTE)
    public ActionResult getIndexStream() throws IOException {
        Path path = Paths.get("./public/index.html");
        return new StreamResult(Files.newInputStream(path), "text/html");
    }

    public static final String GET_DOWNLOAD_ROUTE = "/api/files/download";
    public static final String CONTENT_TYPE = "text/html";
    public static final String CONTENT_DISPOSITION = "index.html";
    @HttpGet(route = GET_DOWNLOAD_ROUTE)
    public ActionResult getIndexDownload() throws IOException {
        Path path = Paths.get("./public/index.html");
        return new DownloadResult(Files.newInputStream(path),CONTENT_TYPE).setFileName(CONTENT_DISPOSITION);
    }

    public static final String POST_FILE_UPLOAD = "/api/files/upload";
    @HttpPost(route = POST_FILE_UPLOAD)
    public ActionResult postFileUpload(@Named("file") FileUpload upload) throws IOException {
        FileUploadDetails details = new FileUploadDetails();
        details.setContentType(upload.getContentType());
        details.setFileName(upload.getFileName());
        details.setLength(IOUtils.toByteArray(upload.getStream()).length);
        return new JsonResult(details);
    }
}
