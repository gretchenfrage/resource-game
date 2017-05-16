package com.phoenixkahlo.resourcegame.multiplayer;

/**
 * Created by Phoenix on 5/15/2017.
 */
public interface RemoteClient<W extends World<W, C>, C, S> {

    void provideInput(WorldInput<W, C> input);

}
