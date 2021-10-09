package ru.akirakozov.sd.refactoring.utils;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.*;

import static ru.akirakozov.sd.refactoring.resources.Resources.*;

public class DataBaseUtils {

    private static final String HTML_BODY_OPEN = "<html><body>";
    private static final String HTML_BODY_CLOSE = "</body></html>";
    private static final String BR = "</br>";
    private static final String HTML_TEXT = "text/html";

    public static void requestToDB(HttpServletResponse response,
                                   String SQLCommand,
                                   String title,
                                   ResultType resultType) {
        Connection c = getConnection();
        if (c == null) return;
        try (Statement stmt = c.createStatement()) {
            ResultSet resultSet = null;
            if (resultType == ResultType.EMPTY) {
                stmt.executeUpdate(SQLCommand);
            } else {
                resultSet = stmt.executeQuery(SQLCommand);
            }
            if (response == null) return;
            presentResult(resultSet, response, title, resultType);
            closeResultSet(resultSet);
            setOKStatus(response);
        } catch (SQLException e) {
            System.err.println("Can't execute SQL command : " + SQLCommand);
            e.printStackTrace();
        }
    }

    public static void showUnknownCommand(HttpServletResponse response, String command) {
        try (PrintWriter out = response.getWriter()) {
            out.println(UNKNOWN_COMMAND + command);
        } catch (IOException e) {
            System.err.println("Can't show result");
            e.printStackTrace();
            return;
        }
        setOKStatus(response);
    }

    private static Connection getConnection() {
        try {
            return DriverManager.getConnection(DATABASE);
        } catch (SQLException e) {
            System.err.println("Can't open Database : " + DATABASE);
            e.printStackTrace();
            return null;
        }
    }

    private static void presentResult(ResultSet resultSet,
                                      HttpServletResponse response,
                                      String title,
                                      ResultType resultType) {
        try {
            PrintWriter out = response.getWriter();
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
                        System.err.println("Unknown resultType");
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

    private static void setOKStatus(HttpServletResponse response) {
        response.setContentType(HTML_TEXT);
        response.setStatus(HttpServletResponse.SC_OK);
    }

    private static void closeResultSet(ResultSet resultSet) {
        if (resultSet != null) {
            try {
                resultSet.close();
            } catch (SQLException e) {
                System.err.println("Can't close ResultSet");
                e.printStackTrace();
            }
        }
    }

}
