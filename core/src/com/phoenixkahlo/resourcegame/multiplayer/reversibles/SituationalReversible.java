package com.phoenixkahlo.resourcegame.multiplayer.reversibles;

import com.phoenixkahlo.resourcegame.multiplayer.ReversibleMutator;

import java.util.Optional;
import java.util.function.Function;
import java.util.function.Predicate;

/**
 * Created by Phoenix on 5/16/2017.
 */
public class SituationalReversible<E> implements ReversibleMutator<E> {

    private Predicate<E> condition;
    private Function<E, ReversibleMutator<E>> evaluator;

    private Optional<ReversibleMutator<E>> applied;

    public SituationalReversible(Predicate<E> condition, Function<E, ReversibleMutator<E>> evaluator) {
        this.condition = condition;
        this.evaluator = evaluator;
    }

    @Override
    public void apply(E obj) {
        if (condition.test(obj)) {
            applied = Optional.of(evaluator.apply(obj));
            applied.get().apply(obj);
        } else {
            applied = Optional.empty();
        }
    }

    @Override
    public void unapply(E obj) {
        if (applied.isPresent())
            applied.get().unapply(obj);
    }

}
