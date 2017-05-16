package com.phoenixkahlo.resourcegame.multiplayer;

/**
 * Used on the server side for initializing the world continuum, can generate it or read it from a file.
 */
public interface WorldSupplier<W extends World<W>> {

    W getStartWorld();

    long getStartTime();

}
