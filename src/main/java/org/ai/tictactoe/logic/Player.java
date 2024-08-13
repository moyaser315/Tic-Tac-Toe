package org.ai.tictactoe.logic.player;

import org.ai.tictactoe.logic.game.GameBoard;
import org.ai.tictactoe.logic.game.GameSymbol;

public abstract class Player {
    protected GameSymbol s;

    public Player(GameSymbol sym) {
        this.s = sym;
    }

    public abstract void play(GameBoard b);
}



