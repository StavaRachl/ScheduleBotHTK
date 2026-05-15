package ru.stavarachi.util;

import ru.stavarachi.model.Break;
import ru.stavarachi.model.Pair;

import java.util.List;

public class HtmlDarkThemeUtil {
    public String generateHTML(List<Object> pairList) {
        StringBuilder html = new StringBuilder("""
                <html>
                        <head>
                        <style>
                            table {
                                border-collapse: collapse;
                                font-family: Arial;
                                width: 100%;
                            }
                            th, td {
                                border: 1px solid #3B3B3B;
                            }
                            th {
                                background-color: #0D0D0D;
                                color: #FFFFFF
                            }
                            tr:nth-child(even) { background-color: #252525; color: #FFFFFF}
                        </style>
                        </head>
                        <body>
                        <div class="card">
                            <table>
                        </div>
                        <tr><th style='padding: 13px 0;'>Номер пары</th><th style='padding: 13px 0;'>Пара</th><th style='padding: 13px 0;'>Время</th></tr>
                """);
        for (Object objects : pairList) {
            if (objects instanceof Pair pair) {
                html.append("<tr style='text-align:center'>")
                        .append("<td style='padding: 20px 0;'>").append(pair.getCount()).append("</td>")
                        .append("<td style='padding: 20px 0;'>").append(pair.getPair()).append("</td>")
                        .append("<td style='padding: 20px 0;'>").append(pair.getTime()).append("</td>")
                        .append("</tr>");
            } else if (objects instanceof Break breaks) {
                html.append("<tr style='background-color:#05A300; color: #FFFFFF; text-align:center'>")
                        .append("<td style='padding: 8px 0;'></td>")
                        .append("<td style='padding: 8px 0;'>").append(breaks.getBreaks()).append("</td>")
                        .append("<td style='padding: 8px 0;'>").append(breaks.getTime()).append("</td>")
                        .append("</tr>");
            }
        }

        html.append("</table></body></html>");

        return html.toString();
    }
}

