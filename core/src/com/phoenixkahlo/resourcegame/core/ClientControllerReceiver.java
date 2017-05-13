package com.phoenixkahlo.resourcegame.core;

/**
 * Created by Phoenix on 5/13/2017.
 */
public interface ClientControllerReceiver<W extends World<W>> {

    void bind(Server<W> server);

}
