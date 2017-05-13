package com.phoenixkahlo.resourcegame.core;

import com.badlogic.gdx.ApplicationAdapter;

/**
 * A state based game that can be plugged into an LwjglApplication.
 * @param <G> the class of game - which should be itself.
 */
public abstract class Game<G extends Game<G>> extends ApplicationAdapter {

    private GameState<G> state;
    private GameState<G> nextState;

    private long lastRenderTime = -1;
    private float tickTimeDebt = 0;

    protected abstract G getGame();

    protected abstract GameState<G> getInitialState();

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
                state.update(getGame());
                tickTimeDebt -= timePerTick;
            }
            // render the state
            state.render(getGame());
            // prepare for next tick
            lastRenderTime = time;
            if (state != nextState) {
                state.onExit(getGame());
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
        state.onExit(getGame());
    }

    public void setNextState(GameState<G> nextState) {
        this.nextState = nextState;
    }

    public GameState<G> getState() {
        return state;
    }

}
