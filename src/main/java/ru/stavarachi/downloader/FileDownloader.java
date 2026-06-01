package ru.stavarachi.downloader;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

public class FileDownloader {
    private final HttpClient client;
    private static final Logger logger = LoggerFactory.getLogger(FileDownloader.class);
    public FileDownloader(HttpClient client) {
        this.client = client;
    }

    public Path download(String url, String fileName, Path targetDir) {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .header("User-Agent", "Mozilla/5.0")
                .GET()
                .build();

        HttpResponse<InputStream> response = null;
        try {
            response = client.send(
                    request,
                    HttpResponse.BodyHandlers.ofInputStream()
            );
        } catch (IOException | InterruptedException e) {
            logger.error("Error: ", e);
        }

        if (response.statusCode() != 200) {
            logger.error("Download failed: {}", response.statusCode());
        }

        Path output = targetDir.resolve(fileName);
        try {
            Files.createDirectories(output.getParent());
        } catch (IOException e) {
            logger.error("Error: ", e);
        }

        try (InputStream inputStream = response.body()){
            Files.copy(inputStream, output, StandardCopyOption.REPLACE_EXISTING);
        } catch (IOException e) {
            logger.error("Error: ", e);
        }

        return output;
    }
}
