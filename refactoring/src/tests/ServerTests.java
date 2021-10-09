import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import ru.akirakozov.sd.refactoring.servlet.Servlet;

import java.io.IOException;

public class ServerTests {

    Servlet servletStub;

    @Before
    public void prepare() throws IOException {
        servletStub = Mockito.mock(Servlet.class);
        Mockito.doNothing().when(servletStub).doGet(Mockito.any(), Mockito.any());
    }

    @Test
    public void simpleTest() throws IOException {
        servletStub.doGet(null, null);
        Mockito.verify(servletStub, Mockito.times(1)).doGet(Mockito.any(), Mockito.any());
    }

}
