package com.phoenixkahlo.resourcegame.hub;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.phoenixkahlo.nodenet.NodeAddress;
import com.phoenixkahlo.resourcegame.hub.entity.Avatar;
import com.phoenixkahlo.resourcegame.hub.entity.Entity;
import com.phoenixkahlo.resourcegame.hub.interactor.AvatarInteractor;
import com.phoenixkahlo.resourcegame.multiplayer.ReversibleMutator;
import com.phoenixkahlo.resourcegame.multiplayer.World;
import com.phoenixkahlo.resourcegame.multiplayer.WorldInput;
import com.phoenixkahlo.resourcegame.multiplayer.WorldInteractor;
import com.phoenixkahlo.resourcegame.multiplayer.reversibles.*;

import java.util.*;
import java.util.stream.Stream;

/**
 * Created by Phoenix on 5/16/2017.
 */
public class HubWorld implements World<HubWorld, HubClient> {

    private SortedMap<UUID, Entity> entities;
    private SortedMap<UpdateStage, SortedMap<UUID, Entity>> updateTree;
    private SortedMap<RenderStage, SortedMap<UUID, Entity>> renderTree;
    private Map<NodeAddress, Avatar> avatars;

    public HubWorld() {
        entities = new TreeMap<>();
        updateTree = new TreeMap<>();
        for (UpdateStage stage : UpdateStage.values())
            updateTree.put(stage, new TreeMap<>());
        renderTree = new TreeMap<>();
        for (RenderStage stage : RenderStage.values())
            renderTree.put(stage, new TreeMap<>());
        avatars = new HashMap<>();
    }

    @Override
    public Stream<? extends ReversibleMutator<HubWorld>> update() {
        return updateTree.values().stream() // stream of bins of entities
                .flatMap(bin -> bin.values().stream()) // stream of entities
                .flatMap(Entity::update); // stream of update mutators
    }

    @Override
    public WorldInteractor<HubWorld, HubClient> getInteractor(NodeAddress address, HubClient client) {
        return new AvatarInteractor(address);
    }

    public ReversibleMutator<HubWorld> addEntity(Entity entity) {
        return new MultipleReversible<HubWorld>(
                new MapPut<HubWorld, UUID, Entity>( // add to the entity collection
                        world -> world.entities,
                        entity.getID(),
                        entity
                ),
                new MapPut<HubWorld, UUID, Entity>( // add to the update tree
                        world -> world.updateTree.get(entity.getUpdateStage()),
                        entity.getID(),
                        entity
                ),
                new MapPut<HubWorld, UUID, Entity>( // add to the render tree
                        world -> world.renderTree.get(entity.getRenderStage()),
                        entity.getID(),
                        entity
                )
        );
    }

    public ReversibleMutator<HubWorld> removeEntity(Entity entity) {
        return new MultipleReversible<HubWorld>(
                new MapRemove<HubWorld, UUID, Entity>( // remove from the entity collection
                        world -> world.entities,
                        entity.getID()
                ),
                new MapRemove<HubWorld, UUID, Entity>( // remove from the update tree
                        world -> world.updateTree.get(entity.getUpdateStage()),
                        entity.getID()
                ),
                new MapRemove<HubWorld, UUID, Entity>( // remove from the render tree
                        world -> world.renderTree.get(entity.getRenderStage()),
                        entity.getID()
                )
        );
    }

    public ReversibleMutator<HubWorld> setAvatar(NodeAddress address, Avatar entity) {
        return new MultipleReversible<HubWorld>(
                addEntity(entity), // add the entity
                new MapPut<HubWorld, NodeAddress, Avatar>( // add to the avatars map
                        world -> world.avatars,
                        address,
                        entity
                )
        );
    }

    public ReversibleMutator<HubWorld> removeAvatar(NodeAddress address) {
        return new SituationalReversible<HubWorld>(
                world -> world.avatars.containsKey(address), // if there is an avatar with that address
                world -> new MultipleReversible<HubWorld>(
                        removeEntity(world.avatars.get(address)), // remove the entity from entity structures
                        new MapRemove<HubWorld, NodeAddress, Avatar>( // and remove the entity from the avatar map
                                world_ -> world_.avatars,
                                address
                        )
                )
        );
    }

    @Override
    public WorldInput<HubWorld, HubClient> handleEnter(NodeAddress client, long time) {
        return new WorldInput<HubWorld, HubClient>(time) {
            @Override
            public Stream<? extends ReversibleMutator> toMutators(HubWorld world) {
                return Stream.of(setAvatar(client, new Avatar()));
            }
        };
    }

    @Override
    public WorldInput<HubWorld, HubClient> handleLeave(NodeAddress client, long time) {
        return new WorldInput<HubWorld, HubClient>(time) {
            @Override
            public Stream<? extends ReversibleMutator<HubWorld>> toMutators(HubWorld world) {
                return Stream.of(removeAvatar(client));
            }
        };
    }

    public Entity getEntity(UUID id) {
        return entities.get(id);
    }

    public Avatar getAvatar(NodeAddress client) {
        return avatars.get(client);
    }

    public Stream<Sprite> getSprites(LocalHubClient client) {
        return renderTree.values().stream()
                .flatMap(bin -> bin.values().stream())
                .flatMap(entity -> entity.getSprites(client));
    }

}
