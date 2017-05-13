package com.phoenixkahlo.resourcegame.core;

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
public class ClientWorldContinuum<W extends World<W>> extends WorldContinuum<W> {

    private Proxy<RemoteServer<W>> server;
    private List<ExternalWorldMutator<W>> externalMutatorCollector;

    public ClientWorldContinuum(Proxy<RemoteServer<W>> server, LocalNode network) {
        this.server = server;
        server.blocking().listenForExternalMutators(network.makeProxy(
                this::provideExternalMutator,
                (Class<Consumer<ExternalWorldMutator<W>>>) (Object) Consumer.class
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
    protected synchronized Collection<ExternalWorldMutator<W>> collectExternalMutators() {
        try {
            return externalMutatorCollector;
        } finally {
            externalMutatorCollector = new ArrayList<>();
        }
    }

    private synchronized void provideExternalMutator(ExternalWorldMutator<W> mutator) {
        externalMutatorCollector.add(mutator);
    }

}
