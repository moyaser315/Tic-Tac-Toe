package org.ai.tictactoe.logic.game;

import java.util.ArrayList;
import java.util.List;

public class Judger {
    private final int toWin;
    private final int[][] startShift;
    private final int[][] step;
    private final List<List<int[]>> lines = getLines();

    private Judger(int toWin) {
        if (toWin < 3) {
            toWin = 3;
        }

        this.toWin = toWin;
        startShift = new int[][] {
                { -toWin + 1, 0 }, // up & down
                { 0, -toWin + 1 }, // left & right
                { -toWin + 1, -toWin + 1 }, // left inclined diagonal
                { toWin - 1, -toWin + 1 }, // right inclined diagonal
        };

        step = new int[][] {
                { 1, 0 }, // up & down
                { 0, 1 }, // left & right
                { 1, 1 }, // left inclined diagonal
                { -1, 1 }, // right inclined diagonal
        };
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

    public GameStatus getStateAferPlay(GameBoard b, int row, int col) {
        if (!b.isValidPosition(row, col) || b.getSymbol(row, col) == null) {
            return null;
        }

        for (int dir = 0; dir < startShift.length; dir++) { // for each direction where we can get winning sequence
            // the position of the 1st element for the first sequence to check in this
            // direction
            int[] pos = { row + startShift[dir][0], col + startShift[dir][1] };
            while (!b.isValidPosition(pos[0], pos[1])) {
                pos[0] += step[dir][0];
                pos[1] += step[dir][1];
            }
            // System.out.println("Starting from position: " + pos[0] + ", " + pos[1] + "
            // for direction " + dir);

            int xCount = 0, oCount = 0, emptyCount = 0;
            for (int i = 0; i < b.size(); i++) {
                if (!b.isValidPosition(pos[0], pos[1])) {
                    break;
                }

                GameSymbol s = b.getSymbol(pos[0], pos[1]);
                if (s == null) {
                    emptyCount++;
                } else if (s == GameSymbol.X) {
                    xCount++;
                } else {
                    oCount++;
                }
                // System.out.println("Considered position: " + pos[0] + ", " + pos[1] + " with
                // symbol: " + s + " xCount: " + xCount + " oCount: " + oCount + " emptyCount: "
                // + emptyCount);

                if (i >= 3) {
                    // System.out.println("Checking if someone has won, xCount: " + xCount + "
                    // oCount: " + oCount + " emptyCount: " + emptyCount);
                    if (xCount == toWin) {
                        return GameStatus.X_WIN;
                    } else if (oCount == toWin) {
                        return GameStatus.O_WIN;
                    }

                    int[] discardPos = { pos[0] - ((toWin - 1) * step[dir][0]), pos[1] - ((toWin - 1) * step[dir][1]) };
                    if (discardPos[0] == row && discardPos[1] == col) {
                        break;
                    }

                    GameSymbol discard = b.getSymbol(discardPos[0], discardPos[1]);
                    if (discard == null) {
                        emptyCount--;
                    } else if (discard == GameSymbol.X) {
                        xCount--;
                    } else {
                        oCount--;
                    }
                }

                pos[0] += step[dir][0];
                pos[1] += step[dir][1];
            }
        }

        return b.complete() ? GameStatus.DRAW : GameStatus.IN_PROGRESS;
    }

    public int getScore(GameBoard b, int row, int col) {
        if (!b.isValidPosition(row, col) || b.getSymbol(row, col) == null) {
            return 0;
        }

        int score = 0;
        for (List<int[]> line : lines) {
            int xCount = 0, oCount = 0, emptyCount = 0;
            for (int[] pos : line) {
                if (b.getSymbol(pos[0], pos[1]) == GameSymbol.X) {
                    xCount++;
                } else if (b.getSymbol(pos[0], pos[1]) == GameSymbol.O) {
                    oCount++;
                } else {
                    emptyCount++;
                }
            }

            if (xCount == toWin) {
                score = -(Integer.MAX_VALUE - 10);
                break;
            } else if (oCount == toWin) {
                score = Integer.MAX_VALUE - 10;
                break;
            } else if (xCount >= 1 && xCount + emptyCount == toWin) {
                score -= 1;
            } else if (oCount >= 1 && oCount + emptyCount == toWin) {
                score += 1;
            } else if (xCount == 3 && oCount == 1) {
                score += 100;
            } else if (oCount == 3 && xCount == 1) {
                score -= 100;
            } else if (xCount == 2 && emptyCount == 1 && oCount == 1) {
                score += 50;
            } else if (oCount == 2 && emptyCount == 1 && xCount == 1) {
                score -= 50;
            }
        }
        return score;
    }

    // Singleton
    private static Judger b;

    public static Judger getJudger() {
        if (b == null) {
            b = new Judger(4);
        }

        return b;
    }
}
