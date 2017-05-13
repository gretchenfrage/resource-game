package com.phoenixkahlo.resourcegame.coretest;

import com.phoenixkahlo.nodenet.serialization.Serializer;
import com.phoenixkahlo.resourcegame.core.Server;

import java.io.IOException;
import java.util.function.BiConsumer;
import java.util.function.Supplier;

/**
 * Created by Phoenix on 5/13/2017.
 */
public class CoreTestServer extends Server<CoreTestWorld> {

    private int port;

    public CoreTestServer(int port) throws IOException {
        super();
        this.port = port;
    }

    @Override
    protected void addSerializers(BiConsumer<Serializer, Integer> acceptor) {

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
        return () -> {
            CoreTestWorld world = new CoreTestWorld();
            // set up world here

            return world;
        };
    }

    @Override
    protected int getTicksPerSecond() {
        return 60;
    }

}
