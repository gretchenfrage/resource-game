package com.phoenixkahlo.resourcegame.multiplayer;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputAdapter;
import com.phoenixkahlo.nodenet.LocalNode;
import com.phoenixkahlo.nodenet.proxy.Proxy;
import com.phoenixkahlo.resourcegame.core.GameState;
import com.phoenixkahlo.resourcegame.util.InputBufferer;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * Game state of a client connected to a server. It only needs to be passed the negotiated connection and the
 * specialized client. The network's serializer should already have been set up, in the same configuration as
 * done by the serializer configurator provided to the server.
 */
public class ClientState<W extends World<W, C>, C extends GameClient<W, C, S>, S> implements GameState, RemoteClient<W, C, S> {

    private final LocalNode network;
    private final Proxy<RemoteServer<W, C, S>> server;
    private final C gameClient;

    private long time;
    private WorldContinuum<W, C> continuum;
    private InputBufferer userInputBuffer;
    private WorldInteractor<W, C> interactor;

    private BlockingQueue<WorldInput<W, C>> worldInputBuffer = new LinkedBlockingQueue<>();

    public ClientState(LocalNode network, Proxy<RemoteServer<W, C, S>> server, C gameClient) {
        this.network = network;
        this.server = server;
        this.gameClient = gameClient;
    }

    @Override
    public void onEnter() {
        // set up user input buffer and getInteractor
        userInputBuffer = new InputBufferer();
        Gdx.input.setInputProcessor(userInputBuffer.processor());
        interactor = continuum.get().getInteractor(network.getAddress(), gameClient);
        // set up the continuum and time
        setupContinuumAndTime();
        // set up game client
        Proxy<S> gameServer = server.blocking().getGameServer();
        gameClient.onStart(gameServer);
        // join the world
        server.blocking().joinWorld(network.makeProxy(
                this,
                (Class<RemoteClient<W, C, S>>) (Object) RemoteClient.class)
        );
    }

    private void setupContinuumAndTime() {
        // set up the continuum
        continuum = new WorldContinuum<>();
        continuum.launch(server.blocking().getLaunchPacket());
        // synchronize time with server
        time = server.blocking().getTime();
    }

    /**
     * The update method should leave the continuum in the updated time state, so that the render method can render it.
     */
    @Override
    public void update() {
        // update time
        time++;
        // handle continuum
        try {
            // apply world inputs
            while (worldInputBuffer.size() > 0)
                continuum.applyInput(worldInputBuffer.remove());
            // update continuum
            continuum.advance(time);
            // prune continuum
            continuum.forget(1000);
        } catch (ForgottenHistoryException e) {
            // if the continuum is messed up, just set it up again
            e.printStackTrace();
            setupContinuumAndTime();
        }
        // handle interactors
        if (continuum.get().getInteractor(network.getAddress(), gameClient).equals(interactor)) {
            // if the interactors are unchanged, let it handle user input
            userInputBuffer.flush(interactor);
        } else {
            // if the interactors are changed, swap them and drain the user input buffer
            userInputBuffer.clear();

            interactor.onDeactivate();
            interactor = continuum.get().getInteractor(network.getAddress(), gameClient);
            Proxy<?> receiver = server.blocking().makeReceiver(network.getAddress(), interactor.getClass());
            interactor.onActivate(receiver, gameClient);
        }
        // update game logic
        gameClient.update();
    }

    @Override
    public void render() {
        interactor.render(continuum.get(), time);
    }

    /**
     * Called by the server when world inputs occur on the authoritative continuum.
     */
    @Override
    public void provideInput(WorldInput<W, C> input) {
        worldInputBuffer.add(input);
    }

    @Override
    public Proxy<C> getGameClient() {
        return network.makeProxy(gameClient, gameClient.getRemoteInterface());
    }

    @Override
    public int getTicksPerSecond() {
        return 60;
    }

    @Override
    public void onExit() {
        // close network
        network.disconnect();
        // unset input processor
        Gdx.input.setInputProcessor(new InputAdapter());
    }
}
