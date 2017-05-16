package com.phoenixkahlo.resourcegame.hub.interactor;

import com.badlogic.gdx.InputAdapter;
import com.phoenixkahlo.nodenet.NodeAddress;
import com.phoenixkahlo.nodenet.proxy.Proxy;
import com.phoenixkahlo.resourcegame.hub.HubClient;
import com.phoenixkahlo.resourcegame.hub.HubWorld;
import com.phoenixkahlo.resourcegame.multiplayer.WorldInteractor;

/**
 * Created by Phoenix on 5/16/2017.
 */
public class AvatarInteractor implements WorldInteractor<HubWorld, HubClient> {

    private NodeAddress address;

    private transient Proxy<AvatarInteractorReceiver> receiver;
    private transient HubClient client;

    public AvatarInteractor(NodeAddress address) {
        this.address = address;
    }

    @Override
    public void onActivate(Proxy<?> receiver, HubClient gameClient) {
        this.receiver = receiver.cast(AvatarInteractorReceiver.class);
        this.client = gameClient;
    }

    @Override
    public void onDeactivate() {

    }

    @Override
    public void render(HubWorld world, HubClient gameClient) {

    }

}
