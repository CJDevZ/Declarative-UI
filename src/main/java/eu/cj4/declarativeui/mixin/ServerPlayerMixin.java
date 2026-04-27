package eu.cj4.declarativeui.mixin;

import com.mojang.authlib.GameProfile;
import eu.cj4.declarativeui.api.NamespacedContainerHolder;
import eu.cj4.declarativeui.api.container.DeclaredContainer;
import eu.cj4.declarativeui.api.registry.DeclarativeUIRegistries;
import eu.cj4.declarativeui.impl.container.PlayerContainer;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ClientInformation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.Container;
import net.minecraft.world.ItemStackWithSlot;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Mixin(ServerPlayer.class)
public abstract class ServerPlayerMixin extends Player implements NamespacedContainerHolder {
    @Unique
    private HashMap<ResourceKey<DeclaredContainer>, PlayerContainer> declarative_ui$containers;

    public ServerPlayerMixin(Level level, GameProfile gameProfile) {
        super(level, gameProfile);
    }

    @Inject(method = "<init>", at = @At("TAIL"))
    private void initContainers(MinecraftServer minecraftServer, ServerLevel serverLevel, GameProfile gameProfile, ClientInformation clientInformation, CallbackInfo ci) {
        declarative_ui$containers = new HashMap<>();
    }

    @Unique
    @Override
    public Container declarative_ui$namespacedContainer(ResourceKey<DeclaredContainer> resourceKey) {
        DeclaredContainer declaredContainer = this.registryAccess().lookupOrThrow(DeclarativeUIRegistries.CONTAINER_REGISTRY).getValue(resourceKey);
        if (declaredContainer == null) {
            return null;
        }
        return declarative_ui$containers.computeIfAbsent(resourceKey, resourceKey1 -> new PlayerContainer(declaredContainer.size()));
    }

    @Inject(method = "readAdditionalSaveData", at = @At("TAIL"))
    private void loadContainers(ValueInput valueInput, CallbackInfo ci) {
        ValueInput containers = valueInput.childOrEmpty("declarative_ui_containers");
        Registry<DeclaredContainer> containerRegistry = this.registryAccess().lookupOrThrow(DeclarativeUIRegistries.CONTAINER_REGISTRY);
        for (Map.Entry<ResourceKey<DeclaredContainer>, DeclaredContainer> entry : containerRegistry.entrySet()) {
            Optional<ValueInput.TypedInputList<ItemStackWithSlot>> slots = containers.list(entry.getKey().location().toString(), ItemStackWithSlot.CODEC);
            if (slots.isEmpty()) continue;
            PlayerContainer playerContainer = new PlayerContainer(entry.getValue().size());
            playerContainer.fromSlots(slots.get());
            declarative_ui$containers.put(entry.getKey(), playerContainer);
        }
    }

    @Inject(method = "addAdditionalSaveData", at = @At("TAIL"))
    private void saveContainers(ValueOutput valueOutput, CallbackInfo ci) {
        ValueOutput containers = valueOutput.child("declarative_ui_containers");
        for (Map.Entry<ResourceKey<DeclaredContainer>, PlayerContainer> entry : declarative_ui$containers.entrySet()) {
            PlayerContainer playerContainer = entry.getValue();
            if (playerContainer.isEmpty()) continue;
            playerContainer.storeAsSlots(containers.list(entry.getKey().location().toString(), ItemStackWithSlot.CODEC));
        }
    }
}
