package com.phoenixkahlo.resourcegame.coretest;

import com.phoenixkahlo.nodenet.BasicLocalNode;
import com.phoenixkahlo.nodenet.DisconnectionException;
import com.phoenixkahlo.nodenet.LocalNode;
import com.phoenixkahlo.nodenet.ProtocolViolationException;
import com.phoenixkahlo.nodenet.proxy.Proxy;
import com.phoenixkahlo.resourcegame.core.Game;
import com.phoenixkahlo.resourcegame.core.GameState;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.util.Scanner;

/**
 * Created by Phoenix on 5/14/2017.
 */
public class CoreTestGame extends Game {

    @Override
    protected GameState getInitialState() {
        InetSocketAddress address;
        try (Scanner scanner = new Scanner(System.in)) {
            System.out.print("address to connect to:\n> ");
            String ip = scanner.next();
            System.out.print("port to connect to:\n> ");
            int port = scanner.nextInt();
            address = new InetSocketAddress(ip, port);
        }
        try {
            LocalNode network = new BasicLocalNode();
            Proxy<CoreTestRemoteServer> server = network.connect(address).get().receiveProxy(CoreTestRemoteServer.class);
            return new CoreTestClientState(network, server);
        } catch (IOException | DisconnectionException | ProtocolViolationException e) {
            e.printStackTrace();
            System.exit(1);
            throw new Error("unreachable code");
        }
    }

}
