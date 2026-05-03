package eu.cj4.declarativeui.api.menu.slot.action;

import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.serialization.MapCodec;
import eu.cj4.declarativeui.impl.command.action.FunctionCommandAction;
import eu.cj4.declarativeui.impl.command.action.OpenMenuCommandAction;
import eu.cj4.declarativeui.api.menu.Menu;
import eu.cj4.declarativeui.impl.registry.DeclarativeUIBuiltInRegistries;
import eu.pb4.sgui.api.gui.SlotBasedGui;
import net.minecraft.core.Registry;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import org.jspecify.annotations.Nullable;

public interface ClickAction {
    ClickActionType getType();

    default void click(Menu menu, SlotBasedGui slotBasedGui) throws CommandSyntaxException {
        click(menu, slotBasedGui, null);
    }
    void click(Menu menu, SlotBasedGui slotGui, @Nullable CompoundTag compoundTag) throws CommandSyntaxException;

    static FunctionCommandAction function(Identifier functionId) {
        return new FunctionCommandAction(functionId);
    }

    static OpenMenuCommandAction openMenu(ResourceKey<Menu> menu) {
        return new OpenMenuCommandAction(menu);
    }
    
    static ClickActionType register(Identifier id, MapCodec<? extends ClickAction> mapCodec) {
        return Registry.register(DeclarativeUIBuiltInRegistries.CLICK_ACTION_TYPE, id, new ClickActionType(mapCodec));
    }
}
