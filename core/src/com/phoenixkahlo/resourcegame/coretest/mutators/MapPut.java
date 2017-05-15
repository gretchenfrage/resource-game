package com.phoenixkahlo.resourcegame.coretest.mutators;

import com.phoenixkahlo.resourcegame.coretest.CoreTestWorld;
import com.phoenixkahlo.resourcegame.coretest.reifications.CoreTestWorldMutator;

import java.util.Map;
import java.util.Optional;
import java.util.function.Function;

/**
 * Created by Phoenix on 5/14/2017.
 */
public class MapPut<K, V> implements CoreTestWorldMutator {

    private Function<CoreTestWorld, Map<K, V>> getter;
    private K key;
    private V value;

    private transient Optional<V> priorValue;

    public MapPut(Function<CoreTestWorld, Map<K, V>> getter, K key, V value) {
        this.getter = getter;
        this.key = key;
        this.value = value;
    }

    @Override
    public boolean prepare(CoreTestWorld world) {
        Map<K, V> map = getter.apply(world);
        if (map.containsKey(key))
            priorValue = Optional.of(map.get(key));
        else
            priorValue = Optional.empty();
        return true;
    }

    @Override
    public void apply(CoreTestWorld world) {
        getter.apply(world).put(key, value);
    }

    @Override
    public void unapply(CoreTestWorld world) {
        if (priorValue.isPresent())
            getter.apply(world).put(key, priorValue.get());
        else
            getter.apply(world).remove(key);
    }

}
