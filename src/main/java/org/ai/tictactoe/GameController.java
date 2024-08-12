package org.ai.tictactoe;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;

import org.ai.tictactoe.logic.ComputerPlayer;
import org.ai.tictactoe.logic.GameBoard;
import org.ai.tictactoe.logic.GameSymbol;
import org.ai.tictactoe.logic.GameType;
import org.ai.tictactoe.logic.HumanPlayer;
import org.ai.tictactoe.logic.Player;
import org.ai.tictactoe.player.GameDifficulty;
import org.ai.tictactoe.ui.Tile;

import java.net.URL;
import java.util.ResourceBundle;

public class GameController implements Initializable {
    public static GameDifficulty difficultyLevel;
    public static GameType gameType;

    @FXML
    private GridPane boardGrid;
    private static final int SIZE = 7;

    private int currentCount;

    private Player player1;
    private Player player2;

    private GameBoard gameBoard = GameBoard.getBoard();

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        Platform.runLater(this::fillGrid);
        if (gameType.equals(GameType.PLAYER_VS_PLAYER)) {
            player1 = new HumanPlayer();
            player2 = new HumanPlayer();
        } else if (gameType.equals(GameType.PLAYER_VS_AI)) {
            player1 = new HumanPlayer();
            player2 = new ComputerPlayer(difficultyLevel);
        } else {
            player1 = new ComputerPlayer(difficultyLevel);
            player2 = new ComputerPlayer(difficultyLevel);
        }
    }


    private void fillGrid() {
        for (int i = 0; i < SIZE; i++) {
            for (int j = 0; j < SIZE; j++) {
                boardGrid.add(new Tile(i, j), j, i);
            }
        }
    }

    public void playerClicked(MouseEvent mouseEvent) {
        int count = 0;
        for (Node tile : boardGrid.getChildren()) {
            if (tile instanceof Tile && ((Tile) tile).getSymbol() != null) {
                count++;
            }
        }

        if (gameType.equals(GameType.PLAYER_VS_PLAYER)) {
            if (count > currentCount) {
                for (Node tile : boardGrid.getChildren()) {
                    ((Tile) tile).setxTurn(!((Tile) tile).isxTurn());
                }
                currentCount++;
            }
        } else if (gameType.equals(GameType.PLAYER_VS_AI)) {
            if (count > currentCount) {
                for (Node tile : boardGrid.getChildren()) {
                    ((Tile) tile).setDisable(true);
                }
                updateGameBoard();
                player2.makeMove(gameBoard);
                //((Tile)boardGrid.getChildren().get(currentCount)).setSymbol(GameSymbol.O);
                updateGraphicBoard();
                currentCount++;
                for (Node tile : boardGrid.getChildren()) {
                    ((Tile) tile).setDisable(false);
                }
            }
        } else if (gameType.equals(GameType.AI_VS_AI)) {
            player1.makeMove(gameBoard);
            player2.makeMove(gameBoard);
        }
    }

    private void updateGraphicBoard() {
        int col = gameBoard.getLastMovCol();
        int row = gameBoard.getLastMovRow();
        Tile tile = ((Tile) boardGrid.getChildren().get(row * SIZE + col));
        tile.setSymbol(GameSymbol.O);
    }

    private void updateGameBoard() {
        for (int i = 0; i < SIZE; i++) {
            for (int j = 0; j < SIZE; j++) {
                try {
                    Tile tile = (Tile) boardGrid.getChildren().get(i * SIZE + j);
                    if (tile.getSymbol() != null)
                        gameBoard.placeMove(i, j, tile.getSymbol());
                } catch (Exception e) {
//                    System.out.println("Error: " + e.getMessage());
                }
            }
        }
    }
}