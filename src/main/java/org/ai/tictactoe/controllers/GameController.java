package org.ai.tictactoe.controllers;

import javafx.animation.FadeTransition;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.animation.TranslateTransition;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.layout.GridPane;

import java.io.IOException;
import java.net.URL;
import java.util.HashSet;
import java.util.ResourceBundle;
import java.util.Set;

import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.util.Duration;
import org.ai.tictactoe.HelloApplication;
import org.ai.tictactoe.logic.game.*;
import org.ai.tictactoe.logic.player.ComputerPlayer;
import org.ai.tictactoe.logic.player.HumanPlayer;
import org.ai.tictactoe.logic.player.Player;
import org.ai.tictactoe.ui.Tile;

public class GameController implements Initializable {
    @FXML
    private GridPane boardGrid;
    private static final int SIZE = 7;
    public static GameType gameType;
    public static int difficultyLevel;
    private int currentCount;

    private Player player1;
    private Player player2;
    private Judger judger;


    @FXML
    private Text statusScreen;

    private final GameBoard gameBoard = GameBoard.getBoard();

    private Set<Tile> moveSet;
    private Tile lastMove;

    @FXML
    private Pane btnPane;

    private boolean firstTurn = false;
    private boolean isComputerPlaying = false;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        resetBoard();
        Platform.runLater(this::fillGrid);

        if (gameType.equals(GameType.PLAYER_VS_PLAYER)) {
            player1 = new HumanPlayer(GameSymbol.X);
            player2 = new HumanPlayer(GameSymbol.O);
            btnPane.getChildren().clear();
        } else if (gameType.equals(GameType.PLAYER_VS_AI)) {
            player1 = new HumanPlayer(GameSymbol.X);
            player2 = new ComputerPlayer(GameSymbol.O, difficultyLevel);
            btnPane.getChildren().clear();
        } else if (gameType.equals(GameType.AI_VS_AI)) {
            player1 = new ComputerPlayer(GameSymbol.X, difficultyLevel);
            player2 = new ComputerPlayer(GameSymbol.O, difficultyLevel);
            // disable all events on board
            boardGrid.setDisable(true);
            for (Node node : boardGrid.getChildren()) {
                node.setDisable(true);
            }
        }

        judger = Judger.getJudger();
        moveSet = new HashSet<>();

