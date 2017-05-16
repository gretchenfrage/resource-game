package com.phoenixkahlo.resourcegame.hub;

import com.phoenixkahlo.nodenet.proxy.Proxy;

/**
 * Created by Phoenix on 5/16/2017.
 */
public class LocalHubClient implements HubClient {

    @Override
    public void onStart(Proxy<HubServer> gameServer) {

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
