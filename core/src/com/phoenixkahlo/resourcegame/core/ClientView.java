package com.phoenixkahlo.resourcegame.core;

/**
 * One is assigned to each client in a world, and they're used to render the world.
 */
public interface ClientView<W extends World<W, C, S, RS>, C extends ClientState<W, C, S, RS>,
        S extends Server<W, C, S, RS>, RS extends RemoteServer<W, C, S, RS>> {

    void render(C client);

}
