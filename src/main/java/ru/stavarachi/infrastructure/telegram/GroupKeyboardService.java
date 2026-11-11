package ru.stavarachi.infrastructure.telegram;

import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;

import java.util.ArrayList;
import java.util.List;

@Slf4j
public class GroupKeyboardService {
    private static final int BUTTONS_PER_ROW = 3;
    private static final int ROWS_PER_PAGE = 5;
    private static final int PAGE_SIZE = BUTTONS_PER_ROW * ROWS_PER_PAGE;

    public InlineKeyboardMarkup buildKeyboardForGroup(String @NotNull [] groups, int page) {
        String[] listOfGroup = groups;

        int totalPage = (int) Math.ceil((double) groups.length / PAGE_SIZE);

        int startPage = page * PAGE_SIZE;
        int end = Math.min(startPage + PAGE_SIZE, groups.length);

        List<List<InlineKeyboardButton>> listOfInlineKeyboardButton = new ArrayList<>();
        List<InlineKeyboardButton> row = new ArrayList<>();

        for (int i = startPage; i < end; i++) {
            InlineKeyboardButton button = InlineKeyboardButton.builder()
                    .text(groups[i])
                    .callbackData("group:" + groups[i])
                    .build();

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

        InlineKeyboardButton prevButton = InlineKeyboardButton.builder()
                .text("⬅️")
                .callbackData("group_page:" + prevPage)
                .build();

        InlineKeyboardButton pageButton = InlineKeyboardButton.builder()
                .text((page + 1) + "/" + totalPage)
                .callbackData("noop")
                .build();

        InlineKeyboardButton nextButton = InlineKeyboardButton.builder()
                .text("➡️")
                .callbackData("group_page:" + nextPage)
                .build();

        inlineKeyboardButtonList.add(prevButton);
        inlineKeyboardButtonList.add(pageButton);
        inlineKeyboardButtonList.add(nextButton);
        listOfInlineKeyboardButton.add(inlineKeyboardButtonList);

        return InlineKeyboardMarkup.builder()
                .keyboard(listOfInlineKeyboardButton)
                .build();
    }
}
