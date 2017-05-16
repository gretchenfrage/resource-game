package com.phoenixkahlo.resourcegame.multiplayer;

import com.badlogic.gdx.InputProcessor;
import com.phoenixkahlo.nodenet.proxy.Proxy;
import com.phoenixkahlo.resourcegame.util.ProxyFactory;

/**
 * An object that, when activated on a client's machine, handles the
 * input and rendering of that client. This interface extends InputProcessor,
 * but the client guarentees that the methods inherited from InputProcessor
 * will only be called while the getInteractor is active. If the getInteractor
 * wishes to do input querying, it can do so in the render method.
 */
public interface WorldInteractor<W extends World<W, C>, C> extends InputProcessor {

    void onActivate(Proxy<?> receiver, C gameClient);

    void onDeactivate();

    void render(W world, C gameClient);

    /**
     * After the client updates the world, it will check for
     * equality between the currently active getInteractor
     * and the getInteractor produced by the updated world. If
     * the interactors are unequal, it will deactivate the
     * existing getInteractor and activate the new one.
     */
    @Override
    boolean equals(Object other);

    @Override
    default boolean keyDown(int keycode) {
        return false;
    }

    @Override
    default boolean keyUp(int keycode) {
        return false;
    }

    @Override
    default boolean keyTyped(char character) {
        return false;
    }

    @Override
    default boolean touchDown(int screenX, int screenY, int pointer, int button) {
        return false;
    }

    @Override
    default boolean touchUp(int screenX, int screenY, int pointer, int button) {
        return false;
    }

    @Override
    default boolean touchDragged(int screenX, int screenY, int pointer) {
        return false;
    }

    @Override
    default boolean mouseMoved(int screenX, int screenY) {
        return false;
    }

    @Override
    default boolean scrolled(int amount) {
        return false;
    }

}
