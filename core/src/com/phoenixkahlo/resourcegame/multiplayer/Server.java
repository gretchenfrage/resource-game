package com.phoenixkahlo.resourcegame.multiplayer;

import com.phoenixkahlo.nodenet.BasicLocalNode;
import com.phoenixkahlo.nodenet.DisconnectionException;
import com.phoenixkahlo.nodenet.LocalNode;
import com.phoenixkahlo.nodenet.NodeAddress;
import com.phoenixkahlo.nodenet.proxy.Proxy;
import com.phoenixkahlo.nodenet.serialization.Serializer;
import com.phoenixkahlo.resourcegame.core.ServerLoop;
import com.phoenixkahlo.resourcegame.util.SerialCloner;

import java.net.SocketException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.function.Consumer;
import java.util.function.UnaryOperator;

/**
 * Server loop that connects to client states. When a node joins the network, this will send them the server proxy.
 * For the client to join the world, it has to call RemoteServer.joinWorld and pass a proxy of itself.
 */
public class Server<W extends World<W, C>, C, S> implements ServerLoop, RemoteServer<W, C, S> {

    private ContinuumLaunchPacket launchPacket;
    private Consumer<LocalNode> serializerConfigurator;
    private int port;

    private S specializedServer;
    private LocalNode network;
    private WorldContinuum<W, C> continuum; // synchronize usages
    private volatile long time;
    private BlockingQueue<WorldInput<W, C>> worldInputBuffer = new LinkedBlockingQueue<>();
    private Collection<Proxy<RemoteClient<W, C, S>>> clients = Collections.synchronizedCollection(new ArrayList<>());
    private SerialCloner cloner;

    public Server(ContinuumLaunchPacket launchPacket, Consumer<LocalNode> serializerConfigurator, int port,
                  S specializedServer) {
        this.launchPacket = launchPacket;
        this.serializerConfigurator = serializerConfigurator;
        this.port = port;
        this.specializedServer = specializedServer;
    }

    @Override
    public void init() throws SocketException {
        // set up the network
        network = new BasicLocalNode(port);
        // set up serializers
        serializerConfigurator.accept(network);
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
        // when a node leaves, apply a leave event
        network.listenForLeave(node -> {
            clients.removeIf(proxy -> proxy.getSource().equals(node.getAddress()));
            synchronized (continuum) {
                provideInput(continuum.get().handleLeave(node.getAddress()));
            }
        });
        // open the network
        network.acceptAllIncoming();
    }

    @Override
    public void update() {

    }

    @Override
    public int getTicksPerSecond() {
        return 60;
    }

    @Override
    public void joinWorld(Proxy<RemoteClient<W, C, S>> client) {
        clients.add(client);
        synchronized (continuum) {
            provideInput(continuum.get().handleEnter(client.getSource()));
        }
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
        return null;
    }

    public void provideInput(WorldInput<W, C> input) {
        // simply add it to the world input queue, the main loop can be used to send it to the clients
        worldInputBuffer.add(input);
    }

}
