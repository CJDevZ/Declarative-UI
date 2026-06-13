package eu.cj4.declarativeui.impl.menu.slot.action;

import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.serialization.MapCodec;
import eu.cj4.declarativeui.api.menu.Menu;
import eu.cj4.declarativeui.api.menu.slot.action.ClickAction;
import eu.cj4.declarativeui.api.menu.slot.action.ClickActionType;
import eu.pb4.sgui.api.gui.SlotGuiInterface;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.ComponentUtils;
import net.minecraft.server.level.ServerPlayer;
import org.jspecify.annotations.Nullable;

public record RefreshMenuTitleClickAction() implements ClickAction {
    public static final RefreshMenuTitleClickAction INSTANCE = new RefreshMenuTitleClickAction();
    public static final MapCodec<RefreshMenuTitleClickAction> MAP_CODEC = MapCodec.unit(INSTANCE);

    @Override
    public ClickActionType getType() {
        return ClickActionTypes.REFRESH_MENU;
    }

    @Override
    public void click(Menu menu, SlotGuiInterface slotGui, @Nullable CompoundTag compoundTag) throws CommandSyntaxException {
        ServerPlayer serverPlayer = slotGui.getPlayer();
        CommandSourceStack commandSourceStack = serverPlayer.createCommandSourceStack();
        slotGui.setTitle(ComponentUtils.updateForEntity(commandSourceStack, menu.title().orElse(menu.getDefaultTitle(serverPlayer.registryAccess())), serverPlayer, 0));
        slotGui.open();
    }
}
