package ru.akirakozov.sd.refactoring.servlet;

import ru.akirakozov.sd.refactoring.utils.DataBaseUtils;
import ru.akirakozov.sd.refactoring.utils.ResultType;
import ru.akirakozov.sd.refactoring.utils.SQLUtils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;

/**
 * @author akirakozov
 */
public class AddProductServlet extends ServletInterface {

    @Override
    public void doGet(HttpServletRequest request, HttpServletResponse response) {
        String name = request.getParameter("name");
        long price = Long.parseLong(request.getParameter("price"));
        DataBaseUtils.requestToDB(response, SQLUtils.addProduct(name, price), "OK", ResultType.EMPTY);
    }
}
