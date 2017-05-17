package com.phoenixkahlo.resourcegame.core;

import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Texture;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Supplier;

/**
 * Given an enum that implements Supplier<FileHandle>, this object will load a texture from the file handle of each
 * enum constant, and make them available.
 */
public class EnumTextureCollection<E extends Enum<E> & Supplier<FileHandle>> {

    private Class<E> clazz;
    private Map<E, Texture> textures;

    public EnumTextureCollection(Class<E> clazz) {
        this.clazz = clazz;
    }

    public void load() {
        textures = new HashMap<>();
        for (E key : clazz.getEnumConstants())
            textures.put(key, new Texture(key.get()));
    }

    public Texture get(E key) {
        return textures.get(key);
    }

}
