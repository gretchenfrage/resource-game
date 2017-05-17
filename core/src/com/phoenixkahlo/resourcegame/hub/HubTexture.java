package com.phoenixkahlo.resourcegame.hub;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;

import java.util.function.Supplier;

/**
 * Created by Phoenix on 5/17/2017.
 */
public enum HubTexture implements Supplier<FileHandle> {

    WHEAT("wheat.png"),
    ;

    private FileHandle handle;

    HubTexture(String path) {
        handle = Gdx.files.internal(path);
    }

    @Override
    public FileHandle get() {
        return handle;
    }

}
