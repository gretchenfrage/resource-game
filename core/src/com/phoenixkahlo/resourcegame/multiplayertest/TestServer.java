package com.phoenixkahlo.resourcegame.multiplayertest;

import com.phoenixkahlo.nodenet.LocalNode;
import com.phoenixkahlo.nodenet.proxy.Proxy;
import com.phoenixkahlo.resourcegame.multiplayer.SpecializedServer;
import com.phoenixkahlo.resourcegame.multiplayer.WorldInteractor;
import com.phoenixkahlo.resourcegame.util.ProxyFactory;

/**
 * Created by kahlo on 5/16/2017.
 */
public class TestServer implements SpecializedServer {

    @Override
    public Proxy<?> makeReceiver(Class<? extends WorldInteractor> interactorClass, ProxyFactory factory) {
        return null;
    }

    @Override
    public void initializeSerialization(LocalNode network) {

    }

    public static void launch() throws Exception {

    }

}
