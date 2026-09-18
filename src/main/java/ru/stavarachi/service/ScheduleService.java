package ru.stavarachi.service;

import com.microsoft.playwright.*;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.stavarachi.config.PathConfig;
import ru.stavarachi.config.ScheduleConfig;
import ru.stavarachi.config.StorageConfig;
import ru.stavarachi.excel.PoiChangeReader;
import ru.stavarachi.excel.PoiScheduleReader;
import ru.stavarachi.handler.ClientHandler;
import ru.stavarachi.model.Change;
import ru.stavarachi.model.Pair;
import ru.stavarachi.model.User;
import ru.stavarachi.util.HtmlDarkThemeUtil;
import ru.stavarachi.util.HtmlUtil;

import java.nio.file.Path;
import java.util.List;

public class ScheduleService {
    private static final Logger log = LoggerFactory.getLogger(ScheduleService.class);

    private final ClientHandler clientHandler;
    private final Playwright playwright;
    private final Browser browser;
    private final PathConfig pathConfig;
    private final ScheduleConfig scheduleConfig;
    private final HtmlUtil htmlUtil;
    private final HtmlDarkThemeUtil htmlDarkThemeUtil;
    private final PoiChangeReader poiChangeReader;
    private final PoiScheduleReader poiScheduleReader;
    private final ExcelService excelService;
    private final ExcelChangeService excelChangeService;

    public ScheduleService(ClientHandler clientHandler, PathConfig pathConfig, ScheduleConfig scheduleConfig, HtmlUtil htmlUtil, HtmlDarkThemeUtil htmlDarkThemeUtil, PoiChangeReader poiChangeReader, PoiScheduleReader poiScheduleReader, ExcelService excelService, ExcelChangeService excelChangeService) {
        this.clientHandler = clientHandler;
        this.htmlDarkThemeUtil = htmlDarkThemeUtil;
        this.htmlUtil = htmlUtil;
        this.pathConfig = pathConfig;
        this.poiChangeReader = poiChangeReader;
        this.poiScheduleReader = poiScheduleReader;
        this.excelService = excelService;
        this.excelChangeService = excelChangeService;
        this.scheduleConfig = scheduleConfig;
        this.playwright = Playwright.create();
        this.browser = playwright.chromium().launch(
                new BrowserType.LaunchOptions()
                        .setHeadless(true)
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
        //static schedule
        String sheet = poiScheduleReader.findTargetSheet(group);
        int row = poiScheduleReader.findTargetDay(sheet, day);
        int col = poiScheduleReader.findTargetGroup(sheet, group);

        List<Pair> listOfPairs = excelService.loadPair(excelPath, sheet, day, group, month, row, col);

        //change schedule
        clientHandler.getChange();

        int pairsChangeVariable = poiChangeReader.getChangeVariable(scheduleConfig.getPAIRS_CHANGE());
        int classroomChangeVariable = poiChangeReader.getChangeVariable(scheduleConfig.getCLASSROOM_CHANGE());

        int startRowPairsChange = poiChangeReader.getTargetGroup(group, pairsChangeVariable);
        int startRowClassroomChange = poiChangeReader.getTargetGroup(group, classroomChangeVariable);

        //schedule lists
        List<Change> listOfChangePairs = excelChangeService.getChangeOfPair(group, scheduleConfig.getPAIRS_CHANGE(), startRowPairsChange);
        List<Change> listOfChangeClassroom = excelChangeService.getChangeOfPair(group, scheduleConfig.getCLASSROOM_CHANGE(), startRowClassroomChange);
        List<Object> listOfPairsWithBreaks = excelService.loadPairWithBreaks(listOfPairs, day);

        String html;

        if (user.isDarkTheme()) {
            html = htmlDarkThemeUtil.generateHTML(listOfPairsWithBreaks, listOfChangePairs, listOfChangeClassroom);
        } else {
            html = htmlUtil.generateHTML(listOfPairsWithBreaks, listOfChangePairs, listOfChangeClassroom);
        }

        save(html, pathToSave);
        log.info("ScheduleService complete work");
        return pathToSave;
    }
}
