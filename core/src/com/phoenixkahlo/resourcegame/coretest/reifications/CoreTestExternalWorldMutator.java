package com.phoenixkahlo.resourcegame.coretest.reifications;

import com.phoenixkahlo.resourcegame.coreold.ExternalWorldMutator;
import com.phoenixkahlo.resourcegame.coretest.CoreTestClientState;
import com.phoenixkahlo.resourcegame.coretest.CoreTestRemoteServer;
import com.phoenixkahlo.resourcegame.coretest.CoreTestServer;
import com.phoenixkahlo.resourcegame.coretest.CoreTestWorld;

import java.util.stream.Stream;

/**
 * Created by Phoenix on 5/14/2017.
 */
public interface CoreTestExternalWorldMutator
        extends ExternalWorldMutator<CoreTestWorld, CoreTestClientState, CoreTestServer, CoreTestRemoteServer> {

    @Override
    Stream<? extends CoreTestWorldMutator> toWorldMutators();

}
