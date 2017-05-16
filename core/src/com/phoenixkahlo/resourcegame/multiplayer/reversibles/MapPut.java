package com.phoenixkahlo.resourcegame.multiplayer.reversibles;

import com.phoenixkahlo.resourcegame.multiplayer.ReversibleMutator;

import javax.swing.text.html.Option;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;

/**
 * Created by Phoenix on 5/16/2017.
 */
public class MapPut<E, K, V> implements ReversibleMutator<E> {

    private Function<E, Map<K, V>> getter;
    private K key;
    private V value;

    private Optional<V> priorValue;

    public MapPut(Function<E, Map<K, V>> getter, K key, V value) {
        this.getter = getter;
        this.key = key;
        this.value = value;
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
        else
            getter.apply(obj).remove(key);
    }

}
