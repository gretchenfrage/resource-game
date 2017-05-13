package com.phoenixkahlo.resourcegame.coretest;

import com.phoenixkahlo.nodenet.LocalNode;
import com.phoenixkahlo.nodenet.proxy.Proxy;
import com.phoenixkahlo.resourcegame.core.ClientState;
import com.phoenixkahlo.resourcegame.core.RemoteServer;

/**
 * Created by Phoenix on 5/13/2017.
 */
public class CoreTestClientState extends ClientState<CoreTestGame, CoreTestWorld> {

    public CoreTestClientState(LocalNode network, Proxy<RemoteServer<CoreTestWorld>> server) {
        super(network, server);
    }

}
