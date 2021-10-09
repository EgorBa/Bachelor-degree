package ru.akirakozov.sd.refactoring.servlet;

import ru.akirakozov.sd.refactoring.utils.DataBaseUtils;
import ru.akirakozov.sd.refactoring.utils.ResultType;
import ru.akirakozov.sd.refactoring.utils.SQLUtils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;

/**
 * @author akirakozov
 */
public class QueryServlet extends ServletInterface {
    @Override
    public void doGet(HttpServletRequest request, HttpServletResponse response) {
        String command = request.getParameter("command");

        switch (command) {
            case "max":
                DataBaseUtils.requestToDB(response,
                        SQLUtils.getExpensiveProduct(),
                        "<h1>Product with max price: </h1>",
                        ResultType.PRODUCT);
                break;
            case "min":
                DataBaseUtils.requestToDB(response,
                        SQLUtils.getChipProduct(),
                        "<h1>Product with min price: </h1>",
                        ResultType.PRODUCT);
                break;
            case "sum":
                DataBaseUtils.requestToDB(response,
                        SQLUtils.getSum(),
                        "Summary price: ",
                        ResultType.NUMBER);
                break;
            case "count":
                DataBaseUtils.requestToDB(response,
                        SQLUtils.getCount(),
                        "Number of products: ",
                        ResultType.NUMBER);
                break;
            default:
                DataBaseUtils.showUnknownCommand(response, command);
        }
    }
}
