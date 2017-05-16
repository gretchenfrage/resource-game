package com.phoenixkahlo.resourcegame.coreold;

/**
 * Created by Phoenix on 5/13/2017.
 */
public interface ClientControllerReceiver<W extends World<W, C, S, RS>, C extends ClientState<W, C, S, RS>,
        S extends Server<W, C, S, RS>, RS extends RemoteServer<W, C, S, RS>> {

    Class<? extends ClientControllerReceiver<W, C, S, RS>> getRemoteInterface();

}
