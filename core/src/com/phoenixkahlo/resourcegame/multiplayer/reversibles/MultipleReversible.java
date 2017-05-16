package com.phoenixkahlo.resourcegame.multiplayer.reversibles;

import com.phoenixkahlo.resourcegame.multiplayer.ReversibleMutator;

import java.util.Arrays;
import java.util.List;

/**
 * Created by Phoenix on 5/16/2017.
 */
public class MultipleReversible<E> implements ReversibleMutator<E> {

    private List<ReversibleMutator<E>> mutators;

    public MultipleReversible(List<ReversibleMutator<E>> mutators) {
        this.mutators = mutators;
    }

    public MultipleReversible(ReversibleMutator<E>... mutators) {
        this(Arrays.asList(mutators));
    }

    @Override
    public void apply(E obj) {
        for (ReversibleMutator<E> mutator : mutators)
            mutator.apply(obj);
    }

    @Override
    public void unapply(E obj) {
        for (int i = mutators.size() - 1; i >= 0; i--) {
            mutators.get(i).unapply(obj);
        }
    }

}
