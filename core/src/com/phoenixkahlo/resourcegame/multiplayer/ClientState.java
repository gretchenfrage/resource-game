package com.phoenixkahlo.resourcegame.multiplayer;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputAdapter;
import com.phoenixkahlo.nodenet.LocalNode;
import com.phoenixkahlo.nodenet.proxy.Proxy;
import com.phoenixkahlo.resourcegame.core.GameState;
import com.phoenixkahlo.resourcegame.util.ProxyFactory;

/**
 * Created by kahlo on 5/15/2017.
 */
public class ClientState<W extends World<W>, C, S> implements GameState {

    private C specificClient;
    private LocalNode network;
    private Proxy<RemoteServer<W, C, S>> server;

    private long time = -1;
    private ClientWorldContinuum<W, C, S> continuum;
    private InputBufferer inputBufferer;
    private WorldInteractor<W> interactor;

    public ClientState(C specificClient, LocalNode network, Proxy<RemoteServer<W, C, S>> server) {
        this.specificClient = specificClient;
        this.network = network;
        this.server = server;
    }

    @Override
    public void onEnter() {
        continuum = new ClientWorldContinuum<>(server);
        continuum.init();

        interactor = continuum.getWorld().getInteractor(network.getAddress());
        interactor.onActivate();

        Gdx.input.setInputProcessor(inputBufferer.processor());
    }

    @Override
    public void render() {
        continuum.makeTime(time);
        interactor.render(continuum.getWorld());
    }

    @Override
    public void update() {

    }

    @Override
    public int getTicksPerSecond() {
        return 60;
    }

    @Override
    public void onExit() {
        Gdx.input.setInputProcessor(new InputAdapter());
    }
}
