package ru.stavarachi.client;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.URI;
import java.net.URLEncoder;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;

public class YandexDiskClient {
    private final HttpClient client;
    private final ObjectMapper mapper = new ObjectMapper();
    private static final Logger logger = LoggerFactory.getLogger(YandexDiskClient.class);
    public YandexDiskClient(HttpClient client) {
        this.client = client;
    }

    public JsonNode getPublicMeta(String publicKey){
        String url =
                "https://cloud-api.yandex.net/v1/disk/public/resources?public_key="
                        + URLEncoder.encode(publicKey, StandardCharsets.UTF_8);

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .GET()
                .build();

        HttpResponse<String> response = null;
        try {
            response = client.send(request, HttpResponse.BodyHandlers.ofString());
        } catch (IOException | InterruptedException e) {
            logger.error("Error: ", e);
        }

        if (response.statusCode() != 200) {
            logger.error("Yandex api error: {}", response.body());
        }

        try {
            return mapper.readTree(response.body());
        } catch (JsonProcessingException e) {
            logger.error("Error: ", e);
        }
        return null;
    }
}
