package com.phoenixkahlo.resourcegame.hub.entity;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Vector2;
import com.phoenixkahlo.resourcegame.hub.*;
import com.phoenixkahlo.resourcegame.hub.reversibles.EntityPropertySetter;
import com.phoenixkahlo.resourcegame.multiplayer.ReversibleMutator;

import java.util.stream.Stream;

/**
 * Created by Phoenix on 5/16/2017.
 */
public class Avatar extends AbstractEntity {

    private Vector2 position;
    private Vector2 velocity;

    private transient Sprite spriteCache = null;

    public Avatar() {
        position = new Vector2(0, 0);
        velocity = new Vector2(0, 0);
    }

    public Vector2 getPosition() {
        return position.cpy();
    }

    public Vector2 getVelocity() {
        return velocity.cpy();
    }

    public ReversibleMutator<HubWorld> setVelocity(Vector2 velocity) {
        return new EntityPropertySetter<Avatar, Vector2>(
                getID(),
                entity -> (Avatar) entity,
                Avatar::getVelocity,
                (avatar, vec) -> avatar.velocity = vec.cpy(),
                velocity
        );
    }

    public ReversibleMutator<HubWorld> setPosition(Vector2 position) {
        return new EntityPropertySetter<Avatar, Vector2>(
                getID(),
                entity -> (Avatar) entity,
                Avatar::getPosition,
                (avatar, vec) -> avatar.position = vec.cpy(),
                position
        );
    }

    @Override
    public Stream<ReversibleMutator<HubWorld>> update() {
        return Stream.of(
                setPosition(getPosition().add(getVelocity()))
        );
    }

    @Override
    public Stream<Sprite> getSprites(LocalHubClient client) {


    }

}
