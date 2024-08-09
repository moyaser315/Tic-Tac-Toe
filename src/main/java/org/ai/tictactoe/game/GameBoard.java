package org.ai.tictactoe.game;


import java.util.ArrayList;

public class GameBoard {
    // Singleton
    private static GameBoard board;

    public static GameBoard getBoard() {
        if (board == null) {
            board = new GameBoard(7);
        }

        return board;
    }

    private GameBoard(final int size) {
        this.grid = new GameSymbol[size][size];
    }

    private GameBoard(GameBoard copy_board) {
        // copy grid
        this.grid = new GameSymbol[copy_board.grid.length][copy_board.grid.length];
        for (int row = 0; row < grid.length; row++) {
            for (int col = 0; col < grid.length; col++) {
                grid[row][col] = copy_board.grid[row][col];
            }
        }

        // copy rest of game status
        this.plays = copy_board.plays;
        this.status = copy_board.status;
        this.lastMovRow = copy_board.lastMovRow;
        this.lastMovCol = copy_board.lastMovCol;
    }

    // attributes
    // -> static ones
    private static final int seqCount = 4; // no of (x/o) in a row to win
    private static final int[][] dRow = {
            { 0, 1, 2, 3 }, // down
            { 0, -1, -2, -3 }, // up
            { 0, 0, 0, 0 }, // right
            { 0, 0, 0, 0 }, // left
            { 0, 1, 2, 3 }, // left inclined diagonal
            { 0, -1, -2, -3 }, // right inclined diagonal
    };
    private static final int[][] dCol = {
            { 0, 0, 0, 0 },
            { 0, 0, 0, 0 },
            { 0, 1, 2, 3 },
            { 0, -1, -2, -3 },
            { 0, 1, 2, 3 },
            { 0, -1, -2, -3 }
    };
    // -> instance ones
    private GameSymbol[][] grid; // grid of the board
    private int plays = 0;
    private int lastMovRow = -1;
    private int lastMovCol = -1;
    private GameStatus status = GameStatus.IN_PROGRESS;

    // functions
    public int getSize() {
        return this.grid.length;
    }

    /**
     * Checks if the tile at the specified row and column is empty.
     *
     * @param row the row index of the tile
     * @param col the column index of the tile
     * @return true if the tile is empty, false otherwise
     * @throws Exception if the tile position is invalid
     */
    public boolean isTileEmpty(final int row, final int col) throws Exception {
        if (!validPosition(row, col)) {
            throw new Exception("Invalid tile position");
        }

        return grid[row][col] == null;
    }

    /**
     * Retrieves the status of the tile at the specified position on the game board.
     *
     * @param row the row index of the tile
     * @param col the column index of the tile
     * @return the status of the tile (null, X, or O)
     * @throws Exception if the tile position is invalid
     */
    public GameSymbol getTileStatus(final int row, final int col) throws Exception {
        if (!validPosition(row, col)) {
            throw new Exception("Invalid tile position");
        }

        return grid[row][col];
    }

    /**
     * Returns the current status of the game.
     *
     * @return the current status of the game
     */
    public GameStatus getGameStatus() {
        return status;
    }

    /**
     * Returns the number of plays made on the game board.
     *
     * @return the number of plays made on the game board
     */
    public int getPlays() {
        return plays;
    }

    /**
     * Retrieves the row index of the last move made on the game board.
     *
     * @return the row index of the last move
     */
    public int getLastMovRow() {
        return lastMovRow;
    }

    /**
     * Retrieves the column index of the last move made on the game board.
     *
     * @return the column index of the last move
     */
    public int getLastMovCol() {
        return lastMovCol;
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

    /**
     * Checks if the given position is within board range
     *
     * @param row the board row
     * @param col the board column
     * @return boolean (true/false)
     */
    private boolean validPosition(final int row, final int col) {
        if (row < 0 || row >= grid.length || col < 0 || col >= grid.length) {
            return false;
        }

        return true;
    }

    /**
     * Calculates the impact of a move on the game board.
     *
     * @param row the row index of the move
     * @param col the column index of the move
     * @return the GameStatus indicating the impact of the move
     * @throws Exception if the position is invalid or empty
     */
    private GameStatus moveImpact(final int row, final int col) throws Exception {
        if (!validPosition(row, col) || grid[row][col] == null) {
            throw new Exception("Invalid position to check");
        }

        GameSymbol symbol = grid[row][col];
        for (int seq = 0; seq < 6; seq++) {
            int count = 0;
            for (int mov = 0; mov < 4; mov++) {
                int cur_row = row + dRow[seq][mov], cur_col = col + dCol[seq][mov];
                if (!validPosition(cur_row, cur_col)) {
                    continue;
                }

                if (grid[cur_row][cur_col] == symbol) {
                    count++;
                } else {
                    break;
                }
            }

            if (count == seqCount) {
                if (symbol == GameSymbol.X) {
                    return GameStatus.X_WIN;
                } else {
                    return GameStatus.O_WIN;
                }
            }
        }

        if (plays == grid.length * grid.length) {
            return GameStatus.DRAW;
        }

        return GameStatus.IN_PROGRESS;
    }

    /**
     * Places a move on the game board.
     *
     * @param row    the row index of the move
     * @param col    the column index of the move
     * @param symbol the symbol of the move
     * @return the GameStatus indicating the impact of the move
     * @throws Exception if the position is invalid or already occupied
     */
    public GameStatus placeMove(final int row, final int col, final GameSymbol symbol) throws Exception {
        if (!validPosition(row, col)) {
            throw new IllegalMoveException("Out of grid");
        } else if (grid[row][col] != null) {
            throw new IllegalMoveException("Position already occupied");
        } else if (plays == grid.length * grid.length) {
            throw new IllegalMoveException("Game is already over");
        } else if (status != GameStatus.IN_PROGRESS) {
            return status;
        }

        grid[row][col] = symbol;
        lastMovRow = row;
        lastMovCol = col;
        plays++;

        status = moveImpact(row, col);
        return status;
    }

    /**
     * Gets all empty positions on the board
     *
     * @return an array of pairs [even index, odd index, ...etc], where each
     *         consecutive 2 numbers represents the (row, col) of an empty position
     */
    public ArrayList<Integer> getEmptyTiles() {
        ArrayList<Integer> list = new ArrayList<>();
        for (int row = 0; row < grid.length; row++) {
            for (int col = 0; col < grid.length; col++) {
                if (grid[row][col] == null) {
                    list.add(row);
                    list.add(col);
                }
            }
        }

        return list;
    }

    /**
     * Generates the next possible game states for the next given move.
     *
     * @param nextPlay the symbol of the player making the next move
     * @return an ArrayList containing the next possible game states
     */
    public ArrayList<GameBoard> nextStates(final GameSymbol nextPlay) {
        if (status != GameStatus.IN_PROGRESS) {
            return new ArrayList<>();
        }

        ArrayList<GameBoard> list = new ArrayList<>();
        for (int row = 0; row < grid.length; row++) {
            for (int col = 0; col < grid.length; col++) {
                if (grid[row][col] == null) {
                    GameBoard copy = new GameBoard(this);
                    try {
                        copy.placeMove(row, col, nextPlay);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    list.add(copy);
                }
            }
        }

        return list;
    }

}
