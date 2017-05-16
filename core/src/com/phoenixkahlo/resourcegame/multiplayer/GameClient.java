package com.phoenixkahlo.resourcegame.multiplayer;

import com.phoenixkahlo.nodenet.proxy.Proxy;

/**
 * Object for game specific logic in games using the multiplayer package.
 */
public interface GameClient<W extends World<W, C>, C, S> {

    void onStart(Proxy<S> gameServer);

    void update();

    void onEnd();

    Class<C> getRemoteInterface();

}
