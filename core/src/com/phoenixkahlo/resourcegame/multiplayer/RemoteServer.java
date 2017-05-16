package com.phoenixkahlo.resourcegame.multiplayer;

import com.phoenixkahlo.nodenet.NodeAddress;
import com.phoenixkahlo.nodenet.proxy.Proxy;

import java.util.Collection;

/**
 * Remote proxy interface for a server.
 */
public interface RemoteServer<W extends World<W, C>, C, S> {

    ContinuumLaunchPacket<W, C> getLaunchPacket();

    long getTime();

    /**
     * Deactivate the receiver currently associated with that client (including destroying the proxy), ask the
     * specialized server for a new receiver based on the interactor class, and return a proxy of it.
     */
    Proxy<?> makeReceiver(NodeAddress address, Class<? extends WorldInteractor> interactorClass);

    void joinWorld(Proxy<RemoteClient<W, C, S>> client);

}
