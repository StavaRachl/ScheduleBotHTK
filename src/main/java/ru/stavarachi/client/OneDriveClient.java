
package ru.stavarachi.client;

import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URL;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class OneDriveClient {

    private static final Logger logger =
            LoggerFactory.getLogger(OneDriveClient.class);

    private static final String USER_AGENT =
            "Mozilla/5.0 (Windows NT 10.0; Win64; x64) "
                    + "AppleWebKit/537.36 (KHTML, like Gecko) "
                    + "Chrome/120.0.0.0 Safari/537.36";

    public String getDownloadUrl(String shortUrl) throws IOException {
        List<String> chain = new ArrayList<>();
        String current = shortUrl;

        for (int i = 0; i < 20; i++) {
            chain.add(current);

            HttpURLConnection connection = open(current, false);
            int status;
            String location;

            try {
                status = connection.getResponseCode();
                location = connection.getHeaderField("Location");
                logger.info("{} {}", status, current);
            } finally {
                connection.disconnect();
            }

            if (status >= 300 && status < 400
                    && location != null) {
                current = URI.create(current)
                        .resolve(location)
                        .toString();
            } else {
                break;
            }
        }

        String shareUrl = null;

        for (String url : chain) {
            if (url.toLowerCase(Locale.ROOT)
                    .contains("onedrive.live.com")
                    && url.contains(":x:")) {
                shareUrl = url;
                break;
            }
        }

        if (shareUrl == null) {
            throw new IllegalStateException(
                    "Не найдена ссылка OneDrive вида :x:/g/personal/..."
            );
        }

        String downloadUrl = shareUrl
                + (shareUrl.contains("?") ? "&" : "?")
                + "download=1";

        logger.info("Download URL: {}", downloadUrl);
        return downloadUrl;
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
