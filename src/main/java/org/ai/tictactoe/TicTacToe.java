package org.ai.tictactoe;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class TicTacToe {
    static String[][] xo = new String[7][7];

    public static void main(String[] args) {
        for (int i = 0; i < 7; i++) {
            for (int j = 0; j < 7; j++) {
                xo[i][j] = " ";
            }
        }

        start();
    }

    /**
     * Prints the Tic-Tac-Toe game table to the console.
     * The table is represented by a 2D array of strings.
     * Each cell in the table is printed with a space character.
     * The rows are labeled with numbers from 0 to 6.
     * The columns are labeled with numbers from 0 to 6.
     * The cells are separated by vertical bars.
     *
     * @return None
     */
    static void printTable() {
        System.out.print("  ");
        for (int i = 0; i < 7; i++) {
            System.out.print(i + " ");
        }
        System.out.println();
        for (int i = 0; i < 7; i++) {
            System.out.print(i + " ");
            for (int j = 0; j < 7; j++) {
                System.out.print(xo[i][j]);
                if (j < 6)
                    System.out.print(" | ");
            }
            System.out.println();
        }
        System.out.println();
    }

    static boolean checkWin(String p) {
        for (List<int[]> line : getLines()) {
            if (checkLine(p, line)) {
                return true;
            }
        }
        return false;
    }

    static List<List<int[]>> getLines() {
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

    static boolean checkLine(String p, List<int[]> coord) {
        for (int[] c : coord) {
            if (!xo[c[0]][c[1]].equals(p)) {
                return false;
            }
        }

        return true;
    }

    static int[] minmaxopt(String p, double alpha, double beta, int depth) {
        System.out
                .println("opti called with player =" + p + ", alpha=" + alpha + ", beta=" + beta + ", depth=" + depth);

        if (checkWin("o")) {
            return new int[] { 1, -1, -1 };
        } else if (checkWin("x")) {
            return new int[] { -1, -1, -1 };
        } else if (depth == 0) {
            int score = checkB();
            return new int[] { score, -1, -1 };
        }

        int[] result;
        if (p.equals("o")) {
            result = maximizeComputer(alpha, beta, depth);
        } else {
            result = minimizePlayer(alpha, beta, depth);
        }

        return result;
    }

    static int checkB() {   // something is wrong here
        int Xwin = 0, Owin = 0;
        for (List<int[]> line : getLines()) {
            int[] counts = checkL(line);
            if (counts[0] == 3 && counts[2] == 1) {
                Xwin++;
            } else if (counts[1] == 3 && counts[2] == 1) {
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

    static int[] checkL(List<int[]> line) {
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

    static int[] maximizeComputer(double alpha, double beta, int depth) {
        double curScore = -Double.MAX_VALUE;
        int ansR = -1, ansCol = -1;
        for (int i = 0; i < 7; i++) {
            for (int j = 0; j < 7; j++) {
                if (xo[i][j].equals(" ")) {
                    xo[i][j] = "o";
                    int[] result = minmaxopt("x", alpha, beta, depth - 1);
                    xo[i][j] = " ";
                    if (result[0] > curScore) { // why compare with current score
                        curScore = result[0];
                        ansR = i;
                        ansCol = j;
                    }
                    
                    alpha = Math.max(alpha, curScore);
                    if (beta <= alpha) {
                        break;
                    }
                }
            }
        }
        return new int[] { (int) curScore, ansR, ansCol };
    }

    static int[] minimizePlayer(double alpha, double beta, int depth) {
        double curScore = Double.MAX_VALUE;
        int ansR = -1, ansCol = -1;
        for (int i = 0; i < 7; i++) {
            for (int j = 0; j < 7; j++) {
                if (xo[i][j].equals(" ")) {
                    xo[i][j] = "x";
                    int[] result = minmaxopt("o", alpha, beta, depth - 1);
                    xo[i][j] = " ";
                    if (result[0] < curScore) {
                        curScore = result[0];
                        ansR = i;
                        ansCol = j;
                    }
                    beta = Math.min(beta, curScore);
                    if (beta <= alpha) {
                        break;
                    }
                }
            }
        }
        return new int[] { (int) curScore, ansR, ansCol };
    }

    static void start() {
        Scanner scanner = new Scanner(System.in);
        String p = "x";
        int turns = 0;
        int dep = 3;
        System.out.println("please note your choice affects your RAM and CPU usage directly");
        System.out.println("please choose difficulty 1 is easy 4 is extreme");
        dep = scanner.nextInt();
        dep = dep > 0 && dep < 5 ? dep : 3;
        System.out.println(dep);
        while (turns < 49) {
            printTable();
            if (p.equals("o")) {
                int[] result = minmaxopt(p, -Double.MAX_VALUE, Double.MAX_VALUE, dep);
                int row = result[1];
                int col = result[2];
                System.out.println("player O move: (" + row + ", " + col + ")");
                xo[row][col] = p;
            } else {
                System.out.println("player X, (r,c): ");
                int row = scanner.nextInt();
                int col = scanner.nextInt();
                if (xo[row][col].equals(" ")) {
                    xo[row][col] = p;
                } else {
                    System.out.println("occupied");
                    continue;
                }
            }
            if (checkWin(p)) {
                printTable();
                System.out.println("p " + p + " wins!");
                return;
            }
            p = p.equals("o") ? "x" : "o";
            turns++;
        }

        printTable();
        System.out.println("tie");
    }
}
