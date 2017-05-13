package com.phoenixkahlo.resourcegame.coretest;

import com.phoenixkahlo.nodenet.NodeAddress;
import com.phoenixkahlo.resourcegame.core.ClientController;
import com.phoenixkahlo.resourcegame.core.ClientView;
import com.phoenixkahlo.resourcegame.core.World;
import com.phoenixkahlo.resourcegame.core.WorldMutator;

import java.util.stream.Stream;

/**
 * Created by Phoenix on 5/13/2017.
 */
public class CoreTestWorld implements World<CoreTestWorld> {

    @Override
    public Stream<WorldMutator<CoreTestWorld>> collectMutators() {
        return null;
    }

    @Override
    public ClientController<CoreTestWorld> getController(NodeAddress client) {
        return null;
    }

    @Override
    public ClientView<CoreTestWorld> getView(NodeAddress client) {
        return null;
    }

}
