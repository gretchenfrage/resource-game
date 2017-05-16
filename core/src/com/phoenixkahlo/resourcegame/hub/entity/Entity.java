package com.phoenixkahlo.resourcegame.hub.entity;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.phoenixkahlo.resourcegame.hub.HubWorld;
import com.phoenixkahlo.resourcegame.hub.RenderStage;
import com.phoenixkahlo.resourcegame.hub.UpdateStage;
import com.phoenixkahlo.resourcegame.multiplayer.ReversibleMutator;

import java.util.UUID;
import java.util.stream.Stream;

/**
 * Created by Phoenix on 5/16/2017.
 */
public interface Entity {

    UUID getID();

    Stream<ReversibleMutator<HubWorld>> update();

    Stream<Sprite> getSprites();

    UpdateStage getUpdateStage();

    RenderStage getRenderStage();

}
