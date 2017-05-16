package com.phoenixkahlo.resourcegame.coreold;

/**
 * A reversible mutator for a world.
 */
public interface WorldMutator<W extends World<W, C, S, RS>, C extends ClientState<W, C, S, RS>,
        S extends Server<W, C, S, RS>, RS extends RemoteServer<W, C, S, RS>> {

    /**
     * Make any final captures of the world state, and return whether the mutator still wants to mutate the world.
     */
    boolean prepare(W world);

    /**
     * Apply the changes to the world, assuming prepare has already been called.
     */
    void apply(W world);

    /**
     * Revert the changes that have been applied to the world, assuming prepare has already been called on this object.
     */
    void unapply(W world);

}
