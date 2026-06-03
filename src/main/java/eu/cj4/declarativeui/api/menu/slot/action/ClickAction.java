package eu.cj4.declarativeui.api.menu.slot.action;

import com.mojang.brigadier.exceptions.CommandSyntaxException;
import eu.cj4.declarativeui.impl.command.action.FunctionCommandAction;
import eu.cj4.declarativeui.impl.command.action.OpenMenuCommandAction;
import eu.cj4.declarativeui.api.menu.Menu;
import eu.cj4.declarativeui.impl.menu.slot.action.*;
import eu.pb4.sgui.api.gui.SlotGuiInterface;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import org.jspecify.annotations.Nullable;

public interface ClickAction {
    ClickActionType getType();

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
}
