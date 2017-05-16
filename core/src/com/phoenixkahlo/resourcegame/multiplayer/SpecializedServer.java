package com.phoenixkahlo.resourcegame.multiplayer;

import com.phoenixkahlo.nodenet.LocalNode;
import com.phoenixkahlo.nodenet.proxy.Proxy;
import com.phoenixkahlo.resourcegame.util.ProxyFactory;

/**
 * Created by Phoenix on 5/15/2017.
 */
public interface SpecializedServer {

    Proxy<?> makeReceiver(Class<? extends WorldInteractor> interactorClass, ProxyFactory factory);

    void initializeSerialization(LocalNode network);

}
