package com.phoenixkahlo.resourcegame.coretest.externmutators;

import com.badlogic.gdx.math.Vector2;
import com.phoenixkahlo.nodenet.NodeAddress;
import com.phoenixkahlo.resourcegame.core.WorldMutator;
import com.phoenixkahlo.resourcegame.coretest.CoreTestClientState;
import com.phoenixkahlo.resourcegame.coretest.CoreTestRemoteServer;
import com.phoenixkahlo.resourcegame.coretest.CoreTestServer;
import com.phoenixkahlo.resourcegame.coretest.CoreTestWorld;
import com.phoenixkahlo.resourcegame.coretest.mutators.MapPut;
import com.phoenixkahlo.resourcegame.coretest.reifications.CoreTestExternalWorldMutator;
import com.phoenixkahlo.resourcegame.coretest.reifications.CoreTestWorldMutator;

import java.util.stream.Stream;

/**
 * Created by Phoenix on 5/14/2017.
 */
public class PlayerEnter extends AbstractCoreTestExternMutator {

    private NodeAddress address;

    public PlayerEnter(long time, NodeAddress address) {
        super(time);
        this.address = address;
    }

    @Override
    protected CoreTestWorldMutator toWorldMutator() {
        return new MapPut<NodeAddress, Vector2>(
                CoreTestWorld::getPositions,
                address,
                new Vector2(0, 0)
        );
    }

}
