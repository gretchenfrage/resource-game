package com.phoenixkahlo.resourcegame.multiplayer;

import com.phoenixkahlo.nodenet.BasicLocalNode;
import com.phoenixkahlo.nodenet.DisconnectionException;
import com.phoenixkahlo.nodenet.LocalNode;
import com.phoenixkahlo.nodenet.NodeAddress;
import com.phoenixkahlo.nodenet.proxy.Proxy;
import com.phoenixkahlo.resourcegame.core.ServerLoop;
import com.phoenixkahlo.resourcegame.util.SerialCloner;

import java.net.SocketException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.stream.Collectors;

/**
 * Server loop that connects to client states. When a node joins the network, this will send them the server proxy.
 * For the client to join the world, it has to call RemoteServer.joinWorld and pass a proxy of itself.
 */
public class Server<W extends World<W, C>, C, S extends GameServer<W, C, S>> implements ServerLoop, RemoteServer<W, C, S> {

    private final int port;

    private S gameServer;
    private LocalNode network;
    private WorldContinuum<W, C> continuum; // synchronize usages
    private volatile long time;
    private BlockingQueue<WorldInput<W, C>> worldInputBuffer = new LinkedBlockingQueue<>();
    private Collection<Proxy<RemoteClient<W, C, S>>> clients = Collections.synchronizedCollection(new ArrayList<>());
    private SerialCloner cloner;

    public Server(int port, S gameServer) {
        this.port = port;
        this.gameServer = gameServer;
    }

    @Override
    public void init() throws SocketException {
        // set up the network
        network = new BasicLocalNode(port);
        // set up serializers
        gameServer.initializeSerialization(network);
        // set up serial cloner
        cloner = new SerialCloner(network.getSerializer());
        // when a node joins, send them the server proxy
        network.listenForJoin(node -> {
            try {
                node.send(network.makeProxy(this, RemoteServer.class));
            } catch (DisconnectionException e) {
                e.printStackTrace();
            }
        });
        // when a node leaves, apply a leave event, and remove from the clients collection
        network.listenForLeave(node -> {
            clients.removeIf(proxy -> proxy.getSource().equals(node.getAddress()));
            synchronized (continuum) {
                provideInput(continuum.get().handleLeave(node.getAddress()));
            }
        });
        // set up the continuum and time
        continuum = new WorldContinuum<W, C>();
        ContinuumLaunchPacket<W, C> launchPacket = gameServer.getStarterPacket();
        continuum.launch(launchPacket);
        time = launchPacket.getAdvanceToTime();
        // set up the game server
        gameServer.onStart(() -> {
            synchronized (clients) {
                return clients.stream()
                        .map(proxy -> proxy.blocking().getGameClient())
                        .collect(Collectors.toList());
            }
        });
        // open the network
        network.acceptAllIncoming();
    }

    @Override
    public void update() {
        // update time
        time++;
        synchronized (continuum) {
            // apply world inputs
            while (worldInputBuffer.size() > 0) {
                WorldInput<W, C> input = worldInputBuffer.remove();
                try {
                    // apply the input
                    continuum.applyInput(input);
                    // send to clients
                    clients.forEach(client -> client.unblocking(false).provideInput(input));
                } catch (ForgottenHistoryException e) {
                    // log the exception and resume
                    System.err.println("failed to apply input: " + input);
                    e.printStackTrace();
                }
            }
            // update continuum
            continuum.advance(time);
            // prune continuum
            continuum.forget(1000);
        }
    }

    @Override
    public int getTicksPerSecond() {
        return 60;
    }

    @Override
    public void joinWorld(Proxy<RemoteClient<W, C, S>> client) {
        clients.add(client);
        WorldInput<W, C> handler;
        synchronized (continuum) {
            handler = continuum.get().handleEnter(client.getSource());
        }
        provideInput(handler);
    }

    @Override
    public ContinuumLaunchPacket<W, C> getLaunchPacket() {
        synchronized (continuum) {
            return new ContinuumLaunchPacket<>(
                    cloner.clone(continuum.get()),
                    continuum.getKnownInputs(),
                    continuum.getEarliestRememberedTime(),
                    time
            );
        }
    }

    @Override
    public long getTime() {
        return time;
    }

    @Override
    public Proxy<?> makeReceiver(NodeAddress address, Class<? extends WorldInteractor> interactorClass) {
        return gameServer.makeReceiver(address, interactorClass, network::makeProxy);
    }

    @Override
    public Proxy<S> getGameServer() {
        return network.makeProxy(gameServer, gameServer.getRemoteInterface());
    }

    public void provideInput(WorldInput<W, C> input) {
        // simply add it to the world input queue, the main loop can be used to send it to the clients
        worldInputBuffer.add(input);
    }

}
