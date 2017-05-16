package com.phoenixkahlo.resourcegame.multiplayer;

import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.Stream;

/**
 * Represents external input into a world, which propagates itself into the world through the production of mutators.
 */
public abstract class WorldInput<W extends World<W, C>, C> {

    private long time;
    private long id;

    public WorldInput(long time, long id) {
        this.time = time;
        this.id = id;
    }

    public WorldInput(long time) {
        this(time, ThreadLocalRandom.current().nextLong());
    }

    public long getTime() {
        return time;
    }

    public long getID() {
        return id;
    }

    public abstract Stream<? extends ReversibleMutator<W>> toMutators();

}
