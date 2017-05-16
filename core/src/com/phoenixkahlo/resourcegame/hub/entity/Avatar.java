package com.phoenixkahlo.resourcegame.hub.entity;

import com.phoenixkahlo.resourcegame.hub.HubClient;
import com.phoenixkahlo.resourcegame.hub.HubWorld;
import com.phoenixkahlo.resourcegame.multiplayer.WorldInteractor;

/**
 * Created by Phoenix on 5/16/2017.
 */
public interface Avatar extends Entity {

    WorldInteractor<HubWorld, HubClient> getInteractor();

}
