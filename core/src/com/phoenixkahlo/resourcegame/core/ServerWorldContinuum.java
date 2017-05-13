package com.phoenixkahlo.resourcegame.core;

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
public class ServerWorldContinuum<W extends World<W>> extends WorldContinuum<W> {

    private long startTime;
    private Supplier<W> startWorldSupplier;

    private Collection<ExternalWorldMutator<W>> externalMutatorCollector;
    private List<Proxy<Consumer<ExternalWorldMutator<W>>>> externalMutatorListeners;

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
    protected synchronized Collection<ExternalWorldMutator<W>> collectExternalMutators() {
        try {
            return externalMutatorCollector;
        } finally {
            externalMutatorCollector = new ArrayList<>();
        }
    }

    /**
     * Asynchronously make the mutator available for collection.
     */
    public synchronized void provideExternalMutator(ExternalWorldMutator<W> mutator) {
        externalMutatorCollector.add(mutator);
        for (Proxy<Consumer<ExternalWorldMutator<W>>> listener : externalMutatorListeners) {
            listener.unblocking(false).accept(mutator);
        }
    }

    public synchronized void listenForExternalMutators(Proxy<Consumer<ExternalWorldMutator<W>>> listener) {
        externalMutatorListeners.add(listener);
    }

}
