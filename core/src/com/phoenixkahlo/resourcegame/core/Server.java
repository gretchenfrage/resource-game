package com.phoenixkahlo.resourcegame.core;


import com.phoenixkahlo.nodenet.BasicLocalNode;
import com.phoenixkahlo.nodenet.DisconnectionException;
import com.phoenixkahlo.nodenet.LocalNode;
import com.phoenixkahlo.nodenet.NodeAddress;
import com.phoenixkahlo.nodenet.proxy.Proxy;
import com.phoenixkahlo.nodenet.serialization.Serializer;
import com.phoenixkahlo.resourcegame.util.SerialCloner;

import java.io.IOException;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.function.Supplier;

/**
 * A server that can be connected to by a ClientState.
 */
public abstract class Server<W extends World<W>> implements RemoteServer<W>, Runnable  {

    private LocalNode network;

    private SerialCloner cloner;

    private ServerWorldContinuum<W> continuum;

    private long ticks;
    private long lastTickTime = -1;
    private float tickTimeDebt = 0;

    public Server() throws IOException {
        network = new BasicLocalNode(getPort());
        addSerializers(network::addSerializer);
        network.listenForJoin(node -> {
            try {
                node.send(network.makeProxy(this, RemoteServer.class));
            } catch (DisconnectionException e) {
                e.printStackTrace();
            }
        });
        network.acceptAllIncoming();

        cloner = new SerialCloner(network.getSerializer());

        ticks = getStartTime();
        continuum = new ServerWorldContinuum<W>(ticks, getStartWorldSupplier());
    }

    protected abstract void addSerializers(BiConsumer<Serializer, Integer> acceptor);

    protected abstract int getPort();

    protected abstract long getStartTime();

    protected abstract Supplier<W> getStartWorldSupplier();

    protected abstract int getTicksPerSecond();

    @Override
    public void run() {
        if (lastTickTime == -1) {
            lastTickTime = System.nanoTime();
        } else {
            // update tick time
            ticks++;
            // update the state
            long time = System.nanoTime();
            tickTimeDebt = (time - lastTickTime) / 1_000_000_000.0f;
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
    public void listenForExternalMutators(Proxy<Consumer<ExternalWorldMutator<W>>> listener) {
        continuum.listenForExternalMutators(listener);
    }

    public void provideExternalWorldMutator(ExternalWorldMutator<W> mutator) {
        continuum.provideExternalMutator(mutator);
    }

    @Override
    public Proxy<ClientControllerReceiver<W>> createReceiver(NodeAddress client, long time) {
        ClientController<W> controller;
        synchronized (continuum) {
            controller = continuum.getWorld(time).getController(client);
        }
        ClientControllerReceiver<W> receiver = controller.produceReceiver();
        receiver.bind(this);
        return network.makeProxy(
                receiver,
                (Class<ClientControllerReceiver<W>>) (Object) ClientControllerReceiver.class
        );
    }

}
