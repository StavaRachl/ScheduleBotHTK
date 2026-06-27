package ru.stavarachi.service;

import com.microsoft.playwright.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.stavarachi.config.PathConfig;
import ru.stavarachi.config.ScheduleConfig;
import ru.stavarachi.config.StorageConfig;
import ru.stavarachi.handler.ClientHandler;
import ru.stavarachi.model.Change;
import ru.stavarachi.model.Pair;
import ru.stavarachi.model.User;
import ru.stavarachi.repository.ExcelChangeRepositoryImpl;
import ru.stavarachi.repository.ExcelRepositoryImpl;
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
    private final ExcelRepositoryImpl excelRepositoryImpl;
    private final ExcelChangeRepositoryImpl excelChangeRepositoryImpl;
    private final ExcelService excelService;
    private final ExcelChangeService excelChangeService;

    public ScheduleService(ClientHandler clientHandler, PathConfig pathConfig, ScheduleConfig scheduleConfig, HtmlUtil htmlUtil, HtmlDarkThemeUtil htmlDarkThemeUtil, ExcelService excelService, ExcelRepositoryImpl excelRepositoryImpl, ExcelChangeRepositoryImpl excelChangeRepositoryImpl, ExcelChangeService excelChangeService) {
        this.clientHandler = clientHandler;
        this.htmlDarkThemeUtil = htmlDarkThemeUtil;
        this.htmlUtil = htmlUtil;
        this.pathConfig = pathConfig;
        this.excelService = excelService;
        this.excelRepositoryImpl = excelRepositoryImpl;
        this.excelChangeRepositoryImpl = excelChangeRepositoryImpl;
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

    public Path generateScheduleImage(Path excelPath, String group, String day, User user, String month) throws Exception {
        log.info("Start ScheduleService");

        Path pathToSave = StorageConfig.scheduleImage();
        //static schedule
        String sheet = excelRepositoryImpl.findTargetSheet(group);
        int row = excelRepositoryImpl.findTargetDay(sheet, day);
        int col = excelRepositoryImpl.findTargetGroup(sheet, group);

        List<Pair> listOfPairs = excelService.loadPair(excelPath, sheet, day, group, month, row, col);

        //change schedule
        clientHandler.getChange();

        int pairsChangeVariable = excelChangeRepositoryImpl.getChangeVariable(scheduleConfig.getPAIRS_CHANGE());
        int classroomChangeVariable = excelChangeRepositoryImpl.getChangeVariable(scheduleConfig.getCLASSROOM_CHANGE());

        int startRowPairsChange = excelChangeRepositoryImpl.getTargetGroup(group, pairsChangeVariable);
        int startRowClassroomChange = excelChangeRepositoryImpl.getTargetGroup(group, classroomChangeVariable);

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
