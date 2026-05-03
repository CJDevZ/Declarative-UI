package eu.cj4.declarativeui.impl.menu.slot.action;

import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.serialization.MapCodec;
import eu.cj4.declarativeui.api.menu.slot.action.ClickAction;
import eu.cj4.declarativeui.api.menu.slot.action.ClickActionType;
import eu.cj4.declarativeui.api.menu.Menu;
import eu.cj4.declarativeui.impl.registry.DeclarativeUIRegistries;
import eu.pb4.sgui.api.gui.SlotBasedGui;
import net.minecraft.core.Registry;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.level.ServerPlayer;
import org.jspecify.annotations.Nullable;

import java.util.Collections;

public record OpenMenuClickAction(ResourceKey<Menu> menu) implements ClickAction {
    public static final MapCodec<OpenMenuClickAction> CODEC = ResourceKey.codec(DeclarativeUIRegistries.MENU).fieldOf("menu").xmap(OpenMenuClickAction::new, OpenMenuClickAction::menu);

    @Override
    public ClickActionType getType() {
        return ClickActionTypes.OPEN_MENU;
    }

    @Override
    public void click(Menu thisMenu, SlotBasedGui slotBasedGui, @Nullable CompoundTag compoundTag) throws CommandSyntaxException {
        ServerPlayer serverPlayer = slotBasedGui.getPlayer();
        Registry<Menu> MENU_REGISTRY = serverPlayer.registryAccess().lookupOrThrow(DeclarativeUIRegistries.MENU);
        Menu menu = MENU_REGISTRY.getValueOrThrow(this.menu);
        menu.open(serverPlayer.createCommandSourceStack(), Collections.singletonList(serverPlayer));
    }
}
