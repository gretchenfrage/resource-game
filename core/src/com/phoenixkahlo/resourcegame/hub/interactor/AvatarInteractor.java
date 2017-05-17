package com.phoenixkahlo.resourcegame.hub.interactor;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.phoenixkahlo.nodenet.NodeAddress;
import com.phoenixkahlo.nodenet.proxy.Proxy;
import com.phoenixkahlo.resourcegame.hub.HubClient;
import com.phoenixkahlo.resourcegame.hub.HubWorld;
import com.phoenixkahlo.resourcegame.hub.LocalHubClient;
import com.phoenixkahlo.resourcegame.hub.entity.Avatar;
import com.phoenixkahlo.resourcegame.multiplayer.WorldInteractor;

/**
 * Created by Phoenix on 5/16/2017.
 */
public class AvatarInteractor implements WorldInteractor<HubWorld, HubClient> {

    private NodeAddress address;

    private transient Proxy<AvatarInteractorReceiver> receiver;
    private transient LocalHubClient client;

    public AvatarInteractor(NodeAddress address) {
        this.address = address;
    }

    @Override
    public void onActivate(Proxy<?> receiver, HubClient gameClient) {
        this.receiver = receiver.cast(AvatarInteractorReceiver.class);
        this.client = (LocalHubClient) gameClient;
    }

    @Override
    public void render(HubWorld world, HubClient gameClient) {
        // retrieve entity
        Avatar avatar = world.getAvatar(address);
        // set up camera
        OrthographicCamera camera = client.getCamera();
        camera.setToOrtho(false,
                Gdx.graphics.getWidth() / 100f,
                Gdx.graphics.getHeight() / 100f);
        camera.position.x =
    }

    @Override
    public void onDeactivate() {

    }

}
