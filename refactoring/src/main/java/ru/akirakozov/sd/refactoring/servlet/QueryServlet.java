package ru.akirakozov.sd.refactoring.servlet;

import ru.akirakozov.sd.refactoring.utils.DataBaseUtils;
import ru.akirakozov.sd.refactoring.utils.ResultType;
import ru.akirakozov.sd.refactoring.utils.SQLUtils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import static ru.akirakozov.sd.refactoring.resources.Resources.*;

/**
 * @author akirakozov
 */
public class QueryServlet extends Servlet {
    @Override
    public void doGet(HttpServletRequest request, HttpServletResponse response) {
        String command = request.getParameter(COMMAND);

        switch (command) {
            case MAX:
                DataBaseUtils.requestToDB(response,
                        SQLUtils.getExpensiveProduct(),
                        MAX_TITLE,
                        ResultType.PRODUCT);
                break;
            case MIN:
                DataBaseUtils.requestToDB(response,
                        SQLUtils.getChipProduct(),
                        MIN_TITLE,
                        ResultType.PRODUCT);
                break;
            case SUM:
                DataBaseUtils.requestToDB(response,
                        SQLUtils.getSum(),
                        SUM_TITLE,
                        ResultType.NUMBER);
                break;
            case COUNT:
                DataBaseUtils.requestToDB(response,
                        SQLUtils.getCount(),
                        COUNT_TITLE,
                        ResultType.NUMBER);
                break;
            default:
                DataBaseUtils.showUnknownCommand(response, command);
        }
    }
}
