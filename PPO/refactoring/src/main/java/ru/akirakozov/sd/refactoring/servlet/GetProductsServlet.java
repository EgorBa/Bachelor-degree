package ru.akirakozov.sd.refactoring.servlet;

import ru.akirakozov.sd.refactoring.utils.DatabaseUtils;
import ru.akirakozov.sd.refactoring.utils.ResultType;
import ru.akirakozov.sd.refactoring.utils.SQLUtils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author akirakozov
 */
public class GetProductsServlet extends Servlet {

    public GetProductsServlet(DatabaseUtils dataBaseUtils) {
        super(dataBaseUtils);
    }

    @Override
    public void doGet(HttpServletRequest request, HttpServletResponse response) {
        dataBaseUtils.requestToDB(response, SQLUtils.getAll(), null, ResultType.PRODUCT);
    }
}
