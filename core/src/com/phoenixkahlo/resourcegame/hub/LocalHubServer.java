package com.phoenixkahlo.resourcegame.hub;

import com.phoenixkahlo.nodenet.LocalNode;
import com.phoenixkahlo.nodenet.NodeAddress;
import com.phoenixkahlo.nodenet.proxy.Proxy;
import com.phoenixkahlo.resourcegame.hub.interactor.AvatarInteractor;
import com.phoenixkahlo.resourcegame.hub.interactor.AvatarInteractorReceiver;
import com.phoenixkahlo.resourcegame.hub.interactor.LocalAvatarInteractorReceiver;
import com.phoenixkahlo.resourcegame.multiplayer.ContinuumLaunchPacket;
import com.phoenixkahlo.resourcegame.multiplayer.WorldInteractor;
import com.phoenixkahlo.resourcegame.util.ProxyFactory;

import java.util.Collection;
import java.util.function.Supplier;

/**
 * Created by Phoenix on 5/16/2017.
 */
public class LocalHubServer implements HubServer {

    @Override
    public void onStart(Supplier<Collection<Proxy<HubClient>>> gameClients) {

    }

    @Override
    public void onUpdate() {

    }

    @Override
    public void onEnd() {

    }

    @Override
    public Proxy<?> makeReceiver(NodeAddress client, Class<? extends WorldInteractor> interactorClass, ProxyFactory factory) {
        if (interactorClass.equals(AvatarInteractor.class))
            return factory.apply(new LocalAvatarInteractorReceiver(client), AvatarInteractorReceiver.class);
    }

    @Override
    public void initializeSerialization(LocalNode network) {
        //TODO: implement
    }

    @Override
    public ContinuumLaunchPacket<HubWorld, HubClient> getStarterPacket() {
        return null;
    }

    @Override
    public Class<HubServer> getRemoteInterface() {
        return HubServer.class;
    }

}
