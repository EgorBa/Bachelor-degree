import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import ru.akirakozov.sd.refactoring.utils.HTMLUtils;
import ru.akirakozov.sd.refactoring.utils.ResultType;

import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;

import static org.junit.Assert.assertEquals;

public class HTMLTests {

    HttpServletResponse responseStub;
    PrintWriter out;
    String FILE_NAME = "test.txt";
    String HTML_TEXT = "<html><body>title</body></html>";

    @Before
    public void prepare() throws IOException {
        responseStub = Mockito.mock(HttpServletResponse.class);
        out = new PrintWriter(FILE_NAME);
        Mockito.when(responseStub.getWriter()).thenReturn(out);
        Mockito.doNothing().when(responseStub).setContentType(Mockito.anyString());
    }

    @After
    public void clear() throws IOException {
        Files.delete(Paths.get(FILE_NAME));
    }

    @Test
    public void generateHTMLTest() throws IOException {
        HTMLUtils.generateHTMLAnswer(null, responseStub, "title", ResultType.EMPTY);
        BufferedReader in = new BufferedReader(new FileReader(FILE_NAME));
        StringBuilder ans = new StringBuilder();
        while (in.ready()) {
            ans.append(in.readLine());
        }
        in.close();
        assertEquals(HTML_TEXT, ans.toString());
    }
}
