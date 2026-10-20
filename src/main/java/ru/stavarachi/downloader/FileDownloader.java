
package ru.stavarachi.downloader;

import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.Locale;

public class FileDownloader {

    private static final Logger logger =
            LoggerFactory.getLogger(FileDownloader.class);

    private static final String USER_AGENT =
            "Mozilla/5.0 (Windows NT 10.0; Win64; x64) "
                    + "AppleWebKit/537.36 (KHTML, like Gecko) "
                    + "Chrome/120.0.0.0 Safari/537.36";

    public Path download(
            String url,
            String fileName,
            @NotNull Path targetDir) {

        Path output = targetDir.resolve(fileName);
        Path temp = null;

        HttpURLConnection connection = null;

        try {
            Files.createDirectories(targetDir);

            connection = open(url, true);

            int status = connection.getResponseCode();
            String contentType = connection.getContentType();

            logger.info("Download HTTP status: {}", status);
            logger.info("Content-Type: {}", contentType);

            if (status != 200) {
                throw new IOException(
                        "Download failed: HTTP " + status
                );
            }

            if (contentType != null
                    && contentType.toLowerCase(Locale.ROOT)
                    .contains("text/html")) {
                throw new IOException(
                        "Получена HTML-страница вместо Excel-файла"
                );
            }

            temp = Files.createTempFile(
                    output.toAbsolutePath().getParent(),
                    ".download-",
                    ".tmp"
            );

            try (InputStream input = connection.getInputStream();
                 OutputStream outputStream =
                         Files.newOutputStream(temp)) {

                byte[] buffer = new byte[8192];
                int count;

                while ((count = input.read(buffer)) != -1) {
                    outputStream.write(buffer, 0, count);
                }
            }

            try (InputStream check = Files.newInputStream(temp)) {
                if (check.read() != 'P' || check.read() != 'K') {
                    throw new IOException(
                            "Сервер вернул файл, который не похож на XLSX"
                    );
                }
            }

            Files.move(
                    temp,
                    output,
                    StandardCopyOption.REPLACE_EXISTING
            );

            logger.info("File saved: {}", output.toAbsolutePath());
            logger.info("File size: {} bytes", Files.size(output));

            return output;

        } catch (IOException e) {
            logger.error("Download failed", e);
            throw new RuntimeException("Не удалось скачать файл", e);

        } finally {
            if (connection != null) {
                connection.disconnect();
            }

            if (temp != null) {
                try {
                    Files.deleteIfExists(temp);
                } catch (IOException e) {
                    logger.warn("Не удалось удалить временный файл: {}", temp, e);
                }
            }
        }
    }

    private @NotNull HttpURLConnection open(
            String url, boolean followRedirects) throws IOException {

        HttpURLConnection connection =
                (HttpURLConnection) new URL(url).openConnection();

        connection.setInstanceFollowRedirects(followRedirects);
        connection.setConnectTimeout(30_000);
        connection.setReadTimeout(180_000);
        connection.setRequestProperty("User-Agent", USER_AGENT);
        connection.setRequestProperty("Accept", "*/*");
        connection.setRequestProperty(
                "Accept-Language", "ru-RU,ru;q=0.9,en;q=0.8"
        );

        return connection;
    }
}
