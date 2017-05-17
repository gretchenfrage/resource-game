package com.phoenixkahlo.resourcegame.multiplayer;

import com.phoenixkahlo.nodenet.LocalNode;
import com.phoenixkahlo.nodenet.NodeAddress;
import com.phoenixkahlo.nodenet.proxy.Proxy;
import com.phoenixkahlo.resourcegame.util.ProxyFactory;

import java.util.Collection;
import java.util.function.Consumer;
import java.util.function.Supplier;

/**
 * Object for game specific logic in games using the multiplayer package.
 */
public interface GameServer<W extends World<W, C>, C, S> {

    void onStart(Supplier<Collection<Proxy<C>>> gameClients);

    void onUpdate();

    void onEnd();

    Proxy<?> makeReceiver(NodeAddress client, Class<? extends WorldInteractor> interactorClass, ProxyFactory factory,
                          Consumer<WorldInput<W, C>> inputProvider);

    void initializeSerialization(LocalNode network);

    ContinuumLaunchPacket<W, C> getStarterPacket();

    Class<S> getRemoteInterface();

}
