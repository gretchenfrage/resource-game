package com.phoenixkahlo.resourcegame.coreold;

import com.phoenixkahlo.nodenet.LocalNode;
import com.phoenixkahlo.nodenet.proxy.Proxy;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.function.Consumer;

/**
 * An implementation of the WorldContinuum that conforms to a server world.
 */
public class ClientWorldContinuum<W extends World<W, C, S, RS>, C extends ClientState<W, C, S, RS>,
        S extends Server<W, C, S, RS>, RS extends RemoteServer<W, C, S, RS>> extends WorldContinuum<W, C, S, RS> {

    private Proxy<? extends RemoteServer<W, C, S, RS>> server;
    private List<ExternalWorldMutator<W, C, S, RS>> externalMutatorCollector;

    public ClientWorldContinuum(Proxy<? extends RemoteServer<W, C, S, RS>> server, LocalNode network) {
        this.server = server;
        server.blocking().listenForExternalMutators(network.makeProxy(
                this::provideExternalMutator,
                (Class<Consumer<ExternalWorldMutator<W, C, S, RS>>>) (Object) Consumer.class
        ));

    }
    @Override
    protected long getStartTime() {
        return server.blocking().getTime();
    }

    @Override
    protected W downloadWorld(long time) throws NoSuchElementException {
        return server.blocking().downloadWorld(time);
    }

    @Override
    protected synchronized Collection<ExternalWorldMutator<W, C, S, RS>> collectExternalMutators() {
        try {
            return externalMutatorCollector;
        } finally {
            externalMutatorCollector = new ArrayList<>();
        }
    }

    private synchronized void provideExternalMutator(ExternalWorldMutator<W, C, S, RS> mutator) {
        externalMutatorCollector.add(mutator);
    }

}
