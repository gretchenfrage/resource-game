package com.phoenixkahlo.resourcegame.core;

import com.badlogic.gdx.InputProcessor;
import com.phoenixkahlo.nodenet.proxy.Proxy;

/**
 * An extension of the input processor, one of which is assigned to each client in a world, and which collects client
 * input and then sends data to the server.
 */
public interface ClientController<W extends World<W, C, S, RS>, C extends ClientState<W, C, S, RS>,
        S extends Server<W, C, S, RS>, RS extends RemoteServer<W, C, S, RS>> extends InputProcessor {

    /**
     * Any controller that is going to be registered as an InputProcessor will first have this method called.
     */
    void bind(Proxy<RS> server, C client);

    /**
     * Produce a receiver object for this controller. This method will only be called on the server side, and thus
     * should not rely on having the server proxy set.
     */
    ClientControllerReceiver<W, C, S, RS> toReceiver(S server);

    /**
     * Return whether this controller is equivalent to the other controller, but don't consider whether
     * the server proxy has been set.
     */
    boolean equals(Object other);

}
