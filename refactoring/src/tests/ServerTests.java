import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import ru.akirakozov.sd.refactoring.servlet.ServletInterface;

import java.io.IOException;

import static org.junit.Assert.assertEquals;

public class ServerTests {

    ServletInterface servlet;
    int response;

    @Before
    public void prepare(){
        servlet = Mockito.mock(ServletInterface.class);
        try {
            Mockito.doNothing().when(servlet).doGet(Mockito.any(), Mockito.any());
        } catch (IOException ignored) {
        }
    }

    @Test
    public void simpleTest() {
        try {
            servlet.doGet(null, null);
            response = 1;
        } catch (IOException e) {
            response = 0;
        }
        assertEquals(1, response);
    }

}
