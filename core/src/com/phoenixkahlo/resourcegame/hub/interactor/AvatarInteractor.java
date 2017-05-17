package com.phoenixkahlo.resourcegame.hub.interactor;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.phoenixkahlo.nodenet.NodeAddress;
import com.phoenixkahlo.nodenet.proxy.Proxy;
import com.phoenixkahlo.resourcegame.hub.HubClient;
import com.phoenixkahlo.resourcegame.hub.HubWorld;
import com.phoenixkahlo.resourcegame.hub.LocalHubClient;
import com.phoenixkahlo.resourcegame.hub.entity.Avatar;
import com.phoenixkahlo.resourcegame.multiplayer.WorldInteractor;

/**
 * Created by Phoenix on 5/16/2017.
 */
public class AvatarInteractor implements WorldInteractor<HubWorld, HubClient> {

    private NodeAddress address;

    private transient Proxy<AvatarInteractorReceiver> receiver;
    private transient LocalHubClient client;

    public AvatarInteractor(NodeAddress address) {
        this.address = address;
    }

    @Override
    public void onActivate(Proxy<?> receiver, HubClient gameClient) {
        this.receiver = receiver.cast(AvatarInteractorReceiver.class);
        this.client = (LocalHubClient) gameClient;
    }

    @Override
    public void render(HubWorld world, long time) {
        // set up camera
        Avatar avatar = world.getAvatar(address);
        OrthographicCamera camera = client.getCamera();
        camera.setToOrtho(false,
                Gdx.graphics.getWidth() / 100f,
                Gdx.graphics.getHeight() / 100f);
        camera.position.x = avatar.getPosition().x;
        camera.position.y = avatar.getPosition().y;
        // clear screen
        Gdx.gl.glClearColor(1, 1, 1, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        // render sprites
        SpriteBatch batch = client.getBatch();
        batch.begin();
        world.getSprites(client).forEach(sprite -> sprite.draw(batch));
        batch.end();
        // move avatar
        Vector2 vel = new Vector2(0, 0);
        if (Gdx.input.isKeyPressed(Input.Keys.D))
            vel.x += 1;
        if (Gdx.input.isKeyPressed(Input.Keys.A))
            vel.x -= 1;
        if (Gdx.input.isKeyPressed(Input.Keys.W))
            vel.y += 1;
        if (Gdx.input.isKeyPressed(Input.Keys.S))
            vel.y -= 1;
        float speed = Gdx.input.isKeyPressed(Input.Keys.SHIFT_LEFT) ? 2 : 1;
        vel.x *= speed;
        vel.y *= speed;
        receiver.blocking().setVelocity(vel, time);
    }

    @Override
    public void onDeactivate() {

    }

}
