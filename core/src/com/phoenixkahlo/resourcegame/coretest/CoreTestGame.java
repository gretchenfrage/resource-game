package com.phoenixkahlo.resourcegame.coretest;

import com.phoenixkahlo.resourcegame.core.ClientState;
import com.phoenixkahlo.resourcegame.core.Game;
import com.phoenixkahlo.resourcegame.core.GameState;

/**
 * Created by Phoenix on 5/13/2017.
 */
public class CoreTestGame extends Game<CoreTestGame> {

    @Override
    protected CoreTestGame getGame() {
        return this;
    }

    @Override
    protected GameState<CoreTestGame> getInitialState() {

    }

}
