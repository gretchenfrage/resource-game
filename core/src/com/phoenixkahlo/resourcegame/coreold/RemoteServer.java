package com.phoenixkahlo.resourcegame.coreold;

import com.phoenixkahlo.nodenet.NodeAddress;
import com.phoenixkahlo.nodenet.proxy.Proxy;

import java.util.function.Consumer;

/**
 * Proxy interface for the server.
 */
public interface RemoteServer<W extends World<W, C, S, RS>, C extends ClientState<W, C, S, RS>,
        S extends Server<W, C, S, RS>, RS extends RemoteServer<W, C, S, RS>> {

    long getTime();

    W downloadWorld(long time);

    void listenForExternalMutators(Proxy<Consumer<ExternalWorldMutator<W, C, S, RS>>> receiver);

    Proxy<ClientControllerReceiver<W, C, S, RS>> createReceiver(NodeAddress client, long time);

    //TODO: make this more secure
    void join(NodeAddress address);

    //TODO: make this more secure
    void leave(NodeAddress address);

}
