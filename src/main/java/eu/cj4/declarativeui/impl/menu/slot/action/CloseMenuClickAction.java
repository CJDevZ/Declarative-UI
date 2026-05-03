package eu.cj4.declarativeui.impl.menu.slot.action;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import eu.cj4.declarativeui.api.menu.Menu;
import eu.cj4.declarativeui.api.menu.slot.action.ClickAction;
import eu.cj4.declarativeui.api.menu.slot.action.ClickActionType;
import eu.pb4.sgui.api.gui.SlotBasedGui;
import net.minecraft.nbt.CompoundTag;
import org.jspecify.annotations.Nullable;

public record CloseMenuClickAction() implements ClickAction {
    public static final MapCodec<CloseMenuClickAction> CODEC = RecordCodecBuilder.build(RecordCodecBuilder.stable(new CloseMenuClickAction()));

    @Override
    public ClickActionType getType() {
        return ClickActionTypes.REFRESH_MENU;
    }

    @Override
    public void click(Menu declaredMenu, SlotBasedGui slotBasedGui, @Nullable CompoundTag compoundTag) {
        slotBasedGui.close();
    }
}
