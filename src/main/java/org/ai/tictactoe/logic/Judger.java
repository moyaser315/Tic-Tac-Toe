package org.ai.tictactoe.logic;

public class Judger {
    private final int toWin;
    private final int[][] startShift;
    private final int[][] step;

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
    public int getScore(GameBoard b, int row, int col) {
        if (!b.isValidPosition(row, col) || b.getSymbol(row, col) == null) {
            return 0;
        }

        int score = 0;
        boolean blockOppWin = false, win = false;
        for (int dir = 0; dir < startShift.length; dir++) { // for each direction where we can get winning sequence
            // the position of the 1st element for the first sequence to check in this
            // direction
            int[] pos = { row + startShift[dir][0], col + startShift[dir][1] };
            while (!b.isValidPosition(pos[0], pos[1])) {
                pos[0] += step[dir][0];
                pos[1] += step[dir][1];
            }

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

                if (i >= 3) {
                    if (xCount == toWin || oCount == toWin) {
                        score = Integer.MAX_VALUE - 10;
                        win = true;
                        break;
                    }

                    if (b.getSymbol(row, col) == GameSymbol.X) {
                        if (oCount == toWin - 1 && xCount == 1) {
                            blockOppWin = true;
                        }
                    } else {
                        if (xCount == toWin - 1 && oCount == 1) {
                            blockOppWin = true;
                        }
                    }

                    if (b.getSymbol(row, col) == GameSymbol.X) {
                        if (xCount + emptyCount == 4) {
                            score += xCount - 1;
                        }
                    } else {
                        if (oCount + emptyCount == 4) {
                            score += oCount - 1;
                        }
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

        score = (blockOppWin && !win) ? Integer.MAX_VALUE - 20 : score;
        return b.getSymbol(row, col) == GameSymbol.O ? score : -score;
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

    // Singleton
    private static Judger b;
    public static Judger getJudger() {
        if (b == null) {
            b = new Judger(4);
        }

        return b;
    }
}