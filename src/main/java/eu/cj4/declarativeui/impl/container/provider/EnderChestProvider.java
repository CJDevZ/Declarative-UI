package eu.cj4.declarativeui.impl.container.provider;

import com.mojang.serialization.MapCodec;
import eu.cj4.declarativeui.api.container.ContainerProvider;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.Container;
import net.minecraft.world.entity.Entity;

public record EnderChestProvider() implements ContainerProvider {
    public static final EnderChestProvider INSTANCE = new EnderChestProvider();
    public static final MapCodec<EnderChestProvider> MAP_CODEC = MapCodec.unit(INSTANCE);

    @Override
    public MapCodec<EnderChestProvider> codec() {
        return MAP_CODEC;
    }

    @Override
    public Container getContainer(Entity entity) {
        if (entity instanceof ServerPlayer serverPlayer) {
            return serverPlayer.getEnderChestInventory();
        }
        return null;
    }
}
