package com.phoenixkahlo.resourcegame.core;

/**
 * One is assigned to each client in a world, and they're used to render the world.
 */
public interface ClientView<W extends World<W>> {

    void render();

}
