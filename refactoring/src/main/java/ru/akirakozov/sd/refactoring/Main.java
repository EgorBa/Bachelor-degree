package ru.akirakozov.sd.refactoring;

import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;
import ru.akirakozov.sd.refactoring.servlet.AddProductServlet;
import ru.akirakozov.sd.refactoring.servlet.GetProductsServlet;
import ru.akirakozov.sd.refactoring.servlet.QueryServlet;
import ru.akirakozov.sd.refactoring.servlet.Servlet;
import ru.akirakozov.sd.refactoring.utils.DataBaseUtils;
import ru.akirakozov.sd.refactoring.utils.ResultType;
import ru.akirakozov.sd.refactoring.utils.SQLUtils;

import static ru.akirakozov.sd.refactoring.resources.Resources.*;

/**
 * @author akirakozov
 */
public class Main {
    public static void main(String[] args) throws Exception {
        DataBaseUtils.requestToDB(null, SQLUtils.createTable(), null, ResultType.EMPTY);

        Server server = new Server(8081);

        ServletContextHandler context = new ServletContextHandler(ServletContextHandler.SESSIONS);
        context.setContextPath("/");
        server.setHandler(context);

        addServlet(context, new AddProductServlet(), ADD_PRODUCT);
        addServlet(context, new GetProductsServlet(), GET_PRODUCTS);
        addServlet(context, new QueryServlet(), QUERY);

        server.start();
        server.join();
    }

    private static void addServlet(ServletContextHandler context, Servlet servlet, String path) {
        context.addServlet(new ServletHolder(servlet), path);
    }
}
