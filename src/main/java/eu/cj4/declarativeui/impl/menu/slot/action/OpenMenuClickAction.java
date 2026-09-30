package eu.cj4.declarativeui.impl.menu.slot.action;

import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.serialization.MapCodec;
import eu.cj4.declarativeui.api.menu.slot.ClickAction;
import eu.cj4.declarativeui.api.menu.Menu;
import eu.cj4.declarativeui.impl.registry.DeclarativeUIRegistries;
import eu.pb4.sgui.api.gui.SlotBasedGui;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.codec.RegistryCodecs;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerPlayer;
import org.jspecify.annotations.Nullable;

import java.util.Collections;

public record OpenMenuClickAction(Holder<Menu> menu) implements ClickAction {
    public static final MapCodec<OpenMenuClickAction> MAP_CODEC = RegistryCodecs.holder(DeclarativeUIRegistries.MENU).fieldOf("menu").xmap(OpenMenuClickAction::new, OpenMenuClickAction::menu);

    @Override
    public MapCodec<OpenMenuClickAction> codec() {
        return MAP_CODEC;
    }

    @Override
    public void click(Menu thisMenu, SlotBasedGui gui, @Nullable CompoundTag compoundTag) throws CommandSyntaxException {
        ServerPlayer serverPlayer = gui.getPlayer();
        Menu menu = this.menu.value();
        menu.open(serverPlayer.createCommandSourceStack(), Collections.singletonList(serverPlayer));
    }
}
