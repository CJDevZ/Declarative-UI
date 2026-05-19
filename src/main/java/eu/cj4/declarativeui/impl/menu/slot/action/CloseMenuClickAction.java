package eu.cj4.declarativeui.impl.menu.slot.action;

import com.mojang.serialization.MapCodec;
import eu.cj4.declarativeui.api.menu.Menu;
import eu.cj4.declarativeui.api.menu.slot.action.ClickAction;
import eu.cj4.declarativeui.api.menu.slot.action.ClickActionType;
import eu.pb4.sgui.api.gui.SlotGuiInterface;
import net.minecraft.nbt.CompoundTag;
import org.jspecify.annotations.Nullable;

public record CloseMenuClickAction() implements ClickAction {
    public static final CloseMenuClickAction INSTANCE = new CloseMenuClickAction();
    public static final MapCodec<CloseMenuClickAction> CODEC = MapCodec.unit(INSTANCE);

    @Override
    public ClickActionType getType() {
        return ClickActionTypes.REFRESH_MENU;
    }

    @Override
    public void click(Menu declaredMenu, SlotGuiInterface slotGui, @Nullable CompoundTag compoundTag) {
        slotGui.close();
    }
}
