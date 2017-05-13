package com.phoenixkahlo.resourcegame.core;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.InputMultiplexer;
import com.phoenixkahlo.nodenet.LocalNode;
import com.phoenixkahlo.nodenet.proxy.Proxy;

/**
 * A GameState of a client that is connected to a server with a world.
 */
public class ClientState<G extends Game<G>, W extends World<W>> extends AbstractGameState<G> {

    private LocalNode network;
    private Proxy<RemoteServer<W>> server;

    private Object additionalClientData;

    private ClientWorldContinuum<W> continuum;

    private InputMultiplexer multiplexer;
    private ClientController<W> controller;
    private static final int CONTROLLER_INDEX = 0; // the ClientController's index in the InputMultiplexer

    private long time = -1;

    public ClientState(LocalNode network, Proxy<RemoteServer<W>> server, Object additionalClientData) {
        this.network = network;
        this.server = server;

        this.additionalClientData = additionalClientData;

        continuum = new ClientWorldContinuum<W>(server, network);

        multiplexer = new InputMultiplexer();
    }

    public ClientState(LocalNode network, Proxy<RemoteServer<W>> server) {
        this(network, server, null);
    }

    @Override
    public void onEnter(G game) {
        Gdx.input.setInputProcessor(multiplexer);
    }

    @Override
    public void update(G game) {
        // get the time if this is the first tick
        if (time == -1)
            time = server.blocking().getTime();
        // increment the time
        time++;
        // bring the world up to speed
        World world = continuum.getWorld(time);
        // exchange the client controller if needed
        if (!world.getController(network.getAddress()).equals(controller)) {
            multiplexer.removeProcessor(controller);
            controller = world.getController(network.getAddress());
            controller.bind(server);
            multiplexer.addProcessor(CONTROLLER_INDEX, controller);
        }
    }

    @Override
    public void render(G game) {
        continuum.getWorld(time).getView(network.getAddress()).render();
    }

    @Override
    public void onExit(G game) {
        Gdx.input.setInputProcessor(new InputAdapter());
    }

}
