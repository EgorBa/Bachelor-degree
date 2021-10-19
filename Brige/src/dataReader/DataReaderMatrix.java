package dataReader;

import java.io.BufferedReader;
import java.io.IOException;

public class DataReaderMatrix implements DataReader {
    @Override
    public int[][] readData(BufferedReader in, int[][] matrix) throws IOException {
        int n = matrix.length;
        for (int i = 0; i < n; i++) {
            String[] split = in.readLine().split("[\\s]");
            for (int j = 0; j < n; j++) {
                matrix[i][j] = Integer.parseInt(split[j]);
            }
        }
        return matrix;
    }
}
