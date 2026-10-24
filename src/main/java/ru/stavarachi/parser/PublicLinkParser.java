package ru.stavarachi.parser;

import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;

@Slf4j
public class PublicLinkParser {
    public String extractPublicKey(String viewUrl) {
        String encoded = extractParam(viewUrl, "url");

        if (encoded == null) {
            log.error("Missing url param");
        }

        String decoded = URLDecoder.decode(encoded, StandardCharsets.UTF_8);

        return decoded.replace("ya-disk-public://", "");
    }

    public String extractParam(@NotNull String url, String key) {
        String[] parts = url.split("\\?");

        if (parts.length < 2) return null;

        for (String part : parts[1].split("&")) {
            String[] keyValue = part.split("=");

            if (keyValue.length == 2 && keyValue[0].equals(key)) {
                return keyValue[1];
            }
        }
        return null;
    }
}
