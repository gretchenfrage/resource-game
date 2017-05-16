package com.phoenixkahlo.resourcegame.multiplayer.reversibles;

import com.phoenixkahlo.resourcegame.multiplayer.ReversibleMutator;

import java.util.Map;
import java.util.Optional;
import java.util.function.Function;

/**
 * Created by Phoenix on 5/16/2017.
 */
public class MapRemove<E, K, V> implements ReversibleMutator<E> {

    private Function<E, Map<K, V>> getter;
    private K key;

    private Optional<V> priorValue;

    public MapRemove(Function<E, Map<K, V>> getter, K key) {
        this.getter = getter;
        this.key = key;
    }

    @Override
    public void apply(E obj) {
        Map<K, V> map = getter.apply(obj);
        if (map.containsKey(key))
            priorValue = Optional.of(map.remove(key));
        else
            priorValue = Optional.empty();
    }

    @Override
    public void unapply(E obj) {
        if (priorValue.isPresent())
            getter.apply(obj).put(key, priorValue.get());
    }

}
