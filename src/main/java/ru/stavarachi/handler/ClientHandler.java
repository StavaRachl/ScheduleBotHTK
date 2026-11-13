
package ru.stavarachi.handler;

import lombok.extern.slf4j.Slf4j;
import ru.stavarachi.client.OneDriveClient;
import ru.stavarachi.config.StorageConfig;
import ru.stavarachi.infrastructure.downloader.FileDownloader;

import java.net.CookieHandler;
import java.net.CookieManager;
import java.net.CookiePolicy;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

@Slf4j
public class ClientHandler implements AutoCloseable {

    private static final String ONEDRIVE_URL =
            "https://1drv.ms/x/c/31511f965faf70b6/"
                    + "IQDyk8xKWRQKRbGz_6v0skl8AVb0i5u5S50vRRH7SabQ38g";

    private static final String FILE_NAME = "Книга.xlsx";

    private final OneDriveClient oneDriveClient;
    private final FileDownloader fileDownloader;

    private final ScheduledExecutorService scheduler =
            Executors.newSingleThreadScheduledExecutor();

    public ClientHandler() {
        CookieManager cookies = new CookieManager(
                null,
                CookiePolicy.ACCEPT_ALL
        );

        CookieHandler.setDefault(cookies);

        this.oneDriveClient = new OneDriveClient();
        this.fileDownloader = new FileDownloader();
    }

    public void start() {
        try {
            updateFile();
        } catch (Exception e) {
            log.error(
                    "Initial OneDrive update failed. Trying to use cached file.",
                    e
            );
        }

        scheduler.scheduleWithFixedDelay(
                this::updateSafely,
                15,
                15,
                TimeUnit.MINUTES
        );

        log.info("OneDrive updater started");
    }

    private void updateSafely() {
        try {
            updateFile();
        } catch (Exception e) {
            log.error("OneDrive scheduled update failed", e);
        }
    }

    private synchronized void updateFile() throws Exception {
        log.info("Starting OneDrive file update");

        String downloadUrl =
                oneDriveClient.getDownloadUrl(ONEDRIVE_URL);

        Path downloaded = fileDownloader.download(
                downloadUrl,
                FILE_NAME,
                StorageConfig.DOWNLOADS
        );

        if (!Files.exists(downloaded) || Files.size(downloaded) == 0) {
            throw new IllegalStateException("Downloaded file is empty");
        }

        log.info("OneDrive file updated: {}", downloaded);
    }

    public void getChange() {
        Path file = StorageConfig.DOWNLOADS.resolve(FILE_NAME);

        if (!Files.exists(file)) {
            throw new IllegalStateException(
                    "Schedule file does not exist: " + file
            );
        }
    }

    @Override
    public void close() {
        scheduler.shutdownNow();
    }
}
