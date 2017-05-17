package com.phoenixkahlo.resourcegame.hub.entity;

import com.phoenixkahlo.resourcegame.hub.RenderStage;
import com.phoenixkahlo.resourcegame.hub.UpdateStage;

import java.util.UUID;

/**
 * Created by Phoenix on 5/16/2017.
 */
public abstract class AbstractEntity implements Entity {

    private UUID id;

    public AbstractEntity() {
        id = UUID.randomUUID();
    }

    @Override
    public UUID getID() {
        return id;
    }

    @Override
    public UpdateStage getUpdateStage() {
        return UpdateStage.MAIN;
    }

    @Override
    public RenderStage getRenderStage() {
        return RenderStage.MAIN;
    }
}
