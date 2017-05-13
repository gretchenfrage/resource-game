package com.phoenixkahlo.resourcegame.core;

import com.phoenixkahlo.nodenet.NodeAddress;

import java.util.Collection;
import java.util.List;
import java.util.stream.Stream;

/**
 * A class of world that can be ticked to provide self-applicable reversible mutators. Used within a
 * WorldContinuum.
 * @param <W> the class of world - should be itself.
 */
public interface World<W extends World<W>> {

    /**
     * Collect all the WorldMutators for this tick based on predictable events.
     */
    Stream<WorldMutator<W>> collectMutators();

    ClientController<W> getController(NodeAddress client);

    ClientView<W> getView(NodeAddress client);

}
