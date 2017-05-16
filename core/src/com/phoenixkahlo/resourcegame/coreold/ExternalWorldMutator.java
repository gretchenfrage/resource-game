package com.phoenixkahlo.resourcegame.coreold;

import java.util.stream.Stream;

/**
 * An event that occurs within a world as a result of external input, and that is therefore unpredictable. It
 * applies its changes by generating a sequence of WorldMutators.
 */
public interface ExternalWorldMutator<W extends World<W, C, S, RS>, C extends ClientState<W, C, S, RS>,
        S extends Server<W, C, S, RS>, RS extends RemoteServer<W, C, S, RS>> {

    long getTime();

    /**
     * @return an ID used to order external world mutators.
     */
    long getID();

    Stream<? extends WorldMutator<W, C, S, RS>> toWorldMutators();

}
