package com.phoenixkahlo.resourcegame.core;

/**
 * Created by kahlo on 5/15/2017.
 */
public interface ServerLoop {

    void init() throws Exception;

    void update();

    int getTicksPerSecond();

}
