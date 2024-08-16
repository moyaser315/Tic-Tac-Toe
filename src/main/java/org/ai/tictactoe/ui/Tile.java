package org.ai.tictactoe.ui;

import javafx.geometry.Pos;
import javafx.scene.effect.DropShadow;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import org.ai.tictactoe.logic.game.GameSymbol;


public class Tile extends StackPane {
    private GameSymbol symbol;
    private int x, y;
    private Text text = new Text();

    private boolean xTurn = true;


    public Tile(int x, int y) {
        this.x = x;
        this.y = y;

        Rectangle border = new Rectangle(75, 75);
        border.setFill(Color.rgb(45, 45, 48)); // Dark background color
        border.setArcWidth(15); // Rounded corners
        border.setArcHeight(15);

        // Add a drop shadow effect
        DropShadow shadow = new DropShadow();
        shadow.setColor(Color.BLACK);
        shadow.setOffsetX(2);
        shadow.setOffsetY(2);
        shadow.setRadius(5);
        border.setEffect(shadow);

        text.setFont(Font.font("Roboto", FontWeight.EXTRA_BOLD, 36));

        // text.setFill(Color.WHITE); // White text color for better contrast

        setAlignment(Pos.CENTER);
        getChildren().addAll(border, text);

        setOnMouseClicked(this::handleClick);
    }

    private void handleClick(MouseEvent mouseEvent) {
        if (isxTurn())
            setSymbol(GameSymbol.X);
        else
            setSymbol(GameSymbol.O);
    }

    public GameSymbol getSymbol() {
        return symbol;
    }

    public void setSymbol(GameSymbol symbol) {
        if (this.symbol == null) {
            this.symbol = symbol;
            if (symbol.equals(GameSymbol.X)) {
                text.setText("X");
                text.setFill(Color.web("#007AFF"));
            } else {
                text.setText("O");
                text.setFill(Color.web("#FF3B30"));
            }
        }
    }

    public boolean isxTurn() {
        return xTurn;
    }

    public void setxTurn(boolean xTurn) {
        this.xTurn = xTurn;
    }

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }
}