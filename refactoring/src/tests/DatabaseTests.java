import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import ru.akirakozov.sd.refactoring.utils.SQLUtils;

import java.sql.*;

import static org.junit.Assert.assertEquals;
import static ru.akirakozov.sd.refactoring.resources.Resources.*;

public class DatabaseTests {

    Statement stmt;
    Connection c;
    String PROD1 = "Product1";
    String PROD2 = "Product2";
    int PRICE1 = 10;
    int PRICE2 = 20;

    @Before
    public void prepare() throws SQLException {
        c = DriverManager.getConnection("jdbc:sqlite:testDB.db");
        stmt = c.createStatement();
        stmt.executeUpdate(SQLUtils.dropAll());
        stmt.executeUpdate(SQLUtils.createTable());
        stmt.executeUpdate(SQLUtils.addProduct(PROD1, PRICE1));
        stmt.executeUpdate(SQLUtils.addProduct(PROD2, PRICE2));
    }

    @After
    public void closing() throws SQLException {
        stmt.close();
    }

    @Test
    public void testGetAll() throws SQLException {
        ResultSet rs = stmt.executeQuery(SQLUtils.getAll());
        int count = 0;
        while (rs.next()) {
            count++;
        }
        assertEquals(count, 2);
    }

    @Test
    public void testGetExpensive() throws SQLException {
        ResultSet rs = stmt.executeQuery(SQLUtils.getExpensiveProduct());
        assertEquals(PROD2, rs.getString(NAME));
        assertEquals(PRICE2, rs.getInt(PRICE));
    }

    @Test
    public void testGetChip() throws SQLException {
        ResultSet rs = stmt.executeQuery(SQLUtils.getChipProduct());
        assertEquals(PROD1, rs.getString(NAME));
        assertEquals(PRICE1, rs.getInt(PRICE));
    }

    @Test
    public void testGetCount() throws SQLException {
        ResultSet rs = stmt.executeQuery(SQLUtils.getCount());
        assertEquals(2, rs.getInt(1));
    }

    @Test
    public void testGetSum() throws SQLException {
        ResultSet rs = stmt.executeQuery(SQLUtils.getSum());
        assertEquals(PRICE1 + PRICE2, rs.getInt(1));
    }

}
