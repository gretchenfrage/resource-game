package com.phoenixkahlo.resourcegame.hub.entity;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Vector2;
import com.phoenixkahlo.resourcegame.hub.*;
import com.phoenixkahlo.resourcegame.multiplayer.ReversibleMutator;

import java.util.UUID;
import java.util.stream.Stream;

/**
 * Created by Phoenix on 5/16/2017.
 */
public class Avatar extends AbstractEntity {

    private Vector2 position;
    private Vector2 velocity;

    public Avatar() {
        position = new Vector2(0, 0);
        velocity = new Vector2(0, 0);
    }

    public ReversibleMutator<HubWorld> setMovement(Vector2 direction, boolean running) {
        return new ReversibleMutator<HubWorld>() {
            private UUID avatarID = Avatar.this.getID();

            private Vector2 previousVelocity;

            @Override
            public void apply(HubWorld world) {

            }

            @Override
            public void unapply(HubWorld world) {

            }
        };
    }

    @Override
    public Stream<ReversibleMutator<HubWorld>> update() {
        return null;
    }

    @Override
    public Stream<Sprite> getSprites(LocalHubClient client) {
        return null;
    }

}
