package ru.akirakozov.sd.refactoring.utils;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.ResultSet;
import java.sql.SQLException;

import static ru.akirakozov.sd.refactoring.resources.Resources.NAME;
import static ru.akirakozov.sd.refactoring.resources.Resources.PRICE;

public class HTMLUtils {

    private static final String HTML_BODY_OPEN = "<html><body>";
    private static final String HTML_BODY_CLOSE = "</body></html>";
    private static final String BR = "</br>";
    private static final String HTML_TEXT = "text/html";

    public static void generateHTMLAnswer(ResultSet resultSet,
                                          HttpServletResponse response,
                                          String title,
                                          ResultType resultType) {
        response.setContentType(HTML_TEXT);
        try (PrintWriter out = response.getWriter()) {
            out.println(HTML_BODY_OPEN);
            if (title != null) {
                out.println(title);
            }
            while (resultSet != null && resultSet.next()) {
                switch (resultType) {
                    case PRODUCT:
                        String name = resultSet.getString(NAME);
                        int price = resultSet.getInt(PRICE);
                        out.println(name + "\t" + price + BR);
                        break;
                    case NUMBER:
                        out.println(resultSet.getInt(1));
                        break;
                    case EMPTY:
                        break;
                    default:
                        System.err.println("Unknown resultType " + resultType);
                        return;
                }
            }
            out.println(HTML_BODY_CLOSE);
        } catch (SQLException e) {
            System.err.println("Can't interpret result");
            e.printStackTrace();
        } catch (IOException e) {
            System.err.println("Can't show result");
            e.printStackTrace();
        }
    }
}
