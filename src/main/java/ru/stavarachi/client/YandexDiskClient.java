package ru.stavarachi.client;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.net.URI;
import java.net.URLEncoder;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;

public class YandexDiskClient {
    private final HttpClient client;
    private final ObjectMapper mapper = new ObjectMapper();

    public YandexDiskClient(HttpClient client) {
        this.client = client;
    }

    public JsonNode getPublicMeta(String publicKey) throws Exception {
        String url =
                "https://cloud-api.yandex.net/v1/disk/public/resources?public_key="
                        + URLEncoder.encode(publicKey, StandardCharsets.UTF_8);

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .GET()
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        if (response.statusCode() != 200) {
            throw new RuntimeException("Yandex api error: " + response.body());
        }

        return mapper.readTree(response.body());
    }
}
