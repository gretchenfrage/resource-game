package com.phoenixkahlo.resourcegame.multiplayer;

import java.util.*;

/**
 * An abstract container of a world that initializes it from a
 * source and advances it forward or backwards in time.
 *
 * The synchronization of continuums depends on the world being
 * internally determinstic. That is to say that the save world, updated
 * the same amount of times, will result in the same world, as long as there are no external inputs. For the case of
 * external inputs, the continuum will retroactively apply those inputs into the world.
 *
 * Inputs are applied after the tick that is referenced by their time
 * property, not before.
 */
public abstract class WorldContinuumOld<W extends World<W>> {

    private final long remember;

    private W world;
    private long time;
    private long revertibleTime;

    private SortedMap<Long, Stack<ReversibleMutator<W>>> applied;
    private SortedMap<Long, SortedSet<WorldInput<W>>> inputs;

    /**
     * The real initialization is done in the init method.
     * This is because subclasses may have to be constructed
     * before this class can initialize.
     */
    public WorldContinuumOld(long remember) {
        this.remember = remember;
    }

    protected abstract ContinuumLaunchPacket<W> getLaunchPacket();

    public void init() {
        /*
        world = getStartWorld();
        time = getStartTime();
        revertibleTime = time;
        for (WorldInput<W> input : getStartInputs())
            applyInput(input);
        updateKnownInputs();
        */
    }

    /**
     * Collect all the inputs that have been made available by side threads since the last invocation of this method.
     */
    protected abstract Collection<WorldInput<W>> collectInputs();

    /**
     * Attempt to download the world state at this time.
     */
    protected abstract W downloadWorld(long targetTime) throws ForgottenHistoryException;

    /**
     * Attempt to download all known inputs.
     */
    protected abstract Collection<WorldInput<W>> downloadInputs();

    /**
     * Get all inputs known to this continuum. This is a thread safe method.
     */
    public Collection<WorldInput<W>> getKnownInputs() {
        return knownInputs;
    }

    private void revert(long targetTime) throws ForgottenHistoryException {
        if (targetTime < revertibleTime) {
            world = downloadWorld(targetTime);
            for (WorldInput<W> input : downloadInputs())
                applyInput(input);
            time = targetTime;
        } else {
            while (targetTime < time) {
                time--;
                Stack<ReversibleMutator<W>> stack = applied.remove(time);
                if (stack != null)
                    while (!stack.isEmpty())
                        stack.pop().unapply(world);
            }
        }
    }

    private void advance(long targetTime) {
        while (targetTime > time) {
            SortedSet<WorldInput<W>> apply = inputs.remove(time);
            if (apply != null) {
                Stack<ReversibleMutator<W>> stack = new Stack<>();
                apply.stream()
                        .flatMap(WorldInput::toMutators)
                        .forEach(mutator -> {
                            mutator.apply(world);
                            stack.push(mutator);
                        });
                applied.put(time, stack);
            }
            time++;
        }
    }

    private boolean applyInput(WorldInput<W> input) {
        try {
            revert(input.getTime());
            SortedSet<WorldInput<W>> set = inputs.get(input.getTime());
            if (set == null) {
                set = new TreeSet<>(Comparator.comparingLong(WorldInput::getID));
                inputs.put(input.getTime(), set);
            }
            set.add(input);
            return true;
        } catch (ForgottenHistoryException e) {
            return false;
        }
    }

    /**
     * Forget information pertaining to reversion to states that occured overly long ago.
     */
    public void prune() {
        revertibleTime = time - remember;
        applied.tailMap(revertibleTime).clear();
        inputs.tailMap(revertibleTime).clear();
    }

    /**
     * Bring the continuum to that time with as much information as possible.
     */
    public void makeTime(long targetTime) throws ForgottenHistoryException {
        for (WorldInput<W> input : collectInputs())
            applyInput(input);
        revert(targetTime);
        advance(targetTime);
    }

    /**
     * Get the contained world in its current state.
     */
    public W getWorld() {
        return world;
    }

}
