package com.phoenixkahlo.resourcegame.coretest.externmutators;

import com.phoenixkahlo.resourcegame.coretest.reifications.CoreTestExternalWorldMutator;
import com.phoenixkahlo.resourcegame.coretest.reifications.CoreTestWorldMutator;

import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.Stream;

/**
 * Created by Phoenix on 5/14/2017.
 */
public abstract class AbstractCoreTestExternMutator implements CoreTestExternalWorldMutator {

    private long time;
    private long id;

    public AbstractCoreTestExternMutator(long time, long id) {
        this.time = time;
        this.id = id;
    }

    public AbstractCoreTestExternMutator(long time) {
        this(time, ThreadLocalRandom.current().nextLong());
    }

    @Override
    public long getTime() {
        return time;
    }

    @Override
    public long getID() {
        return id;
    }

    protected abstract CoreTestWorldMutator toWorldMutator();

    @Override
    public Stream<? extends CoreTestWorldMutator> toWorldMutators() {
        return Stream.of(toWorldMutator());
    }
}
