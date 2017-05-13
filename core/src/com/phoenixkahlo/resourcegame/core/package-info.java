/**
 * <p>
 * This package contains an abstract framework of classes for creating a LibGDX state based game, including a state
 * that involves the client connecting to the world on a server. If extended correctly, this package can be used to
 * create a multiplayer game while minimizing network usage and boilerplate needed.
 * </p>
 * <p>
 * To create a game using this package, the classes World and Server must be reified (given full implementations).
 * Giving these classes meaningful implementations will also involve implementing WorldMutator, ExternalWorldMutator,
 * ClientView, and ClientController, probably multiple times. While the Server class is completely ready to accept
 * ClientState connections as soon as it's started, the ClientState must be fed a network and server proxy that have
 * already been created. This is so that the user of this package can exchange data with a server without actually
 * connecting, perhaps to query information like most video games do.
 * </p>
 * <p>
 * The user of this package will probably want to create GameState implementations other than ClientState, for example,
 * for a main menu.
 * </p>
 */
package com.phoenixkahlo.resourcegame.core;