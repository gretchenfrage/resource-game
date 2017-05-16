package com.phoenixkahlo.resourcegame.util;

import com.phoenixkahlo.nodenet.proxy.Proxy;

/**
 * Created by kahlo on 5/15/2017.
 */
public interface ProxyFactory {

    <E> Proxy<E> apply(E object, Class<E> intrface);

}
