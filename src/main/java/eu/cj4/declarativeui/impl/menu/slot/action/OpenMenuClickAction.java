package eu.cj4.declarativeui.impl.menu.slot.action;

import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.serialization.MapCodec;
import eu.cj4.declarativeui.api.menu.slot.action.ClickAction;
import eu.cj4.declarativeui.api.menu.slot.action.ClickActionType;
import eu.cj4.declarativeui.impl.menu.DeclaredMenu;
import eu.cj4.declarativeui.impl.registry.DeclarativeUIRegistries;
import eu.pb4.sgui.api.gui.SlotGuiInterface;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.level.ServerPlayer;

import java.util.Collections;

public record OpenMenuClickAction(ResourceKey<DeclaredMenu> menu) implements ClickAction {
    public static final MapCodec<OpenMenuClickAction> CODEC = ResourceKey.codec(DeclarativeUIRegistries.MENU_REGISTRY).fieldOf("menu").xmap(OpenMenuClickAction::new, OpenMenuClickAction::menu);

    @Override
    public ClickActionType getType() {
        return ClickActionTypes.OPEN_MENU;
    }

    @Override
    public void click(DeclaredMenu thisMenu, SlotGuiInterface slotGui) throws CommandSyntaxException {
        ServerPlayer serverPlayer = slotGui.getPlayer();
        Registry<DeclaredMenu> MENU_REGISTRY = serverPlayer.registryAccess().lookupOrThrow(DeclarativeUIRegistries.MENU_REGISTRY);
        DeclaredMenu declaredMenu = MENU_REGISTRY.getValueOrThrow(this.menu);
        declaredMenu.open(serverPlayer.createCommandSourceStack(), Collections.singletonList(serverPlayer));
    }
}
