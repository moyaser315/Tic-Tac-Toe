import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Main {
    static String[][] xo = new String[7][7];

    public static void main(String[] args) {
        for (int i = 0; i < 7; i++) {
            for (int j = 0; j < 7; j++) {
                xo[i][j] = " ";
            }
        }
        start();
    }

    public static void printTable() {
        System.out.print("  ");
        for (int i = 0; i < 7; i++) {
            System.out.print(i + "   ");
        }
        System.out.println();
        for (int i = 0; i < 7; i++) {
            System.out.print(i + " ");
            for (int j = 0; j < 7; j++) {
                System.out.print(xo[i][j]);
                if (j < 6) {
                    System.out.print(" | ");
                }
            }
            System.out.println();
        }
        System.out.println();
    }

    public static boolean checkWin(String p) {
        for (List<int[]> line : getLines()) {
            if (checkLine(p, line)) {
                return true;
            }
        }
        return false;
    }

    public static List<List<int[]>> getLines() {
        List<List<int[]>> lines = new ArrayList<>();
        for (int i = 0; i < 7; i++) {
            for (int j = 0; j < 4; j++) {
                List<int[]> line = new ArrayList<>();
                for (int k = 0; k < 4; k++) {
                    line.add(new int[] { i, j + k });
                }
                lines.add(line);
            }
        }
        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 7; j++) {
                List<int[]> line = new ArrayList<>();
                for (int k = 0; k < 4; k++) {
                    line.add(new int[] { i + k, j });
                }
                lines.add(line);
            }
        }
        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 4; j++) {
                List<int[]> line = new ArrayList<>();
                for (int k = 0; k < 4; k++) {
                    line.add(new int[] { i + k, j + k });
                }
                lines.add(line);
            }
        }
        for (int i = 0; i < 4; i++) {
            for (int j = 3; j < 7; j++) {
                List<int[]> line = new ArrayList<>();
                for (int k = 0; k < 4; k++) {
                    line.add(new int[] { i + k, j - k });
                }
                lines.add(line);
            }
        }
        return lines;
    }

    public static boolean checkLine(String p, List<int[]> coord) {
        for (int[] c : coord) {
            if (!xo[c[0]][c[1]].equals(p)) {
                return false;
            }
        }
        return true;
    }

    public static int[] minmaxopt(String p, int alpha, int beta, int depth) {
        if (checkWin("o")) {
            return new int[] { 1, 0, 0 };
        } else if (checkWin("x")) {
            return new int[] { -1, 0, 0 };
        } else if (depth == 0) {
            return new int[] { checkB(), 0, 0 };
        }

        int[] result = new int[3];
        if (p.equals("o")) {
            result = maximizeComputer(alpha, beta, depth);
        } else {
            result = minimizePlayer(alpha, beta, depth);
        }

        return result;
    }

    public static int checkB() {
        int Xwin = 0, Owin = 0;
        for (List<int[]> line : getLines()) {
            int[] counts = checkL(line);
            int xcnt = counts[0], ocnt = counts[1], empcnt = counts[2];
            if (xcnt == 3 && empcnt == 1) {
                Xwin++;
            } else if (ocnt == 3 && empcnt == 1) {
                Owin++;
            }
        }

        if (Xwin < Owin) {
            return 1;
        } else if (Owin < Xwin) {
            return -1;
        } else {
            return 0;
        }
    }

    public static int[] checkL(List<int[]> line) {
        int xcnt = 0, ocnt = 0, empcnt = 0;
        for (int[] c : line) {
            if (xo[c[0]][c[1]].equals("x")) {
                xcnt++;
            } else if (xo[c[0]][c[1]].equals("o")) {
                ocnt++;
            } else {
                empcnt++;
            }
        }
        return new int[] { xcnt, ocnt, empcnt };
    }

    public static int[] maximizeComputer(int alpha, int beta, int depth) {
        int curScore = -Integer.MAX_VALUE;
        int ansR = -1, ansC = -1;
        for (int i = 0; i < 7; i++) {
            for (int j = 0; j < 7; j++) {
                if (xo[i][j].equals(" ")) {
                    xo[i][j] = "o";
                    int[] scoreResult = minmaxopt("x", alpha, beta, depth - 1);
                    xo[i][j] = " ";
                    int score = scoreResult[0];
                    if (score > curScore) {
                        curScore = score;
                        ansR = i;
                        ansC = j;
                    }
                    alpha = Math.max(alpha, curScore);
                    if (beta <= alpha) {
                        break;
                    }
                }
            }
        }
        return new int[] { (int) curScore, ansR, ansC };
    }

    public static int[] minimizePlayer(int alpha, int beta, int depth) {
        int curScore = Integer.MAX_VALUE;
        int ansR = -1, ansC = -1;
        for (int i = 0; i < 7; i++) {
            for (int j = 0; j < 7; j++) {
                if (xo[i][j].equals(" ")) {
                    xo[i][j] = "x";
                    int[] scoreResult = minmaxopt("o", alpha, beta, depth - 1);
                    xo[i][j] = " ";
                    int score = scoreResult[0];
                    if (score < curScore) {
                        curScore = score;
                        ansR = i;
                        ansC = j;
                    }
                    beta = Math.min(beta, curScore);
                    if (beta <= alpha) {
                        break;
                    }
                }
            }
        }
        return new int[] { (int) curScore, ansR, ansC };
    }

    public static void start() {
        String p = "x";
        int turns = 0, dep = 3;
        Scanner scanner = new Scanner(System.in);

        System.out.println("Please note your choice affects your RAM and CPU usage directly");
        System.out.println("Please choose difficulty: 1 is easy, 4 is extreme");
        dep = scanner.nextInt();
        dep = dep > 0 && dep < 5 ? dep : 3;

        while (turns < 49) {
            printTable();
            int row, col;
            if (p.equals("o")) {
                int[] move = minmaxopt(p, -Integer.MAX_VALUE, Integer.MAX_VALUE, dep);
                row = move[1];
                col = move[2];
                System.out.println("Player O move: (" + row + ", " + col + ")");
            } else {
                System.out.print("Player X, (r,c): ");
                row = scanner.nextInt();
                col = scanner.nextInt();
            }
            if (xo[row][col].equals(" ")) {
                xo[row][col] = p;
                if (checkWin(p)) {
                    System.out.println("Player " + p + " wins!");
                    printTable();

                    return;
                }
                p = p.equals("x") ? "o" : "x";
                turns++;
            } else {
                System.out.println("Occupied");
            }
        }

        printTable();
        System.out.println("Tie");
    }

}
