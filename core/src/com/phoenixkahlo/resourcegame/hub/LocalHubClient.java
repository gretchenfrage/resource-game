package com.phoenixkahlo.resourcegame.hub;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.phoenixkahlo.nodenet.proxy.Proxy;
import com.phoenixkahlo.resourcegame.core.EnumTextureCollection;

/**
 * Created by Phoenix on 5/16/2017.
 */
public class LocalHubClient implements HubClient {

    private OrthographicCamera camera;
    private SpriteBatch batch;
    private EnumTextureCollection<HubTexture> textures;

    @Override
    public void onStart(Proxy<HubServer> gameServer) {
        System.out.println("starting hub client");

        camera = new OrthographicCamera();
        batch = new SpriteBatch();
        textures = new EnumTextureCollection<>(HubTexture.class);
        textures.load();
    }

    public OrthographicCamera getCamera() {
        return camera;
    }

    public SpriteBatch getBatch() {
        return batch;
    }

    public EnumTextureCollection<HubTexture> getTextures() {
        return textures;
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
