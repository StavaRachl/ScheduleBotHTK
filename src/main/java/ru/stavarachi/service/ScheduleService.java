package ru.stavarachi.service;

import com.microsoft.playwright.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.stavarachi.config.PathConfig;
import ru.stavarachi.model.Pair;
import ru.stavarachi.model.User;
import ru.stavarachi.repository.ExcelRepository;
import ru.stavarachi.util.HtmlDarkThemeUtil;
import ru.stavarachi.util.HtmlUtil;

import java.nio.file.Paths;
import java.util.List;

public class ScheduleService {
    private static final Logger log = LoggerFactory.getLogger(ScheduleService.class);

    private final Playwright playwright;
    private final Browser browser;
    private final PathConfig pathConfig;
    private final HtmlUtil htmlUtil;
    private final HtmlDarkThemeUtil htmlDarkThemeUtil;
    private final ExcelRepository excelRepository;
    private final ExcelService excelService;

    public ScheduleService(PathConfig pathConfig, HtmlUtil htmlUtil, HtmlDarkThemeUtil htmlDarkThemeUtil, ExcelService excelService, ExcelRepository excelRepository) {
        this.htmlDarkThemeUtil = htmlDarkThemeUtil;
        this.htmlUtil = htmlUtil;
        this.pathConfig = pathConfig;
        this.excelService = excelService;
        this.excelRepository = excelRepository;
        this.playwright = Playwright.create();
        this.browser = playwright.chromium().launch(
                new BrowserType.LaunchOptions()
                        .setHeadless(true)
        );
    }

    public void save(String html, String path) {

        Page page = null;

        try {

            page = browser.newPage();

            page.setContent(html);

            Locator card = page.locator(".card");

            card.screenshot(
                    new Locator.ScreenshotOptions()
                            .setPath(Paths.get(path))
            );

        } catch (Exception e) {

            log.error("Error while saving schedule image", e);

        } finally {

            if (page != null) {
                page.close();
            }
        }
    }

    public String generateScheduleImage(String path, String group, String day, User user) {
        log.info("Start ScheduleService");

        String pathToSave = pathConfig.getPathToSave();

        String sheet = excelRepository.findTargetSheet(group);
        int row = excelRepository.findTargetDay(sheet, day);
        int col = excelRepository.findTargetGroup(sheet, group);

        List<Pair> listOfPairs = excelService.loadPair(path, sheet, day, group, row, col);
        List<Object> listOfPairsWithBreaks = excelService.loadPairWithBreaks(listOfPairs, day);

        String html;

        if (user.isDarkTheme()) {
            html = htmlDarkThemeUtil.generateHTML(listOfPairsWithBreaks);
        } else {
            html = htmlUtil.generateHTML(listOfPairsWithBreaks);
        }

        save(html, pathToSave);
        log.info("ScheduleService complete work");
        return pathToSave;
    }
}
