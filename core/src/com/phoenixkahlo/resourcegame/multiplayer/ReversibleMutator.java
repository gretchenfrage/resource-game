package com.phoenixkahlo.resourcegame.multiplayer;

/**
 * Created by kahlo on 5/15/2017.
 */
public interface ReversibleMutator<E> {

    void apply(E obj);

    void unapply(E obj);

}
