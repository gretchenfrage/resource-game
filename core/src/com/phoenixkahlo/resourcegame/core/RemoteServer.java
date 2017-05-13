package com.phoenixkahlo.resourcegame.core;

import com.phoenixkahlo.nodenet.NodeAddress;
import com.phoenixkahlo.nodenet.proxy.Proxy;

import java.util.function.Consumer;

/**
 * Proxy interface for the server.
 */
public interface RemoteServer<W extends World<W>> {

    long getTime();

    W downloadWorld(long time);

    void listenForExternalMutators(Proxy<Consumer<ExternalWorldMutator<W>>> receiver);

    Proxy<ClientControllerReceiver<W>> createReceiver(NodeAddress client, long time);

}
