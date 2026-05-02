package eu.cj4.declarativeui.api.menu.slot.action;

import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.serialization.MapCodec;
import eu.cj4.declarativeui.impl.command.action.FunctionCommandAction;
import eu.cj4.declarativeui.impl.command.action.OpenMenuCommandAction;
import eu.cj4.declarativeui.api.menu.Menu;
import eu.cj4.declarativeui.impl.menu.SimpleMenu;
import eu.cj4.declarativeui.impl.registry.DeclarativeUIBuiltInRegistries;
import eu.pb4.sgui.api.gui.SlotGuiInterface;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;

public interface ClickAction {
    ClickActionType getType();

    void click(SimpleMenu menu, SlotGuiInterface slotGui) throws CommandSyntaxException;

    static FunctionCommandAction function(ResourceLocation functionId) {
        return new FunctionCommandAction(functionId);
    }

    static OpenMenuCommandAction openMenu(ResourceKey<Menu> menu) {
        return new OpenMenuCommandAction(menu);
    }
    
    static ClickActionType register(ResourceLocation id, MapCodec<? extends ClickAction> mapCodec) {
        return Registry.register(DeclarativeUIBuiltInRegistries.CLICK_ACTION_TYPE, id, new ClickActionType(mapCodec));
    }
}
