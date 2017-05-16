package com.phoenixkahlo.resourcegame.coretest.reifications;

import com.phoenixkahlo.resourcegame.coreold.ClientControllerReceiver;
import com.phoenixkahlo.resourcegame.coretest.CoreTestClientState;
import com.phoenixkahlo.resourcegame.coretest.CoreTestRemoteServer;
import com.phoenixkahlo.resourcegame.coretest.CoreTestServer;
import com.phoenixkahlo.resourcegame.coretest.CoreTestWorld;

/**
 * Created by Phoenix on 5/14/2017.
 */
public interface CoreTestClientControllerReceiver
        extends ClientControllerReceiver<CoreTestWorld, CoreTestClientState, CoreTestServer, CoreTestRemoteServer> {

    @Override
    Class<? extends CoreTestClientControllerReceiver> getRemoteInterface();

}
