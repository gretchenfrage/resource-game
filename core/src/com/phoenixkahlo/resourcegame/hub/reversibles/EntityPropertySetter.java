package com.phoenixkahlo.resourcegame.hub.reversibles;

import com.badlogic.gdx.math.Vector2;
import com.phoenixkahlo.resourcegame.hub.HubWorld;
import com.phoenixkahlo.resourcegame.hub.entity.Avatar;
import com.phoenixkahlo.resourcegame.hub.entity.Entity;
import com.phoenixkahlo.resourcegame.multiplayer.reversibles.PropertySetter;

import java.util.UUID;
import java.util.function.BiConsumer;
import java.util.function.Function;

/**
 * Created by kahlo on 5/17/2017.
 */
public class EntityPropertySetter<T, P> extends PropertySetter<HubWorld, P> {

    public EntityPropertySetter(UUID id, Function<Entity, T> caster, Function<T, P> getter, BiConsumer<T, P> setter, P value) {
        super(
                world -> getter.apply(caster.apply(world.getEntity(id))),
                (world, setTo) -> setter.accept(caster.apply(world.getEntity(id)), setTo),
                value
        );
    }


}
