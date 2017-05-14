package com.phoenixkahlo.resourcegame.core;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.InputMultiplexer;
import com.phoenixkahlo.nodenet.LocalNode;
import com.phoenixkahlo.nodenet.proxy.Proxy;

/**
 * A GameState of a client that is connected to a server with a world.
 */
public abstract class ClientState<W extends World<W, C, S, RS>, C extends ClientState<W, C, S, RS>, S extends Server<W, C, S, RS>,
        RS extends RemoteServer<W, C, S, RS>> extends AbstractGameState {

    private LocalNode network;
    private Proxy<RS> server;

    private ClientWorldContinuum<W, C, S, RS> continuum;

    private InputMultiplexer multiplexer;
    private ClientController<W, C, S, RS> controller;
    private static final int CONTROLLER_INDEX = 0; // the ClientController's index in the InputMultiplexer

    private long time = -1;

    public ClientState(LocalNode network, Proxy<RS> server,
                       Object additionalClientData) {
        this.network = network;
        this.server = server;

        continuum = new ClientWorldContinuum<W, C, S, RS>(server, network);

        multiplexer = new InputMultiplexer();
    }

    public ClientState(LocalNode network, Proxy<RS> server) {
        this(network, server, null);
    }

    protected abstract C getSelf();

    @Override
    public void onEnter() {
        Gdx.input.setInputProcessor(multiplexer);
    }

    @Override
    public void update() {
        // get the time if this is the first tick
        if (time == -1)
            time = server.blocking().getTime();
        // increment the time
        time++;
        // bring the world up to speed
        World<W, C, S, RS> world = continuum.getWorld(time);
        // exchange the client controller if needed
        if (!world.getController(network.getAddress()).equals(controller)) {
            multiplexer.removeProcessor(controller);
            controller = world.getController(network.getAddress());
            controller.bind(server, getSelf());
            multiplexer.addProcessor(CONTROLLER_INDEX, controller);
        }
    }

    @Override
    public void render() {
        continuum.getWorld(time).getView(network.getAddress()).render(this);
    }

    @Override
    public void onExit() {
        Gdx.input.setInputProcessor(new InputAdapter());
    }

}
