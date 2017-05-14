package com.phoenixkahlo.resourcegame.core;

/**
 * A state within a Game.
 * @param <G> the class of Game.
 */
public interface GameState {

    void render();

    void update();

    int getTicksPerSecond();

    void onEnter();

    void onExit();

}
