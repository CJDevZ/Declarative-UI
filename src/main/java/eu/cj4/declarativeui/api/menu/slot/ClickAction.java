package eu.cj4.declarativeui.api.menu.slot;

import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.serialization.MapCodec;
import eu.cj4.declarativeui.api.menu.Menu;
import eu.cj4.declarativeui.impl.menu.slot.action.*;
import eu.cj4.declarativeui.impl.registry.DeclarativeUIBuiltInRegistries;
import eu.pb4.sgui.api.gui.SlotGuiInterface;
import net.minecraft.core.Registry;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import org.jspecify.annotations.Nullable;

public interface ClickAction {
    MapCodec<? extends ClickAction> codec();

    default void click(Menu menu, SlotGuiInterface slotGui) throws CommandSyntaxException {
        click(menu, slotGui, null);
    }
    void click(Menu menu, SlotGuiInterface slotGui, @Nullable CompoundTag compoundTag) throws CommandSyntaxException;

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

    static RefreshMenuTitleClickAction refreshMenuTitle() {
        return RefreshMenuTitleClickAction.INSTANCE;
    }

    static <T extends ClickAction> MapCodec<T> register(Identifier id, MapCodec<T> mapCodec) {
        return Registry.register(DeclarativeUIBuiltInRegistries.CLICK_ACTION_TYPE, id, mapCodec);
    }
}
