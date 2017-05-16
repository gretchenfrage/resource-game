package com.phoenixkahlo.resourcegame.core;

/**
 * A state within a Game.
 */
public interface GameState {

    void render();

    void update();

    int getTicksPerSecond();

    void onEnter();

    void onExit();

}
