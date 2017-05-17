package com.phoenixkahlo.resourcegame.hub;

import com.phoenixkahlo.resourcegame.core.ServerLoop;
import com.phoenixkahlo.resourcegame.core.ServerLoopRunner;
import com.phoenixkahlo.resourcegame.multiplayer.Server;

/**
 * Created by Phoenix on 5/16/2017.
 */
public class HubServerLauncher {

    public static void launch() {
        Server<HubWorld, HubClient, HubServer> loop = new Server<>(25565, new LocalHubServer());
        new ServerLoopRunner(loop).run();
    }

}
