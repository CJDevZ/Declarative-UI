package eu.cj4.declarativeui.impl.menu.slot.action;

import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import eu.cj4.declarativeui.api.menu.slot.action.ClickAction;
import eu.cj4.declarativeui.api.menu.slot.action.ClickActionType;
import eu.cj4.declarativeui.impl.menu.SimpleMenu;
import eu.pb4.sgui.api.gui.SlotGuiInterface;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.server.level.ServerPlayer;

import java.util.Collections;

public record RefreshMenuClickAction() implements ClickAction {
    public static final MapCodec<RefreshMenuClickAction> CODEC = RecordCodecBuilder.build(RecordCodecBuilder.stable(new RefreshMenuClickAction()));

    @Override
    public ClickActionType getType() {
        return ClickActionTypes.REFRESH_MENU;
    }

    @Override
    public void click(SimpleMenu menu, SlotGuiInterface slotGui) throws CommandSyntaxException {
        ServerPlayer serverPlayer = slotGui.getPlayer();
        CommandSourceStack commandSourceStack = serverPlayer.createCommandSourceStack().withSuppressedOutput().withPermission(Commands.LEVEL_GAMEMASTERS);
        menu.open(commandSourceStack, Collections.singleton(serverPlayer));
    }
}
