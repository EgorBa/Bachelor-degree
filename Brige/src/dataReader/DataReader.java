package dataReader;

import java.io.BufferedReader;
import java.io.IOException;

public interface DataReader {
    int[][] readData(BufferedReader in, int[][] matrix) throws IOException;
}
