package com.phoenixkahlo.resourcegame.multiplayer.reversibles;

import com.phoenixkahlo.resourcegame.multiplayer.ReversibleMutator;

import java.util.Optional;
import java.util.function.BiConsumer;
import java.util.function.Function;
import java.util.function.UnaryOperator;

/**
 * Created by kahlo on 5/17/2017.
 */
public class PropertyMutator<E, P> implements ReversibleMutator<E> {

    private Function<E, P> getter;
    private BiConsumer<E, P> setter;
    private UnaryOperator<P> mutator;

    private P priorValue;

    public PropertyMutator(Function<E, P> getter, BiConsumer<E, P> setter, UnaryOperator<P> mutator) {
        this.getter = getter;
        this.setter = setter;
        this.mutator = mutator;
    }

    @Override
    public void apply(E obj) {
        priorValue = getter.apply(obj);
        setter.accept(obj, mutator.apply(priorValue));
    }

    @Override
    public void unapply(E obj) {
        setter.accept(obj, priorValue);
    }

}
