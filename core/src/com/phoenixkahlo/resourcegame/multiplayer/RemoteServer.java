package com.phoenixkahlo.resourcegame.multiplayer;

import java.util.Collection;

/**
 * Created by kahlo on 5/15/2017.
 */
public interface RemoteServer<W extends World<W>, C, S> {

    /**
     * Get the current world time.
     */
    long getTime();

    /**
     * Get the world state at that time.
     */
    W getWorld(long targetTime);


    Collection<WorldInput<W>> getInputs(long fromTime, long toTime);

}
