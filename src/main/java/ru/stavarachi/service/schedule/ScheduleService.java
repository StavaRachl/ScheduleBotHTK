package ru.stavarachi.service.schedule;

import com.microsoft.playwright.*;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.stavarachi.config.PathConfig;
import ru.stavarachi.config.StorageConfig;
import ru.stavarachi.domain.user.User;
import ru.stavarachi.util.HtmlDarkThemeUtil;
import ru.stavarachi.util.HtmlUtil;

import java.nio.file.Path;

public class ScheduleService {
    private static final Logger log = LoggerFactory.getLogger(ScheduleService.class);

    private final Playwright playwright;
    private final Browser browser;
    private final PathConfig pathConfig;
    private final ScheduleDataService scheduleDataService;
    private final HtmlUtil htmlUtil;
    private final HtmlDarkThemeUtil htmlDarkThemeUtil;

    public ScheduleService(PathConfig pathConfig, ScheduleDataService scheduleDataService, HtmlUtil htmlUtil, HtmlDarkThemeUtil htmlDarkThemeUtil) {
        this.pathConfig = pathConfig;
        this.scheduleDataService = scheduleDataService;
        this.htmlUtil = htmlUtil;
        this.htmlDarkThemeUtil = htmlDarkThemeUtil;
        this.playwright = Playwright.create();
        this.browser = playwright.chromium().launch(
                new BrowserType.LaunchOptions().setHeadless(true)
        );
    }

    public void save(String html, Path path) {

        Page page = null;

        try {

            page = browser.newPage();

            page.setContent(html);

            Locator card = page.locator(".card");

            card.screenshot(
                    new Locator.ScreenshotOptions()
                            .setPath(path)
            );

        } catch (Exception e) {

            log.error("Error while saving schedule image", e);

        } finally {

            if (page != null) {
                page.close();
            }
        }
    }

    public Path generateScheduleImage(Path excelPath, String group, String day, @NotNull User user, String month) throws Exception {
        log.info("Start ScheduleService");

        Path pathToSave = StorageConfig.scheduleImage();

        ScheduleData data = scheduleDataService.loadScheduleData(excelPath, group, day, month);

        String html;

        if (user.isDarkTheme()) {
            html = htmlDarkThemeUtil.generateHTML(
                    data.pairsWithBreaks(),
                    data.changePair(),
                    data.changeClassroom()
            );
        } else {
            html = htmlUtil.generateHTML(
                    data.pairsWithBreaks(),
                    data.changePair(),
                    data.changeClassroom()
            );
        }

        save(html, pathToSave);

        log.info("ScheduleService complete work");

        return pathToSave;
    }
}
