package com.phoenixkahlo.resourcegame.core;

import java.util.*;

/**
 * A container for a world that can present itself at discrete units of time, and impose
 * events on the world at arbitrary points in time.
 *
 * Mutators that are scheduled for a certain time, are applied before that frame in time, not after.
 *
 * @param <W> the class of world.
 */
public abstract class WorldContinuum<W extends World<W, C, S, RS>, C extends ClientState<W, C, S, RS>,
        S extends Server<W, C, S, RS>, RS extends RemoteServer<W, C, S, RS>> {

    private W world;
    private long time;
    private long lastRevertibleTime;
    private SortedMap<Long, List<WorldMutator<W, C, S, RS>>> appliedMutators;
    private SortedMap<Long, SortedSet<ExternalWorldMutator<W, C, S, RS>>> externalMutators;
    private long tickToRemember;

    public WorldContinuum(long tickToRemember) {
        appliedMutators = new TreeMap<>();
        externalMutators = new TreeMap<>();
        time = getStartTime();
        lastRevertibleTime = time;
        world = downloadWorld(time);
        this.tickToRemember = tickToRemember;
    }

    public WorldContinuum() {
        this(600);
    }

    /**
     * Get the time that this continuum starts as. This time should be downloadeable.
     */
    protected abstract long getStartTime();

    /**
     * Try to get the world at this time, assuming that it cannot revert to it. Perhaps a client implementation
     * would download this from a server.
     */
    protected abstract W downloadWorld(long time) throws NoSuchElementException;

    /**
     * Collect all the external mutators that have accumulated since the last invocation of this method. Perhaps a
     * client implementation would empty a buffer that it had been asynchronously filling from a server.
     */
    protected abstract Collection<ExternalWorldMutator<W, C, S, RS>> collectExternalMutators();

    /**
     * If the target time is less than the current time, revert to that time.
     */
    private void revert(long targetTime) throws NoSuchElementException {
        if (targetTime < lastRevertibleTime) {
            world = downloadWorld(targetTime);
            time = targetTime;
        } else {
            while (targetTime < time) {
                List<WorldMutator<W, C, S, RS>> toUnapply = appliedMutators.remove(time);
                if (toUnapply != null) {
                    for (int i = toUnapply.size() - 1; i >= 0; i--) {
                        toUnapply.get(i).unapply(world);
                    }
                }
                time--;
            }
        }
    }

    /**
     * If the target time is greater than the current time, advance to that time.
     */
    private void advance(long targetTime) {
        while (targetTime > time) {
            List<WorldMutator<W, C, S, RS>> toApply = new ArrayList<>();
            SortedSet<ExternalWorldMutator<W, C, S, RS>> externs = externalMutators.get(time + 1);
            if (externs != null)
                externs.stream().flatMap(ExternalWorldMutator::toWorldMutators).forEach(toApply::add);
            world.getMutators().forEach(toApply::add);
            int i = 0;
            while (i < toApply.size()) {
                if (toApply.get(i).prepare(world)) {
                    toApply.get(i).apply(world);
                    i++;
                } else {
                    toApply.remove(i);
                }
            }
            appliedMutators.put(time + 1, toApply);
            time++;
        }
    }

    /**
     * Add the external mutator to this continuum, reverting if necessary.
     */
    private void addExternalMutator(ExternalWorldMutator<W, C, S, RS> mutator) {
        revert(mutator.getTime() - 1);
        SortedSet<ExternalWorldMutator<W, C, S, RS>> set = externalMutators.get(mutator.getTime());
        if (set == null) {
            set = new TreeSet<>(Comparator.comparingLong(ExternalWorldMutator::getID));
            externalMutators.put(mutator.getTime(), set);
        }
        set.add(mutator);
    }

    /**
     * Collect external mutators and add them.
     */
    private void updateExternalMutators() {
        for (ExternalWorldMutator<W, C, S, RS> mutator : collectExternalMutators()) {
            addExternalMutator(mutator);
        }
    }

    public long getCurrentTime() {
        return time;
    }

    /**
     * Bring the world to this time frame, and then return it.
     */
    public W getWorld(long targetTime) {
        updateExternalMutators();
        revert(targetTime);
        advance(targetTime);
        return world;
    }

}
