package com.phoenixkahlo.resourcegame.multiplayer;

import com.badlogic.gdx.InputProcessor;
import com.phoenixkahlo.nodenet.proxy.Proxy;
import com.phoenixkahlo.resourcegame.util.ProxyFactory;

/**
 * An object that, when activated on a client's machine, handles the
 * input and rendering of that client. This interface extends InputProcessor,
 * but the client guarentees that the methods inherited from InputProcessor
 * will only be called while the interactor is active. If the interactor
 * wishes to do input querying, it can do so in the render method.
 */
public interface WorldInteractor<W extends World<W, C>, C> extends InputProcessor {

    void onActivate(Proxy<?> receiver, C gameClient);

    void onDeactivate();

    void render(W world, C gameClient);

    /**
     * After the client updates the world, it will check for
     * equality between the currently active interactor
     * and the interactor produced by the updated world. If
     * the interactors are unequal, it will deactivate the
     * existing interactor and activate the new one.
     */
    @Override
    boolean equals(Object other);

}
