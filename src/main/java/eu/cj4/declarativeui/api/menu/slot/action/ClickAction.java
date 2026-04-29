package eu.cj4.declarativeui.api.menu.slot.action;

import com.mojang.serialization.MapCodec;
import eu.cj4.declarativeui.impl.command.action.FunctionAction;
import eu.cj4.declarativeui.impl.command.action.OpenMenuAction;
import eu.cj4.declarativeui.impl.menu.DeclaredMenu;
import eu.cj4.declarativeui.impl.registry.DeclarativeUIBuiltInRegistries;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;

public interface ClickAction {
    ClickActionType getType();

    static FunctionAction function(ResourceLocation functionId) {
        return new FunctionAction(functionId);
    }

    static OpenMenuAction openMenu(ResourceKey<DeclaredMenu> menu) {
        return new OpenMenuAction(menu);
    }
    
    static ClickActionType register(ResourceLocation id, MapCodec<? extends ClickAction> mapCodec) {
        return Registry.register(DeclarativeUIBuiltInRegistries.CLICK_ACTION_TYPE, id, new ClickActionType(mapCodec));
    }
}
