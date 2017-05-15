package com.phoenixkahlo.resourcegame.core;

import com.phoenixkahlo.nodenet.NodeAddress;

import java.util.stream.Stream;

/**
 * A class of world that can be ticked to provide self-applicable reversible mutators. Used within a
 * WorldContinuum.
 * @param <W> the class of world - should be itself.
 */
public interface World<W extends World<W, C, S, RS>, C extends ClientState<W, C, S, RS>, S extends Server<W, C, S, RS>,
        RS extends RemoteServer<W, C, S, RS>> {

    /**
     * Collect all the WorldMutators for this tick based on predictable events.
     */
    Stream<? extends WorldMutator<W, C, S, RS>> getMutators();

    ClientController<W, C, S, RS> getController(NodeAddress client);

    ClientView<W, C, S, RS> getView(NodeAddress client);

    /**
     * May be called from a different thread.
     */
    ExternalWorldMutator<W, C, S, RS> onEnter(NodeAddress address, long time);

    /**
     * May be called from a different thread.
     */
    ExternalWorldMutator<W, C, S, RS> onLeave(NodeAddress address, long time);

}
