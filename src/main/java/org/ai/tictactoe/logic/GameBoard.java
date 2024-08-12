package org.ai.tictactoe.logic;

public class GameBoard {
    // attributes
    private GameSymbol[][] grid;
    private int plays = 0;
    private int lastPlay[] = {-1, -1};

    private GameBoard(int size) {
        if (size < 3) {
            size = 7;
        }

        grid = new GameSymbol[size][size];
    }

    public boolean place(GameSymbol s, int row, int col) {
        if (!isValidPosition(row, col) || !isEmpty(row, col)) {
            return false;
        }

        grid[row][col] = s;
        plays++;
        lastPlay[0] = row;
        lastPlay[1] = col;
        return true;
    }

    public boolean remove(int row, int col) {
        if (!isValidPosition(row, col) || isEmpty(row, col)) {
            return false;
        }

        grid[row][col] = null;
        plays--;
        lastPlay[0] = -1;
        lastPlay[1] = -1;
        return true;
    }

    public boolean isValidPosition(int row, int col) {
        if (row < 0 || row >= grid.length || col < 0 || col >= grid.length) {
            return false;
        }

        return true;
    }

    public boolean isEmpty(int row, int col) {
        if (!isValidPosition(row, col)) {
            return false;
        }

        return grid[row][col] == null;
    }

    public GameSymbol getSymbol(int row, int col) {
        if (!isValidPosition(row, col)) {
            return null;
        }

        return grid[row][col];
    }

    public int size() {
        return grid.length;
    }

    public boolean complete() {
        return plays == grid.length * grid.length;
    }

    public int[] getLastPlay() {
        return new int[] {lastPlay[0], lastPlay[1]};
    }

    public void setLastPlay(int row, int col) {
        if (!isValidPosition(row, col)) {
            return;
        }

        lastPlay[0] = row;
        lastPlay[1] = col;
    }

    public void print() {
        for (GameSymbol[] row : grid) {
            for (GameSymbol slot : row) {
                if (slot == null) {
                    System.out.print(" - ");
                } else {
                    System.out.print(" " + slot + " ");
                }
            }

            System.out.println("");
        }
    }

    // Singleton
    private static GameBoard b;
    public static GameBoard getBoard() {
        if (b == null) {
            b = new GameBoard(7);
        }

        return b;
    }
}
