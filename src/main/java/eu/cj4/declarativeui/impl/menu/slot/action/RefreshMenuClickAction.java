package eu.cj4.declarativeui.impl.menu.slot.action;

import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import eu.cj4.declarativeui.api.menu.Menu;
import eu.cj4.declarativeui.api.menu.slot.action.ClickAction;
import eu.cj4.declarativeui.api.menu.slot.action.ClickActionType;
import eu.pb4.sgui.api.gui.SlotGuiInterface;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.permissions.LevelBasedPermissionSet;
import org.jspecify.annotations.Nullable;

import java.util.Collections;

public record RefreshMenuClickAction() implements ClickAction {
    public static final MapCodec<RefreshMenuClickAction> CODEC = RecordCodecBuilder.build(RecordCodecBuilder.stable(new RefreshMenuClickAction()));

    @Override
    public ClickActionType getType() {
        return ClickActionTypes.REFRESH_MENU;
    }

    @Override
    public void click(Menu menu, SlotGuiInterface slotGui, @Nullable CompoundTag compoundTag) throws CommandSyntaxException {
        ServerPlayer serverPlayer = slotGui.getPlayer();
        CommandSourceStack commandSourceStack = serverPlayer.createCommandSourceStack().withSuppressedOutput().withPermission(LevelBasedPermissionSet.GAMEMASTER);
        menu.open(commandSourceStack, Collections.singleton(serverPlayer));
    }
}
