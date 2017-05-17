package com.phoenixkahlo.resourcegame.multiplayer.reversibles;

import java.util.function.BiConsumer;
import java.util.function.Function;

/**
 * Created by kahlo on 5/17/2017.
 */
public class PropertySetter<E, P> extends PropertyMutator<E, P> {

    public PropertySetter(Function<E, P> getter, BiConsumer<E, P> setter, P newValue) {
        super(
                getter,
                setter,
                oldValue -> newValue
        );
    }

}
