package ru.akirakozov.sd.refactoring.servlet;

import ru.akirakozov.sd.refactoring.utils.DatabaseUtils;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public abstract class Servlet extends HttpServlet {
    DatabaseUtils dataBaseUtils;

    public Servlet(DatabaseUtils dataBaseUtils) {
        this.dataBaseUtils = dataBaseUtils;
    }

    @Override
    public void doGet(HttpServletRequest req, HttpServletResponse resp) {

    }
}
