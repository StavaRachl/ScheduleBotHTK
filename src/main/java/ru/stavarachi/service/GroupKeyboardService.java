package ru.stavarachi.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;

import java.util.ArrayList;
import java.util.List;

public class GroupKeyboardService {
    private static final int BUTTONS_PER_ROW = 3;
    private static final int ROWS_PER_PAGE = 5;
    private static final int PAGE_SIZE = BUTTONS_PER_ROW * ROWS_PER_PAGE;

    private static final Logger log = LoggerFactory.getLogger(GroupKeyboardService.class);

    public InlineKeyboardMarkup buildKeyboardForGroup(String[] groups, int page) {
        String[] listOfGroup = groups;

        int totalPage = (int) Math.ceil((double) groups.length / PAGE_SIZE);

        int startPage = page * PAGE_SIZE;
        int end = Math.min(startPage + PAGE_SIZE, groups.length);

        List<List<InlineKeyboardButton>> listOfInlineKeyboardButton = new ArrayList<>();
        List<InlineKeyboardButton> row = new ArrayList<>();

        for (int i = startPage; i < end; i++) {
            InlineKeyboardButton button = new InlineKeyboardButton();
            button.setText(groups[i]);
            button.setCallbackData(groups[i]);

            row.add(button);

            if (row.size() == BUTTONS_PER_ROW) {
                listOfInlineKeyboardButton.add(row);
                row = new ArrayList<>();
            }
        }
        if (!row.isEmpty()) {
            listOfInlineKeyboardButton.add(row);
        }
        List<InlineKeyboardButton> inlineKeyboardButtonList = new ArrayList<>();

        int prevPage = Math.max(page - 1, 0);
        int nextPage = Math.min(page + 1, totalPage - 1);

        InlineKeyboardButton prevButton = new InlineKeyboardButton();
        prevButton.setText("⬅️");
        prevButton.setCallbackData("group_page:" + prevPage);

        InlineKeyboardButton pageButton = new InlineKeyboardButton();
        pageButton.setText((page + 1) + "/" + totalPage);
        pageButton.setCallbackData("noop");

        InlineKeyboardButton nextButton = new InlineKeyboardButton();
        nextButton.setText("➡️");
        nextButton.setCallbackData("group_page:" + nextPage);

        inlineKeyboardButtonList.add(prevButton);
        inlineKeyboardButtonList.add(pageButton);
        inlineKeyboardButtonList.add(nextButton);

        listOfInlineKeyboardButton.add(inlineKeyboardButtonList);

        InlineKeyboardMarkup inlineKeyboardMarkup = new InlineKeyboardMarkup();
        inlineKeyboardMarkup.setKeyboard(listOfInlineKeyboardButton);

        return inlineKeyboardMarkup;
    }
}
