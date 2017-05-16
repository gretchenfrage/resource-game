package com.phoenixkahlo.resourcegame.multiplayer;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Contains a world and can move it forwards or backwards through time. The world itself must be deterministic, so that
 * continuum on the clients and server can remain synchronized. Input to the world is handled by retroactively imposing
 * WorldInputs at a point in time. All events that are said to occur at a point in time, occur after the tick said to be
 * that point in time, not before.
 */
public class WorldContinuum<W extends World<W, C>, C> {

    private W world;
    private long time;
    private SortedMap<Long, Stack<ReversibleMutator<W>>> history;
    private SortedMap<Long, SortedSet<WorldInput<W, C>>> inputs;

    public synchronized void launch(ContinuumLaunchPacket<W, C> packet) {
        world = packet.getWorld();
        time = packet.getStartAtTime();
        history = new TreeMap<>();
        inputs = new TreeMap<>();
        for (WorldInput<W, C> input : packet.getInputs()) {
            SortedSet<WorldInput<W, C>> set = inputs.get(input.getTime());
            if (set == null) {
                set = new TreeSet<>(Comparator.comparingLong(WorldInput::getID));
                inputs.put(input.getTime(), set);
            }
            set.add(input);
        }
        advance(packet.getAdvanceToTime());
    }

    public synchronized void advance(long targetTime) {
        if (world == null)
            throw new IllegalStateException();
        while (targetTime > time) {
            Stack<ReversibleMutator<W>> stack = new Stack<>();
            Stream.concat(
                    inputs.getOrDefault(time, new TreeSet<>()).stream().flatMap(WorldInput::toMutators),
                    world.update()
            ).forEach(mutator -> {
                mutator.apply(world);
                stack.push(mutator);
            });
            history.put(time, stack);
            time++;
        }
    }

    public synchronized void revert(long targetTime) throws ForgottenHistoryException {
        if (world == null)
            throw new IllegalStateException();
        if (targetTime < history.firstKey())
            throw new ForgottenHistoryException();
        while (targetTime < time) {
            time--;
            Stack<ReversibleMutator<W>> stack = history.remove(time);
            while (!stack.isEmpty())
                stack.pop().unapply(world);
        }
    }

    public synchronized void applyInput(WorldInput<W, C> input) throws ForgottenHistoryException {
        revert(input.getTime());
        SortedSet<WorldInput<W, C>> set = inputs.get(input.getTime());
        if (set == null) {
            set = new TreeSet<>(Comparator.comparingLong(WorldInput::getID));
            inputs.put(input.getTime(), set);
        }
        set.add(input);
    }

    public synchronized void forget(long ticksToRemember) {
        history.headMap(time - ticksToRemember).clear();
        inputs.headMap(time - ticksToRemember).clear();
    }

    public synchronized W get() {
        return world;
    }

    public synchronized Collection<WorldInput<W, C>> getKnownInputs() {
        return inputs.values().stream()
                .flatMap(SortedSet::stream)
                .collect(Collectors.toList());
    }

    public synchronized long getEarliestRememberedTime() {
        return history.firstKey();
    }

}
