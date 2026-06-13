package eu.cj4.declarativeui.impl.menu.slot.action;

import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.serialization.MapCodec;
import eu.cj4.declarativeui.api.menu.Menu;
import eu.cj4.declarativeui.api.menu.slot.ClickAction;
import eu.pb4.sgui.api.gui.SlotGuiInterface;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerPlayer;
import org.jspecify.annotations.Nullable;

import java.util.Collections;

public record RefreshMenuClickAction() implements ClickAction {
    public static final RefreshMenuClickAction INSTANCE = new RefreshMenuClickAction();
    public static final MapCodec<RefreshMenuClickAction> MAP_CODEC = MapCodec.unit(INSTANCE);

    @Override
    public MapCodec<RefreshMenuClickAction> codec() {
        return MAP_CODEC;
    }

    @Override
    public void click(Menu menu, SlotGuiInterface slotGui, @Nullable CompoundTag compoundTag) throws CommandSyntaxException {
        ServerPlayer serverPlayer = slotGui.getPlayer();
        CommandSourceStack commandSourceStack = serverPlayer.createCommandSourceStack();
        menu.open(commandSourceStack, Collections.singleton(serverPlayer));
    }
}
