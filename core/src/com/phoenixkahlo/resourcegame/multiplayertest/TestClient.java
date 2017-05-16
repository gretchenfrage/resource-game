package com.phoenixkahlo.resourcegame.multiplayertest;

import com.badlogic.gdx.ApplicationAdapter;
import com.phoenixkahlo.nodenet.*;
import com.phoenixkahlo.nodenet.proxy.Proxy;
import com.phoenixkahlo.resourcegame.core.Game;
import com.phoenixkahlo.resourcegame.multiplayer.ClientState;
import com.phoenixkahlo.resourcegame.multiplayer.RemoteClient;
import com.phoenixkahlo.resourcegame.multiplayer.RemoteServer;

import java.net.InetSocketAddress;
import java.net.SocketException;

/**
 * Created by kahlo on 5/16/2017.
 */
public class TestClient {

    public static ApplicationAdapter launch() throws Exception {
        // connect to server
        InetSocketAddress address = new InetSocketAddress("localhost", 25565);
        LocalNode network = new BasicLocalNode();
        Node serverNode = network.connect(address).get();
        Proxy<RemoteServer<TestWorld, TestClient, TestServer>> serverProxy = serverNode.receiveProxy(
                (Class<RemoteServer<TestWorld, TestClient, TestServer>>) (Object) RemoteClient.class
        );
        // launch state game
        ClientState<TestWorld, TestClient, TestServer> state = new ClientState<>(network, serverProxy, new TestClient());
        return new Game(state);
    }

}
