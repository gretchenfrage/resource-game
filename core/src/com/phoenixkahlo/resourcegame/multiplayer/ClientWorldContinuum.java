package com.phoenixkahlo.resourcegame.multiplayer;

import com.phoenixkahlo.nodenet.proxy.Proxy;

import java.util.Collection;

/**
 * Created by kahlo on 5/15/2017.
 */
public class ClientWorldContinuum<W extends World<W>, C, S> extends WorldContinuumOld<W> {

    private Proxy<RemoteServer<W, C, S>> server;

    public ClientWorldContinuum(Proxy<RemoteServer<W, C, S>> server) {
        super(1000);
        this.server = server;
    }

    @Override
    protected W getStartWorld() {

    }

    @Override
    protected long getStartTime() {
        return 0;
    }

    @Override
    protected Collection<WorldInput<W>> getStartInputs() {
        return null;
    }

    @Override
    protected long getStartTargetTime() {
        return 0;
    }

    @Override
    protected Collection<WorldInput<W>> collectInputs() {
        return null;
    }

    @Override
    protected W downloadWorld(long targetTime) throws ForgottenHistoryException {
        return null;
    }

    @Override
    protected Collection<WorldInput<W>> downloadInputs() {
        return null;
    }
}
