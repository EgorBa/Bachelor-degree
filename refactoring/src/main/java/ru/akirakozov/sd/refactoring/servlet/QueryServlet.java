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
public class QueryServlet extends Servlet {

    public QueryServlet(DatabaseUtils dataBaseUtils) {
        super(dataBaseUtils);
    }

    @Override
    public void doGet(HttpServletRequest request, HttpServletResponse response) {
        String command = request.getParameter(COMMAND);

        switch (command) {
            case MAX:
                dataBaseUtils.requestToDB(response,
                        SQLUtils.getExpensiveProduct(),
                        MAX_TITLE,
                        ResultType.PRODUCT);
                break;
            case MIN:
                dataBaseUtils.requestToDB(response,
                        SQLUtils.getChipProduct(),
                        MIN_TITLE,
                        ResultType.PRODUCT);
                break;
            case SUM:
                dataBaseUtils.requestToDB(response,
                        SQLUtils.getSum(),
                        SUM_TITLE,
                        ResultType.NUMBER);
                break;
            case COUNT:
                dataBaseUtils.requestToDB(response,
                        SQLUtils.getCount(),
                        COUNT_TITLE,
                        ResultType.NUMBER);
                break;
            default:
                dataBaseUtils.showUnknownCommand(response, command);
        }
    }
}
