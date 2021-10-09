package ru.akirakozov.sd.refactoring.servlet;

import ru.akirakozov.sd.refactoring.utils.DatabaseUtils;
import ru.akirakozov.sd.refactoring.utils.ResultType;
import ru.akirakozov.sd.refactoring.utils.SQLUtils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import static ru.akirakozov.sd.refactoring.resources.Resources.*;

/**
 * @author akirakozov
 */
public class AddProductServlet extends Servlet {

    public AddProductServlet(DatabaseUtils dataBaseUtils) {
        super(dataBaseUtils);
    }

    @Override
    public void doGet(HttpServletRequest request, HttpServletResponse response) {
        String name = request.getParameter(NAME);
        long price = Long.parseLong(request.getParameter(PRICE));
        dataBaseUtils.requestToDB(response, SQLUtils.addProduct(name, price), OK, ResultType.EMPTY);
    }
}
