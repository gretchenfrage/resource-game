package com.phoenixkahlo.resourcegame.multiplayer;

import java.util.ArrayList;
import java.util.Collection;

/**
 * Created by kahlo on 5/15/2017.
 */
public class ServerWorldContinuum<W extends World<W>> extends WorldContinuumOld<W> {

    private WorldSupplier<W> supplier;
    private Collection<WorldInput<W>> inputCollector = new ArrayList<>();

    public ServerWorldContinuum(WorldSupplier<W> supplier) {
        super(1000);
        this.supplier = supplier;
    }

    @Override
    protected W getStartWorld() {
        return supplier.getStartWorld();
    }

    @Override
    protected long getStartTime() {
        return supplier.getStartTime();
    }

    @Override
    protected Collection<WorldInput<W>> getStartInputs() {
        return new ArrayList<>(0);
    }

    @Override
    protected long getStartTargetTime() {
        return supplier.getStartTime();
    }

    @Override
    protected synchronized Collection<WorldInput<W>> collectInputs() {
        try {
            return inputCollector;
        } finally {
            inputCollector = new ArrayList<>();
        }
    }

    @Override
    protected W downloadWorld(long targetTime) throws ForgottenHistoryException {
        throw new ForgottenHistoryException(); // There is nowhere to download it from
    }

    @Override
    protected Collection<WorldInput<W>> downloadInputs() {
        return new ArrayList<>(0);
    }

    /**
     * Make this input available to the main thread.
     */
    public synchronized void provideInput(WorldInput<W> input) {
        inputCollector.add(input);
    }

}
