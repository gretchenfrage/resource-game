package com.phoenixkahlo.resourcegame.core;

/**
 * Created by kahlo on 5/15/2017.
 */
public class ServerLoopRunner implements Runnable {

    private ServerLoop loop;
    private long lastRenderTime = -1;
    private float tickTimeDebt = 0;

    public ServerLoopRunner(ServerLoop loop) {
        this.loop = loop;
    }

    @Override
    public void run() {
        while (true) {
            if (lastRenderTime == -1) {
                lastRenderTime = System.nanoTime();
            } else {
                // update
                long time = System.nanoTime();
                tickTimeDebt += (time - lastRenderTime) / 1_000_000_000.0f;
                float timePerTick = 1.0f / loop.getTicksPerSecond();
                while (tickTimeDebt >= timePerTick) {
                    loop.update();
                    tickTimeDebt -= timePerTick;
                }
                // sleep
                time = System.nanoTime();
                try {
                    Thread.sleep((long) (time * 1_000_000 + timePerTick));
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }

}
