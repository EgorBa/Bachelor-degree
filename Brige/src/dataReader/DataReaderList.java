package dataReader;

import java.io.BufferedReader;
import java.io.IOException;

public class DataReaderList implements DataReader {
    @Override
    public int[][] readData(BufferedReader in, int[][] matrix) throws IOException {
        while (in.ready()) {
            String[] split = in.readLine().split("[\\s]");
            int a = Integer.parseInt(split[0]);
            int b = Integer.parseInt(split[1]);
            matrix[b][a] = matrix[a][b] = 1;
        }
        return matrix;
    }
}
