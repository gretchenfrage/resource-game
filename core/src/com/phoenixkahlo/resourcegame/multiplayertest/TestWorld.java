package com.phoenixkahlo.resourcegame.multiplayertest;

import com.phoenixkahlo.nodenet.NodeAddress;
import com.phoenixkahlo.resourcegame.multiplayer.ReversibleMutator;
import com.phoenixkahlo.resourcegame.multiplayer.World;
import com.phoenixkahlo.resourcegame.multiplayer.WorldInput;
import com.phoenixkahlo.resourcegame.multiplayer.WorldInteractor;

import java.util.stream.Stream;

/**
 * Created by kahlo on 5/16/2017.
 */
public class TestWorld implements World<TestWorld, TestClient> {

    @Override
    public Stream<? extends ReversibleMutator<TestWorld>> update() {
        return null;
    }

    @Override
    public WorldInteractor<TestWorld, TestClient> getInteractor(NodeAddress client) {
        return null;
    }

    @Override
    public WorldInput<TestWorld, TestClient> handleEnter(NodeAddress client) {
        return null;
    }

    @Override
    public WorldInput<TestWorld, TestClient> handleLeave(NodeAddress client) {
        return null;
    }

}
