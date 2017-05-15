package com.phoenixkahlo.resourcegame.coretest;

import com.phoenixkahlo.nodenet.LocalNode;
import com.phoenixkahlo.nodenet.proxy.Proxy;
import com.phoenixkahlo.resourcegame.core.ClientState;

/**
 * Created by Phoenix on 5/14/2017.
 */
public class CoreTestClientState
        extends ClientState<CoreTestWorld, CoreTestClientState, CoreTestServer, CoreTestRemoteServer> {

    public CoreTestClientState(LocalNode network, Proxy<CoreTestRemoteServer> server) {
        super(network, server);
    }

    @Override
    protected CoreTestClientState getSelf() {
        return this;
    }

}
