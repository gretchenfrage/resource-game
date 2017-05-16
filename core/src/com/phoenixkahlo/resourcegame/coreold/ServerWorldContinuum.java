package com.phoenixkahlo.resourcegame.coreold;

import com.phoenixkahlo.nodenet.proxy.Proxy;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.function.Consumer;
import java.util.function.Supplier;

/**
 * Created by Phoenix on 5/13/2017.
 */
public class ServerWorldContinuum<W extends World<W, C, S, RS>, C extends ClientState<W, C, S, RS>,
        S extends Server<W, C, S, RS>, RS extends RemoteServer<W, C, S, RS>> extends WorldContinuum<W, C, S, RS> {

    private long startTime;
    private Supplier<W> startWorldSupplier;

    private Collection<ExternalWorldMutator<W, C, S, RS>> externalMutatorCollector;
    private List<Proxy<Consumer<ExternalWorldMutator<W, C, S, RS>>>> externalMutatorListeners;

    public ServerWorldContinuum(long startTime, Supplier<W> startWorldSupplier) {
        this.startTime = startTime;
        this.startWorldSupplier = startWorldSupplier;

        externalMutatorCollector = new ArrayList<>();
        externalMutatorListeners = new ArrayList<>();
    }

    @Override
    protected long getStartTime() {
        return startTime;
    }

    @Override
    protected W downloadWorld(long time) throws NoSuchElementException {
        if (time == startTime)
            return startWorldSupplier.get();
        else
            throw new NoSuchElementException();
    }

    @Override
    protected synchronized Collection<ExternalWorldMutator<W, C, S, RS>> collectExternalMutators() {
        try {
            return externalMutatorCollector;
        } finally {
            externalMutatorCollector = new ArrayList<>();
        }
    }

    /**
     * Asynchronously make the mutator available for collection.
     */
    public synchronized void provideExternalMutator(ExternalWorldMutator<W, C, S, RS> mutator) {
        externalMutatorCollector.add(mutator);
        for (Proxy<Consumer<ExternalWorldMutator<W, C, S, RS>>> listener : externalMutatorListeners) {
            listener.unblocking(false).accept(mutator);
        }
    }

    public synchronized void listenForExternalMutators(Proxy<Consumer<ExternalWorldMutator<W, C, S, RS>>> listener) {
        externalMutatorListeners.add(listener);
    }

}
