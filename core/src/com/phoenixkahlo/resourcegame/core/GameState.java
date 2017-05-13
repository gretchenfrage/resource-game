package com.phoenixkahlo.resourcegame.core;

/**
 * A state within a Game.
 * @param <G> the class of Game.
 */
public interface GameState<G extends Game<G>> {

    void render(G game);

    void update(G game);

    int getTicksPerSecond();

    void onEnter(G game);

    void onExit(G game);

}
