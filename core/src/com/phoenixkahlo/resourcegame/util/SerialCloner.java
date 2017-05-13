package com.phoenixkahlo.resourcegame.util;

import com.phoenixkahlo.nodenet.ProtocolViolationException;
import com.phoenixkahlo.nodenet.serialization.Deserializer;
import com.phoenixkahlo.nodenet.serialization.Serializer;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * Created by Phoenix on 5/13/2017.
 */
public class SerialCloner {

    private Serializer serializer;
    private Deserializer deserializer;

    public SerialCloner(Serializer serializer) {
        this.serializer = serializer;
        this.deserializer = serializer.toDeserializer();
    }

    public <E> E clone(E object) {
        try {
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            serializer.serialize(object, baos);
            InputStream in = new ByteArrayInputStream(baos.toByteArray());
            Object deserialized = deserializer.deserialize(in);
            return (E) deserialized;
        } catch (IOException | ProtocolViolationException e) {
            throw new RuntimeException(e);
        }
    }

}
