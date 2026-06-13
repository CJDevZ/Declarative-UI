package eu.cj4.declarativeui.api.menu.slot;

import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.serialization.MapCodec;
import eu.cj4.declarativeui.api.menu.Menu;
import eu.cj4.declarativeui.impl.menu.slot.action.CloseMenuClickAction;
import eu.cj4.declarativeui.impl.menu.slot.action.FunctionClickAction;
import eu.cj4.declarativeui.impl.menu.slot.action.OpenMenuClickAction;
import eu.cj4.declarativeui.impl.menu.slot.action.RefreshMenuClickAction;
import eu.cj4.declarativeui.impl.registry.DeclarativeUIBuiltInRegistries;
import eu.pb4.sgui.api.gui.SlotBasedGui;
import net.minecraft.core.Registry;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import org.jspecify.annotations.Nullable;

public interface ClickAction {
    MapCodec<? extends ClickAction> codec();

    default void click(Menu menu, SlotBasedGui gui) throws CommandSyntaxException {
        click(menu, gui, null);
    }
    void click(Menu menu, SlotBasedGui gui, @Nullable CompoundTag compoundTag) throws CommandSyntaxException;

    static FunctionClickAction function(Identifier functionId) {
        return new FunctionClickAction(functionId);
    }

    static OpenMenuClickAction openMenu(ResourceKey<Menu> menu) {
        return new OpenMenuClickAction(menu);
    }

    static CloseMenuClickAction closeMenu() {
        return CloseMenuClickAction.INSTANCE;
    }

    static RefreshMenuClickAction refreshMenu() {
        return RefreshMenuClickAction.INSTANCE;
    }

    static <T extends ClickAction> MapCodec<T> register(Identifier id, MapCodec<T> mapCodec) {
        return Registry.register(DeclarativeUIBuiltInRegistries.CLICK_ACTION_TYPE, id, mapCodec);
    }
}
