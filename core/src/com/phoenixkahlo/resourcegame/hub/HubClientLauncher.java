package com.phoenixkahlo.resourcegame.hub;

import com.badlogic.gdx.ApplicationListener;
import com.phoenixkahlo.nodenet.*;
import com.phoenixkahlo.nodenet.proxy.Proxy;
import com.phoenixkahlo.resourcegame.core.Game;
import com.phoenixkahlo.resourcegame.multiplayer.ClientState;
import com.phoenixkahlo.resourcegame.multiplayer.RemoteServer;

import java.net.InetSocketAddress;
import java.net.SocketException;

/**
 * Created by Phoenix on 5/16/2017.
 */
public class HubClientLauncher {

    public static ApplicationListener launch() throws SocketException, DisconnectionException, ProtocolViolationException {
        LocalNode network = new BasicLocalNode();
        Node node = network.connect(new InetSocketAddress("localhost", 25565)).get();
        Proxy<RemoteServer<HubWorld, HubClient, HubServer>> server =
                (Proxy<RemoteServer<HubWorld, HubClient, HubServer>>) (Object) node.receiveProxy(RemoteServer.class);
        ClientState<HubWorld, HubClient, HubServer> state = new ClientState<>(network, server, new LocalHubClient());
        return new Game(state);
    }

}
