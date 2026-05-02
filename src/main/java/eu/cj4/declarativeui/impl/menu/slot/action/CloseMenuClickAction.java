package eu.cj4.declarativeui.impl.menu.slot.action;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import eu.cj4.declarativeui.api.menu.slot.action.ClickAction;
import eu.cj4.declarativeui.api.menu.slot.action.ClickActionType;
import eu.cj4.declarativeui.impl.menu.SimpleMenu;
import eu.pb4.sgui.api.gui.SlotGuiInterface;

public record CloseMenuClickAction() implements ClickAction {
    public static final MapCodec<CloseMenuClickAction> CODEC = RecordCodecBuilder.build(RecordCodecBuilder.stable(new CloseMenuClickAction()));

    @Override
    public ClickActionType getType() {
        return ClickActionTypes.REFRESH_MENU;
    }

    @Override
    public void click(SimpleMenu declaredMenu, SlotGuiInterface slotGui) {
        slotGui.close();
    }
}
