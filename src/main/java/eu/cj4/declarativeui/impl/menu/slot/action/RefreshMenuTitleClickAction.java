package eu.cj4.declarativeui.impl.menu.slot.action;

import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import eu.cj4.declarativeui.api.menu.Menu;
import eu.cj4.declarativeui.api.menu.slot.action.ClickAction;
import eu.cj4.declarativeui.api.menu.slot.action.ClickActionType;
import eu.pb4.sgui.api.gui.SlotBasedGui;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.ComponentUtils;
import net.minecraft.network.chat.ResolutionContext;
import net.minecraft.server.level.ServerPlayer;
import org.jspecify.annotations.Nullable;

public record RefreshMenuTitleClickAction() implements ClickAction {
    public static final MapCodec<RefreshMenuTitleClickAction> CODEC = RecordCodecBuilder.build(RecordCodecBuilder.stable(new RefreshMenuTitleClickAction()));

    @Override
    public ClickActionType getType() {
        return ClickActionTypes.REFRESH_MENU;
    }

    @Override
    public void click(Menu menu, SlotBasedGui slotBasedGui, @Nullable CompoundTag compoundTag) throws CommandSyntaxException {
        ServerPlayer serverPlayer = slotBasedGui.getPlayer();
        CommandSourceStack commandSourceStack = serverPlayer.createCommandSourceStack();
        slotBasedGui.setTitle(ComponentUtils.resolve(ResolutionContext.create(commandSourceStack), menu.title().orElse(menu.getDefaultTitle(serverPlayer.registryAccess()))));
        slotBasedGui.open();
    }
}
