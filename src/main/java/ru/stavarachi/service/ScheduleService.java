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
import ru.stavarachi.repository.ExcelChangeRepository;
import ru.stavarachi.repository.ExcelRepository;
import ru.stavarachi.util.HtmlDarkThemeUtil;
import ru.stavarachi.util.HtmlUtil;

import java.net.URL;
import java.nio.file.Path;
import java.nio.file.Paths;
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
    private final ExcelRepository excelRepository;
    private final ExcelChangeRepository excelChangeRepository;
    private final ExcelService excelService;
    private final ExcelChangeService excelChangeService;

    public ScheduleService(ClientHandler clientHandler, PathConfig pathConfig, ScheduleConfig scheduleConfig, HtmlUtil htmlUtil, HtmlDarkThemeUtil htmlDarkThemeUtil, ExcelService excelService, ExcelRepository excelRepository, ExcelChangeRepository excelChangeRepository, ExcelChangeService excelChangeService) {
        this.clientHandler = clientHandler;
        this.htmlDarkThemeUtil = htmlDarkThemeUtil;
        this.htmlUtil = htmlUtil;
        this.pathConfig = pathConfig;
        this.excelService = excelService;
        this.excelRepository = excelRepository;
        this.excelChangeRepository = excelChangeRepository;
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

        String sheet = excelRepository.findTargetSheet(group);
        int row = excelRepository.findTargetDay(sheet, day);
        int col = excelRepository.findTargetGroup(sheet, group);

        clientHandler.getChange();

        int pairsChangeVariable = excelChangeRepository.getChangeVariable(scheduleConfig.getPAIRS_CHANGE());
        int classroomChangeVariable = excelChangeRepository.getChangeVariable(scheduleConfig.getCLASSROOM_CHANGE());

        int startRowPairsChange = excelChangeRepository.getTargetGroup(group, pairsChangeVariable);
        int startRowClassroomChange = excelChangeRepository.getTargetGroup(group, classroomChangeVariable);

        List<Pair> listOfPairs = excelService.loadPair(excelPath, sheet, day, group, month, row, col);

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
