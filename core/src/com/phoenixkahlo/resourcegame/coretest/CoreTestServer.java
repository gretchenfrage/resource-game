package com.phoenixkahlo.resourcegame.coretest;

import com.phoenixkahlo.nodenet.LocalNode;
import com.phoenixkahlo.nodenet.proxy.Proxy;
import com.phoenixkahlo.nodenet.serialization.Serializer;
import com.phoenixkahlo.resourcegame.coreold.Server;

import java.io.IOException;
import java.util.Scanner;
import java.util.function.Consumer;
import java.util.function.Supplier;
import java.util.function.UnaryOperator;

/**
 * Created by Phoenix on 5/14/2017.
 */
public class CoreTestServer extends Server<CoreTestWorld, CoreTestClientState, CoreTestServer, CoreTestRemoteServer>
        implements CoreTestRemoteServer {

    private int port;

    public CoreTestServer() throws IOException {
        super();
        try (Scanner stevie = new Scanner(System.in)) {
            System.out.print("what port to host server on:\n> ");
            port = stevie.nextInt();
        }
    }

    @Override
    protected void collectSerializerFactories(Consumer<UnaryOperator<Serializer>> collector) {
        collector.accept(CoreTestWorld::serializer);
    }

    @Override
    protected int getPort() {
        return port;
    }

    @Override
    protected long getStartTime() {
        return 0;
    }

    @Override
    protected Supplier<CoreTestWorld> getStartWorldSupplier() {
        return CoreTestWorld::new;
    }

    @Override
    protected int getTicksPerSecond() {
        return 60;
    }

    @Override
    protected Proxy<CoreTestRemoteServer> createProxy(LocalNode network) {
        return network.makeProxy(this, CoreTestRemoteServer.class);
    }

    @Override
    protected CoreTestServer getSelf() {
        return this;
    }
}
