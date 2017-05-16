package com.phoenixkahlo.resourcegame.coretest;

import com.badlogic.gdx.math.Vector2;
import com.phoenixkahlo.nodenet.NodeAddress;
import com.phoenixkahlo.nodenet.serialization.FieldSerializer;
import com.phoenixkahlo.nodenet.serialization.Serializer;
import com.phoenixkahlo.resourcegame.coreold.World;
import com.phoenixkahlo.resourcegame.coretest.externmutators.PlayerEnter;
import com.phoenixkahlo.resourcegame.coretest.externmutators.PlayerLeave;
import com.phoenixkahlo.resourcegame.coretest.reifications.CoreTestClientController;
import com.phoenixkahlo.resourcegame.coretest.reifications.CoreTestClientView;
import com.phoenixkahlo.resourcegame.coretest.reifications.CoreTestExternalWorldMutator;
import com.phoenixkahlo.resourcegame.coretest.reifications.CoreTestWorldMutator;

import java.util.HashMap;
import java.util.Map;
import java.util.stream.Stream;

/**
 * Created by Phoenix on 5/14/2017.
 */
public class CoreTestWorld implements World<CoreTestWorld, CoreTestClientState, CoreTestServer, CoreTestRemoteServer> {

    private Map<NodeAddress, Vector2> positions;

    public CoreTestWorld() {
        positions = new HashMap<>();
    }

    @Override
    public Stream<CoreTestWorldMutator> getMutators() {
        return Stream.empty();
    }

    @Override
    public CoreTestClientController getController(NodeAddress client) {
        return null;
    }

    @Override
    public CoreTestClientView getView(NodeAddress client) {
        return null;
    }

    @Override
    public CoreTestExternalWorldMutator onEnter(NodeAddress address, long time) {
        return new PlayerEnter(time, address);
    }

    @Override
    public CoreTestExternalWorldMutator onLeave(NodeAddress address, long time) {
        return new PlayerLeave(time, address);
    }

    public Map<NodeAddress, Vector2> getPositions() {
        return positions;
    }

    public static Serializer serializer(Serializer subSerializer) {
        return new FieldSerializer(CoreTestWorld.class, subSerializer, CoreTestWorld::new);
    }

}
