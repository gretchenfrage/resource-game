package com.phoenixkahlo.resourcegame.multiplayer;

import com.phoenixkahlo.nodenet.NodeAddress;

import java.util.stream.Stream;

/**
 * World that will be synchronized accross client and server.
 */
public interface World<W extends World<W, C>, C> {

    /**
     * Stream the collection of reversible mutators that will advance
     * the world to the next tick.
     */
    Stream<? extends ReversibleMutator<W>> update();

    WorldInteractor<W, C> getInteractor(NodeAddress client);

    /**
     * Get the world input that handles the entrance of a client.
     */
    WorldInput<W, C> handleEnter(NodeAddress client);

    /**
     * Get the world input that handles the exit of a client.
     */
    WorldInput<W, C> handleLeave(NodeAddress client);

}