        System.out.println("Game Type: " + gameType);
        System.out.println("Difficulty Level: " + difficultyLevel);
    }


    private void fillGrid() {
        for (int i = 0; i < SIZE; i++) {
            for (int j = 0; j < SIZE; j++) {
                boardGrid.add(new Tile(i, j), j, i);
            }
        }
    }

    @FXML
    public void playerClicked() {
        if (gameType.equals(GameType.PLAYER_VS_AI)) {
            handlePlayerVsAI();
        } else if (gameType.equals(GameType.PLAYER_VS_PLAYER)) {
            handlePlayerVsPlayer();
        }
    }

    @FXML
    private void handleAIVsAI() {
        btnPane.getChildren().get(0).setDisable(true);
        final GameStatus[] status = {GameStatus.IN_PROGRESS};

        Timeline firstTimeline = new Timeline(new KeyFrame(Duration.millis(1000), event -> {
            if (status[0].equals(GameStatus.IN_PROGRESS)) {
                if (firstTurn) {
                    status[0] = playAsComputer(player1);
                    playStatus(status[0]);
                } else {
                    status[0] = playAsComputer(player2);
                    playStatus(status[0]);
                }
                if (status[0] != null && (checkWin(status[0]) || status[0].equals(GameStatus.DRAW))) {
                    checkEndGame(status[0]);
                }

                firstTurn = !firstTurn;
            }
        }));

        firstTimeline.setCycleCount(Timeline.INDEFINITE);
        firstTimeline.play();
    }


    @FXML
    private void handlePlayerVsPlayer() {
        if (isBoardChanged()) {
            currentCount++;
            firstTurn = !firstTurn;
        } else {
            return;
        }

        if (firstTurn) {
            ((HumanPlayer) player1).play(gameBoard, new int[]{lastMove.getX(), lastMove.getY()});
        } else {
            ((HumanPlayer) player2).play(gameBoard, new int[]{lastMove.getX(), lastMove.getY()});
        }

        updateGameBoard();
        GameStatus status = judger.getStateAferPlay(gameBoard, lastMove.getX(), lastMove.getY());

        playStatus(status);
        if (status != null && (checkWin(status) || status.equals(GameStatus.DRAW))) {
            checkEndGame(status);
        }


        for (Node tile : boardGrid.getChildren()) {
            ((Tile) tile).setxTurn(!((Tile) tile).isxTurn());
        }
    }

    @FXML
    private void handlePlayerVsAI() {
        if (!isComputerPlaying) {
            if (!isBoardChanged()) {
                for (Node node : boardGrid.getChildren()) {
                    node.setDisable(false); // Ensure the board is enabled if nothing changed
                }
                return;
            } else {
                currentCount++;
            }

            // Player's move
            ((HumanPlayer) player1).play(gameBoard, new int[]{lastMove.getX(), lastMove.getY()});
            GameStatus status = judger.getStateAferPlay(gameBoard, lastMove.getX(), lastMove.getY());

            // Update the UI after player's move
            updateGameBoard();
            playStatus(status);

            // Check if the game ends after player's move
            if (checkEndGame(status)) return;

            // Disable the board and mark the computer as playing
            isComputerPlaying = true;
            for (Node node : boardGrid.getChildren()) {
                node.setDisable(true);
            }

            // Computer's move in a separate thread
            new Thread(() -> {
                GameStatus computerStatus = playAsComputer(player2);
                // Update the UI after computer's move on the JavaFX Application Thread
                Platform.runLater(() -> {
                    currentCount++;
                    playStatus(computerStatus);
                    checkEndGame(computerStatus);
                    // Re-enable the board after the computer's move is done
                    for (Node node : boardGrid.getChildren()) {
                        node.setDisable(false);
                    }
                    isComputerPlaying = false;
                });
            }).start();
        }
    }



    private boolean isBoardChanged() {
        int count = 0;
        for (Node tile : boardGrid.getChildren()) {
            if (((Tile) tile).getSymbol() != null) {
                count++;
                if (!moveSet.contains(tile)) {
                    moveSet.add((Tile) tile);
                    lastMove = (Tile) tile;
                }
            }
        }

        return count != currentCount;
    }

    private boolean checkEndGame(GameStatus status) {
        if (status != null && (checkWin(status) || status.equals(GameStatus.DRAW))) {
            playStatus(status);
            System.out.println(status);
            for (Node node : boardGrid.getChildren()) {
                node.setDisable(true);
            }
            boardGrid.setDisable(true);
            return true;
        }
        return false;
    }

    private void playStatus(GameStatus status) {
        if (status == null) return;

        statusScreen.setText(status.name());
        if (status.equals(GameStatus.X_WIN)) {
            statusScreen.setFill(Color.web("#007AFF"));
        } else if (status.equals(GameStatus.O_WIN)) {
            statusScreen.setFill(Color.web("#FF3B30"));
        } else if (status.equals(GameStatus.DRAW)) {
            statusScreen.setFill(Color.GOLD);
        } else {
            statusScreen.setFill(Color.web("#B4E380"));
        }

        TranslateTransition transition = new TranslateTransition(Duration.millis(800), statusScreen);
        transition.setFromY(0);
        transition.setToY(-300);
        transition.play();
        transition.setOnFinished((e) -> statusScreen.setText(""));

        FadeTransition fadeTransition = new FadeTransition(Duration.millis(1000), statusScreen);
        fadeTransition.setFromValue(1.0);
        fadeTransition.setToValue(0.0);
        fadeTransition.play();
    }

    private boolean checkWin(GameStatus status) {
        if (status == null) return false;

        return status.equals(GameStatus.X_WIN) || status.equals(GameStatus.O_WIN);
    }

    private void updateGraphicBoard() {
        for (int i = 0; i < SIZE; i++) {
            for (int j = 0; j < SIZE; j++) {
                Tile tile = (Tile) boardGrid.getChildren().get(i * SIZE + j);
                if (gameBoard.getSymbol(i, j) == null) {
                    continue;
                }
                tile.setSymbol(gameBoard.getSymbol(i, j));
            }
        }
    }

    private void updateGameBoard() {
        for (int i = 0; i < SIZE; i++) {
            for (int j = 0; j < SIZE; j++) {
                Tile tile = (Tile) boardGrid.getChildren().get(i * SIZE + j);
                gameBoard.setPlace(tile.getSymbol(), i, j);
            }
        }
    }

    private GameStatus playAsComputer(Player computer) {
        computer.play(gameBoard);
        GameStatus status = judger.getStateAferPlay(gameBoard, gameBoard.getLastPlay()[0], gameBoard.getLastPlay()[1]);
        updateGraphicBoard();
        return status;
    }

    private void resetBoard() {
        for (int i = 0; i < SIZE; i++) {
            for (int j = 0; j < SIZE; j++) {
                gameBoard.setPlace(null, i, j);
            }
        }
    }

    public void returnBack(ActionEvent event) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("hello-view.fxml"));
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        Scene scene = new Scene(fxmlLoader.load(), 1140, 780);
        stage.setScene(scene);
    }
}