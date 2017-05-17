package com.phoenixkahlo.resourcegame.util;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

/**
 * Created by Phoenix on 5/17/2017.
 */
public class EnumToMap {

    private EnumToMap() {}

    public static <E extends Enum<E>, K, V> Map<K, V> enumToMap(Class<E> clazz, Function<E, K> keyGetter,
                                                                Function<E, V> valueGetter) {
        Map<K, V> map = new HashMap<>();
        for (E constant : clazz.getEnumConstants())
            map.put(keyGetter.apply(constant), valueGetter.apply(constant));
        return map;
    }

}
