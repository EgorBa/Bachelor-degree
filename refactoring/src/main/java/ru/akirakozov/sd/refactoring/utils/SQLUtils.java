package ru.akirakozov.sd.refactoring.utils;

public class SQLUtils {

    public static String getAll() {
        return "SELECT * FROM PRODUCT";
    }

    public static String addProduct(String name, long price) {
        return "INSERT INTO PRODUCT (NAME, PRICE) VALUES (\"" + name + "\"," + price + ")";
    }

    public static String createTable() {
        return "CREATE TABLE IF NOT EXISTS PRODUCT" +
                "(ID INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL," +
                " NAME           TEXT    NOT NULL, " +
                " PRICE          INT     NOT NULL)";
    }

    public static String getExpensiveProduct() {
        return "SELECT * FROM PRODUCT ORDER BY PRICE DESC LIMIT 1";
    }

    public static String getChipProduct() {
        return "SELECT * FROM PRODUCT ORDER BY PRICE LIMIT 1";
    }

    public static String getSum() {
        return "SELECT SUM(price) FROM PRODUCT";
    }

    public static String getCount() {
        return "SELECT COUNT(*) FROM PRODUCT";
    }

    public static String dropAll(){
        return "DROP TABLE IF EXISTS PRODUCT";
    }

}
