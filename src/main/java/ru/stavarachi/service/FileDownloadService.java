package ru.stavarachi.service;

import com.fasterxml.jackson.databind.JsonNode;
import ru.stavarachi.client.YandexDiskClient;
import ru.stavarachi.downloader.FileDownloader;
import ru.stavarachi.parser.PublicLinkParser;

import java.nio.file.Path;

public class FileDownloadService {
    private final PublicLinkParser parser;
    private final YandexDiskClient client;
    private final FileDownloader downloader;

    public FileDownloadService(PublicLinkParser parser, YandexDiskClient client, FileDownloader downloader) {
        this.parser = parser;
        this.client = client;
        this.downloader = downloader;
    }

    public Path download(String viewUrl, String targetDir) throws Exception {
        String publicKey = parser.extractPublicKey(viewUrl);
        JsonNode meta = client.getPublicMeta(publicKey);

        String fileUrl = meta.get("file").asText();
        String fileName = meta.has("name") ? meta.get("name").asText() : "file.xlsx";

        return downloader.download(fileUrl, fileName, targetDir);
    }
}
