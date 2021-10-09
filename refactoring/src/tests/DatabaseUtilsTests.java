import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import ru.akirakozov.sd.refactoring.utils.DatabaseUtils;
import ru.akirakozov.sd.refactoring.utils.ResultType;
import ru.akirakozov.sd.refactoring.utils.SQLUtils;

import java.sql.*;

import static org.junit.Assert.assertEquals;
import static ru.akirakozov.sd.refactoring.resources.Resources.*;

public class DatabaseUtilsTests {

    Statement stmt;
    Connection c;
    ResultSet rs;
    DatabaseUtils databaseUtils;
    String DATABASE = "jdbc:sqlite:testDB.db";
    String PROD1 = "Product1";
    String PROD2 = "Product2";
    int PRICE1 = 10;
    int PRICE2 = 20;

    @Before
    public void prepare() throws SQLException {
        databaseUtils = new DatabaseUtils(DATABASE);
        databaseUtils.requestToDB(null, SQLUtils.dropAll(), null, ResultType.EMPTY);
        databaseUtils.requestToDB(null, SQLUtils.createTable(), null, ResultType.EMPTY);
        databaseUtils.requestToDB(null, SQLUtils.addProduct(PROD1, PRICE1), null, ResultType.EMPTY);
        databaseUtils.requestToDB(null, SQLUtils.addProduct(PROD2, PRICE2), null, ResultType.EMPTY);
        c = DriverManager.getConnection(DATABASE);
        stmt = c.createStatement();
    }

    @After
    public void closing() throws SQLException {
        rs.close();
        stmt.close();
    }

    @Test
    public void testGetAll() throws SQLException {
        rs = stmt.executeQuery(SQLUtils.getAll());
        int count = 0;
        while (rs.next()) {
            count++;
        }
        assertEquals(count, 2);
    }

    @Test
    public void testGetExpensive() throws SQLException {
        rs = stmt.executeQuery(SQLUtils.getExpensiveProduct());
        assertEquals(PROD2, rs.getString(NAME));
        assertEquals(PRICE2, rs.getInt(PRICE));
    }

    @Test
    public void testGetChip() throws SQLException {
        rs = stmt.executeQuery(SQLUtils.getChipProduct());
        assertEquals(PROD1, rs.getString(NAME));
        assertEquals(PRICE1, rs.getInt(PRICE));
    }

    @Test
    public void testGetCount() throws SQLException {
        rs = stmt.executeQuery(SQLUtils.getCount());
        assertEquals(2, rs.getInt(1));
    }

    @Test
    public void testGetSum() throws SQLException {
        rs = stmt.executeQuery(SQLUtils.getSum());
        assertEquals(PRICE1 + PRICE2, rs.getInt(1));
    }

}
