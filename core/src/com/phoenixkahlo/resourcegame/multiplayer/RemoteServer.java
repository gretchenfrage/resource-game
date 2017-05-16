package com.phoenixkahlo.resourcegame.multiplayer;

import java.util.Collection;

/**
 * Created by kahlo on 5/15/2017.
 */
public interface RemoteServer<W extends World<W>, C, S> {

    ContinuumLaunchPacket<W> getLaunchPacket();

}
