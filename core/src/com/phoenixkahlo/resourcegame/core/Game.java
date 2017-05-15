package com.phoenixkahlo.resourcegame.core;

import com.badlogic.gdx.ApplicationAdapter;

/**
 * A state based game that can be plugged into an LwjglApplication.
 * @param <G> the class of game - which should be itself.
 */
public abstract class Game extends ApplicationAdapter {

    private GameState state;
    private GameState nextState;

    private long lastRenderTime = -1;
    private float tickTimeDebt = 0;

    protected abstract GameState getInitialState();

    @Override
    public void create() {
        state = getInitialState();
        setNextState(state);
    }

    @Override
    public void render() {
        if (lastRenderTime == -1) {
            lastRenderTime = System.nanoTime();
        } else {
            // update the state
            long time = System.nanoTime();
            tickTimeDebt += (time - lastRenderTime) / 1_000_000_000.0f;
            float timePerTick = 1.0f / state.getTicksPerSecond();
            while (tickTimeDebt >= timePerTick) {
                state.update();
                tickTimeDebt -= timePerTick;
            }
            // render the state
            state.render();
            // prepare for next tick
            lastRenderTime = time;
            if (state != nextState) {
                state.onExit();
                state = nextState;
            }
        }
    }

    @Override
    public void resume() {
        lastRenderTime = System.nanoTime();
    }

    @Override
    public void dispose() {
        state.onExit();
    }

    public void setNextState(GameState nextState) {
        this.nextState = nextState;
    }

    public GameState getState() {
        return state;
    }

}
