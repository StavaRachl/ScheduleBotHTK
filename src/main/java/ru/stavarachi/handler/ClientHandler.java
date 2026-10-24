
package ru.stavarachi.handler;

import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.stavarachi.client.OneDriveClient;
import ru.stavarachi.config.StorageConfig;
import ru.stavarachi.downloader.FileDownloader;

import java.net.CookieHandler;
import java.net.CookieManager;
import java.net.CookiePolicy;
import java.nio.file.Path;

@Slf4j
public class ClientHandler {

    private final OneDriveClient oneDriveClient;
    private final FileDownloader fileDownloader;

    private static final String ONEDRIVE_URL =
            "https://1drv.ms/x/c/31511f965faf70b6/"
                    + "IQDyk8xKWRQKRbGz_6v0skl8AVb0i5u5S50vRRH7SabQ38g";

    public ClientHandler() {
        CookieManager cookies = new CookieManager(
                null,
                CookiePolicy.ACCEPT_ALL
        );

        CookieHandler.setDefault(cookies);

        this.oneDriveClient = new OneDriveClient();
        this.fileDownloader = new FileDownloader();
    }

    public void getChange() throws Exception {
        log.info("Starting OneDrive download");

        String downloadUrl =
                oneDriveClient.getDownloadUrl(ONEDRIVE_URL);

        log.info("Resolved download URL: {}", downloadUrl);

        Path result = fileDownloader.download(
                downloadUrl,
                "Книга.xlsx",
                StorageConfig.DOWNLOADS
        );

        log.info(
                "Successfully saved: {}",
                result.toAbsolutePath()
        );
    }
}
