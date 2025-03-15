import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;

public class I {
    public static int[][] a = new int[4][4];
    public static int[][] b = new int[4][4];
    public static int[][] c = new int[4][4];

    public static void main(String[] args) throws IOException {
        BufferedReader in = new BufferedReader(new FileReader("sqroot.in"));
        for (int i = 0; i < 4; i++) {
            String[] split = in.readLine().split("[\\s]");
            for (int j = 0; j < 4; j++) {
                a[i][j] = Integer.parseInt(split[j]);
            }
        }
        boolean flag = false;
        for (int i11 = 0; i11 < 2; i11++) {
            for (int i12 = 0; i12 < 2; i12++) {
                for (int i13 = 0; i13 < 2; i13++) {
                    for (int i14 = 0; i14 < 2; i14++) {
                        for (int i21 = 0; i21 < 2; i21++) {
                            for (int i22 = 0; i22 < 2; i22++) {
                                for (int i23 = 0; i23 < 2; i23++) {
                                    for (int i24 = 0; i24 < 2; i24++) {
                                        for (int i31 = 0; i31 < 2; i31++) {
                                            for (int i32 = 0; i32 < 2; i32++) {
                                                for (int i33 = 0; i33 < 2; i33++) {
                                                    for (int i34 = 0; i34 < 2; i34++) {
                                                        for (int i41 = 0; i41 < 2; i41++) {
                                                            for (int i42 = 0; i42 < 2; i42++) {
                                                                for (int i43 = 0; i43 < 2; i43++) {
                                                                    for (int i44 = 0; i44 < 2; i44++) {
                                                                        b[0][0] = i11;
                                                                        b[0][1] = i12;
                                                                        b[0][2] = i13;
                                                                        b[0][3] = i14;
                                                                        b[1][0] = i21;
                                                                        b[1][1] = i22;
                                                                        b[1][2] = i23;
                                                                        b[1][3] = i24;
                                                                        b[2][0] = i31;
                                                                        b[2][1] = i32;
                                                                        b[2][2] = i33;
                                                                        b[2][3] = i34;
                                                                        b[3][0] = i41;
                                                                        b[3][1] = i42;
                                                                        b[3][2] = i43;
                                                                        b[3][3] = i44;
                                                                        if (check()) {
                                                                            flag = true;
                                                                            c[0][0] = i11;
                                                                            c[0][1] = i12;
                                                                            c[0][2] = i13;
                                                                            c[0][3] = i14;
                                                                            c[1][0] = i21;
                                                                            c[1][1] = i22;
                                                                            c[1][2] = i23;
                                                                            c[1][3] = i24;
                                                                            c[2][0] = i31;
                                                                            c[2][1] = i32;
                                                                            c[2][2] = i33;
                                                                            c[2][3] = i34;
                                                                            c[3][0] = i41;
                                                                            c[3][1] = i42;
                                                                            c[3][2] = i43;
                                                                            c[3][3] = i44;
                                                                        }
                                                                    }
                                                                }
                                                            }
                                                        }
                                                    }
                                                }
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
        in.close();
        PrintWriter out = new PrintWriter("sqroot.out");
        if (flag) {
            for (int i = 0; i < 4; i++) {
                for (int j = 0; j < 4; j++) {
                    out.print(c[i][j] + " ");
                }
                out.println();
            }
        } else {
            out.println("NO SOLUTION");
        }
        out.close();
    }

    private static boolean check() {
        int[][] c = new int[4][4];
        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 4; j++) {
                for (int t = 0; t < 4; t++) {
                    c[i][j] += b[i][t] * b[t][j];
                }
            }
        }
        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 4; j++) {
                c[i][j] %= 2;
            }
        }
        int count = 0;
        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 4; j++) {
                if (c[i][j] == a[i][j]) {
                    count++;
                }
            }
        }
        return count == 16;
    }
}
