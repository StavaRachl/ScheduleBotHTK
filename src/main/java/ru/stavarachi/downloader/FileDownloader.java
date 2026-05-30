package ru.stavarachi.downloader;

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

    public FileDownloader(HttpClient client) {
        this.client = client;
    }

    public Path download(String url, String fileName, Path targetDir) throws Exception {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .header("User-Agent", "Mozilla/5.0")
                .GET()
                .build();

        HttpResponse<InputStream> response = client.send(
                request,
                HttpResponse.BodyHandlers.ofInputStream()
        );

        if (response.statusCode() != 200) {
            throw new RuntimeException("Download failed: " + response.statusCode());
        }

        Path output = targetDir.resolve(fileName);
        Files.createDirectories(output.getParent());

        try (InputStream inputStream = response.body()){
            Files.copy(inputStream, output, StandardCopyOption.REPLACE_EXISTING);
        }

        return output;
    }
}
