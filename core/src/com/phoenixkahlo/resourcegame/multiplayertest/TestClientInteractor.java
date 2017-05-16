package com.phoenixkahlo.resourcegame.multiplayertest;

import com.phoenixkahlo.nodenet.NodeAddress;
import com.phoenixkahlo.nodenet.proxy.Proxy;
import com.phoenixkahlo.resourcegame.multiplayer.WorldInteractor;

/**
 * Created by kahlo on 5/16/2017.
 */
public class TestClientInteractor implements WorldInteractor<TestWorld, TestClient> {

    private NodeAddress client;

    public TestClientInteractor(NodeAddress client) {
        this.client = client;
    }

    @Override
    public void onActivate(Proxy<?> receiver) {

    }

    @Override
    public void onDeactivate() {

    }

    @Override
    public void render(TestWorld world, TestClient specializedClient) {

    }

    @Override
    public boolean keyDown(int keycode) {
        return false;
    }

    @Override
    public boolean keyUp(int keycode) {
        return false;
    }

    @Override
    public boolean keyTyped(char character) {
        return false;
    }

    @Override
    public boolean touchDown(int screenX, int screenY, int pointer, int button) {
        return false;
    }

    @Override
    public boolean touchUp(int screenX, int screenY, int pointer, int button) {
        return false;
    }

    @Override
    public boolean touchDragged(int screenX, int screenY, int pointer) {
        return false;
    }

    @Override
    public boolean mouseMoved(int screenX, int screenY) {
        return false;
    }

    @Override
    public boolean scrolled(int amount) {
        return false;
    }
}
