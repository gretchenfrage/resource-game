package com.phoenixkahlo.resourcegame.core;


import com.phoenixkahlo.nodenet.BasicLocalNode;
import com.phoenixkahlo.nodenet.DisconnectionException;
import com.phoenixkahlo.nodenet.LocalNode;
import com.phoenixkahlo.nodenet.NodeAddress;
import com.phoenixkahlo.nodenet.proxy.Proxy;
import com.phoenixkahlo.nodenet.serialization.Serializer;
import com.phoenixkahlo.resourcegame.util.SerialCloner;

import java.io.IOException;
import java.util.*;
import java.util.function.*;

/**
 * A server that can be connected to by a ClientState.
 */
public abstract class Server<W extends World<W, C, S, RS>, C extends ClientState<W, C, S, RS>,
        S extends Server<W, C, S, RS>, RS extends RemoteServer<W, C, S, RS>>
        implements RemoteServer<W, C, S, RS>, Runnable  {

    private LocalNode network;
    private SerialCloner cloner;
    private final ServerWorldContinuum<W, C, S, RS> continuum;
    private volatile long ticks;
    private long lastTickTime = -1;

    public Server() throws IOException {
        cloner = new SerialCloner(network.getSerializer());

        ticks = getStartTime();
        continuum = new ServerWorldContinuum<>(ticks, getStartWorldSupplier());
    }

    protected abstract void collectSerializerFactories(Consumer<UnaryOperator<Serializer>> collector);

    protected abstract int getPort();

    protected abstract long getStartTime();

    protected abstract Supplier<W> getStartWorldSupplier();

    protected abstract int getTicksPerSecond();

    protected abstract Proxy<RS> createProxy(LocalNode network);

    protected abstract S getSelf();

    @Override
    public void run() {
        try {
            // create network
            network = new BasicLocalNode(getPort());
            // configure serialization
            List<UnaryOperator<Serializer>> serializerFactories = new ArrayList<>();
            collectSerializerFactories(serializerFactories::add);
            int header = 1;
            for (UnaryOperator<Serializer> serializerFactory : serializerFactories) {
                Serializer serializer = serializerFactory.apply(network.getSerializer());
                network.addSerializer(serializer, header++);
            }
            // configure network
            network.listenForJoin(node -> {
                // when a player joins, send them the server proxy
                try {
                    node.send(createProxy(network));
                } catch (DisconnectionException e) {
                    e.printStackTrace();
                }
            });
            network.listenForLeave(node -> leave(node.getAddress()));
            network.acceptAllIncoming();
        } catch (IOException e) {
            e.printStackTrace();
            return;
        }
        while (true) {
            if (lastTickTime == -1) {
                lastTickTime = System.nanoTime();
            } else {
                // update tick time
                ticks++;
                // update the state
                long time = System.nanoTime();
                float tickTimeDebt = (time - lastTickTime) / 1_000_000_000.0f;
                float timePerTick = 1.0f / getTicksPerSecond();
                synchronized (continuum) {
                    while (tickTimeDebt >= timePerTick) {
                        continuum.getWorld(ticks);
                    }
                }
                // prepare for next tick
                lastTickTime = time;
            }
        }
    }

    @Override
    public long getTime() {
        return ticks;
    }

    @Override
    public W downloadWorld(long time) {
        synchronized (continuum) {
            return cloner.clone(continuum.getWorld(time));
        }
    }

    @Override
    public void listenForExternalMutators(Proxy<Consumer<ExternalWorldMutator<W, C, S, RS>>> listener) {
        continuum.listenForExternalMutators(listener);
    }

    public void provideExternalWorldMutator(ExternalWorldMutator<W, C, S, RS> mutator) {
        continuum.provideExternalMutator(mutator);
    }

    @Override
    public Proxy<ClientControllerReceiver<W, C, S, RS>> createReceiver(NodeAddress client, long time) {
        ClientController<W, C, S, RS> controller;
        synchronized (continuum) {
            controller = continuum.getWorld(time).getController(client);
        }
        ClientControllerReceiver<W, C, S, RS> receiver = controller.toReceiver(getSelf());
        return network.makeProxy(
                receiver,
                (Class<ClientControllerReceiver<W, C, S, RS>>) (Object) receiver.getRemoteInterface()
        );
    }

    @Override
    public void join(NodeAddress address) {
        ExternalWorldMutator<W, C, S, RS> mutator;
        synchronized (continuum) {
            mutator = continuum.getWorld(continuum.getCurrentTime()).onEnter(address, getTime());
        }
        provideExternalWorldMutator(mutator);
    }

    @Override
    public void leave(NodeAddress address) {
        ExternalWorldMutator<W, C, S, RS> mutator;
        synchronized (continuum) {
            mutator = continuum.getWorld(continuum.getCurrentTime()).onLeave(address, getTime());
        }
        provideExternalWorldMutator(mutator);
    }

}
