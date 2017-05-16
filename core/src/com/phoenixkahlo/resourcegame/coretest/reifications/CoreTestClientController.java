package com.phoenixkahlo.resourcegame.coretest.reifications;

import com.phoenixkahlo.resourcegame.coreold.ClientController;
import com.phoenixkahlo.resourcegame.coretest.CoreTestClientState;
import com.phoenixkahlo.resourcegame.coretest.CoreTestRemoteServer;
import com.phoenixkahlo.resourcegame.coretest.CoreTestServer;
import com.phoenixkahlo.resourcegame.coretest.CoreTestWorld;

/**
 * Created by Phoenix on 5/14/2017.
 */
public interface CoreTestClientController
        extends ClientController<CoreTestWorld, CoreTestClientState, CoreTestServer, CoreTestRemoteServer> {

    @Override
    CoreTestClientControllerReceiver toReceiver(CoreTestServer server);

}
