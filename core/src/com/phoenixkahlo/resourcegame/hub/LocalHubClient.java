package com.phoenixkahlo.resourcegame.hub;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.phoenixkahlo.nodenet.proxy.Proxy;

/**
 * Created by Phoenix on 5/16/2017.
 */
public class LocalHubClient implements HubClient {

    private OrthographicCamera camera;
    private SpriteBatch batch;

    @Override
    public void onStart(Proxy<HubServer> gameServer) {
        camera = new OrthographicCamera();
        batch = new SpriteBatch();
    }

    public OrthographicCamera getCamera() {
        return camera;
    }

    public SpriteBatch getBatch() {
        return batch;
    }

    @Override
    public void update() {

    }

    @Override
    public void onEnd() {

    }

    @Override
    public Class<HubClient> getRemoteInterface() {
        return HubClient.class;
    }

}
