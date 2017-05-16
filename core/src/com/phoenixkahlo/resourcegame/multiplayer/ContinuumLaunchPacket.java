package com.phoenixkahlo.resourcegame.multiplayer;

import java.util.Collection;

/**
 * All the information needed to start or restart a world continuum
 */
public class ContinuumLaunchPacket<W extends World<W, C>, C> {

    private W world;
    private Collection<WorldInput<W, C>> inputs;
    private long startAtTime;
    private long advanceToTime;

    public ContinuumLaunchPacket(W world, Collection<WorldInput<W, C>> inputs, long startAtTime, long advanceToTime) {
        this.world = world;
        this.inputs = inputs;
        this.startAtTime = startAtTime;
        this.advanceToTime = advanceToTime;
    }

    public W getWorld() {
        return world;
    }

    public Collection<WorldInput<W, C>> getInputs() {
        return inputs;
    }

    public long getStartAtTime() {
        return startAtTime;
    }

    public long getAdvanceToTime() {
        return advanceToTime;
    }

}
