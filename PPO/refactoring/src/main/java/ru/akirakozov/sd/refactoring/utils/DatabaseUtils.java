package ru.akirakozov.sd.refactoring.utils;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.*;

import static ru.akirakozov.sd.refactoring.resources.Resources.*;
import static ru.akirakozov.sd.refactoring.utils.HTMLUtils.generateHTMLAnswer;

public class DatabaseUtils {

    private String database;

    public DatabaseUtils(String database){
        this.database = database;
    }

    public void requestToDB(HttpServletResponse response,
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
            generateHTMLAnswer(resultSet, response, title, resultType);
            closeResultSet(resultSet);
            setOKStatus(response);
        } catch (SQLException e) {
            System.err.println("Can't execute SQL command : " + SQLCommand);
            e.printStackTrace();
        }
    }

    public void showUnknownCommand(HttpServletResponse response, String command) {
        try (PrintWriter out = response.getWriter()) {
            out.println(UNKNOWN_COMMAND + command);
        } catch (IOException e) {
            System.err.println("Can't show result");
            e.printStackTrace();
            return;
        }
        setOKStatus(response);
    }

    private Connection getConnection() {
        try {
            return DriverManager.getConnection(database);
        } catch (SQLException e) {
            System.err.println("Can't open Database : " + DATABASE);
            e.printStackTrace();
            return null;
        }
    }

    private void setOKStatus(HttpServletResponse response) {
        response.setStatus(HttpServletResponse.SC_OK);
    }

    private void closeResultSet(ResultSet resultSet) {
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
