package com.phoenixkahlo.resourcegame.multiplayer;

import com.badlogic.gdx.InputProcessor;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.LinkedList;
import java.util.Queue;

/**
 * Produces an InputProcessor which's invocations get placed into a buffer, which can
 * be drained into a target InputProcessor in a controlled manor.
 */
public class InputBufferer {

    private static class Invocation {
        Method method;
        Object[] args;

        Invocation(Method method, Object[] args) {
            this.method = method;
            this.args = args;
        }

        void apply(InputProcessor processor) {
            try {
                method.invoke(processor, args);
            } catch (IllegalAccessException | InvocationTargetException e) {
                e.printStackTrace();
            }
        }
    }

    private Queue<Invocation> invocations = new LinkedList<>();

    public InputProcessor processor() {
        return (InputProcessor) Proxy.newProxyInstance(
                ClassLoader.getSystemClassLoader(),
                new Class<?>[] {InputProcessor.class},
                (proxy, method, args) -> {
                    synchronized (invocations) {
                        invocations.add(new Invocation(method, args));
                    }
                    return true;
                }
        );
    }

    public void flush(InputProcessor processor) {
        synchronized (invocations) {
            while (!invocations.isEmpty()) {
                invocations.remove().apply(processor);
            }
        }
    }

    public void clear() {
        synchronized (invocations) {
            invocations.clear();
        }
    }

}
