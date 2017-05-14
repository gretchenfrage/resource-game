package com.phoenixkahlo.resourcegame.core;

/**
 * Abstract implementation of a GameState.
 */
public abstract class AbstractGameState implements GameState {

    private int ticksPerSecond;

    public AbstractGameState(int ticksPerSecond) {
        this.ticksPerSecond = ticksPerSecond;
    }

    public AbstractGameState() {
        this(60);
    }

    @Override
    public int getTicksPerSecond() {
        return ticksPerSecond;
    }

    @Override
    public void onEnter() {}

    @Override
    public void onExit() {}

}
