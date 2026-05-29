package ru.stavarachi.handler;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.stavarachi.client.YandexDiskClient;
import ru.stavarachi.downloader.FileDownloader;
import ru.stavarachi.parser.PublicLinkParser;
import ru.stavarachi.service.FileDownloadService;

import java.net.http.HttpClient;
import java.nio.file.Path;

public class ClientHandler {
    private final Logger log = LoggerFactory.getLogger(ClientHandler.class);

    public void getChange() throws Exception {
        HttpClient client = HttpClient.newBuilder()
                .followRedirects(HttpClient.Redirect.ALWAYS)
                .build();

        FileDownloadService service = new FileDownloadService(
                new PublicLinkParser(),
                new YandexDiskClient(client),
                new FileDownloader(client)
        );

        String url = "https://docs.yandex.ru/docs/view?url=ya-disk-public%3A%2F%2F6%2F9ALAKFdkSkkpd4OGR6si%2BQt6ofAx4265zSd2fuUCb3K2yBSDEZ%2F4Ef13a8ampdq%2FJ6bpmRyOJonT3VoXnDag%3D%3D&name=%D0%9D%D0%BE%D0%B2%D0%B0%D1%8F%20%D1%82%D0%B0%D0%B1%D0%BB%D0%B8%D1%86%D0%B0%20.xlsx";
        Path result = service.download(url, "src/main/resources");

        log.info("Info Success saved");
    }
}
