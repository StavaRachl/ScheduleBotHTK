package ru.stavarachi.service;

import com.microsoft.playwright.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.stavarachi.model.Pair;
import ru.stavarachi.repository.ExcelRepository;
import ru.stavarachi.util.HtmlUtil;
import ru.stavarachi.util.TimeUtil;

import java.nio.file.Paths;
import java.util.List;

public class ScheduleService {
    private final Playwright playwright;
    private final Browser browser;
    private final String pathToSave = "src/main/resources/images/schedule.png";
    private static final Logger log = LoggerFactory.getLogger(ScheduleService.class);

    HtmlUtil htmlUtil = new HtmlUtil();
    TimeUtil timeUtil = new TimeUtil();
    ExcelRepository excelRepository = new ExcelRepository();
    ExcelService excelService = new ExcelService();

    public ScheduleService() {

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

    public String generateScheduleImage(String path, String group) {
        log.info("Start ScheduleService");
        String sheet = excelRepository.findTargetSheet(path, group);
        String day = timeUtil.getDayOfWeek();

        int row = excelRepository.findTargetDay(path, sheet, day);
        int col = excelRepository.findTargetGroup(path, sheet, group);

        List<Pair> listOfPairs = excelService.loadPair(path, sheet, day, group, row, col);
        List<Object> listOfPairsWithBreaks = excelService.loadPairWithBreaks(listOfPairs, day);

        String html = htmlUtil.generateHTML(listOfPairsWithBreaks);

        save(html, pathToSave);

        return pathToSave;
    }
}
