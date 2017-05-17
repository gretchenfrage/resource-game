package com.phoenixkahlo.resourcegame.hub.interactor;

import com.badlogic.gdx.math.Vector2;
import com.phoenixkahlo.nodenet.NodeAddress;
import com.phoenixkahlo.resourcegame.hub.HubClient;
import com.phoenixkahlo.resourcegame.hub.HubWorld;
import com.phoenixkahlo.resourcegame.multiplayer.ReversibleMutator;
import com.phoenixkahlo.resourcegame.multiplayer.WorldInput;

import java.util.function.Consumer;
import java.util.stream.Stream;

/**
 * Created by Phoenix on 5/16/2017.
 */
public class LocalAvatarInteractorReceiver implements AvatarInteractorReceiver {

    private NodeAddress address;
    private Consumer<WorldInput<HubWorld, HubClient>> inputter;

    public LocalAvatarInteractorReceiver(NodeAddress address, Consumer<WorldInput<HubWorld, HubClient>> inputter) {
        this.address = address;
        this.inputter = inputter;
    }

    @Override
    public void setVelocity(Vector2 velocity, long time) {
        inputter.accept(new WorldInput<HubWorld, HubClient>(time) {
            @Override
            public Stream<? extends ReversibleMutator<HubWorld>> toMutators(HubWorld world) {
                return Stream.of(world.getAvatar(address).setVelocity(velocity));
            }
        });
    }

}
