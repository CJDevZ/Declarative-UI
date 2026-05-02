package eu.cj4.declarativeui.impl.menu.slot.action;

import com.mojang.serialization.MapCodec;
import eu.cj4.declarativeui.api.menu.slot.action.ClickAction;
import eu.cj4.declarativeui.api.menu.slot.action.ClickActionType;
import eu.cj4.declarativeui.impl.menu.SimpleMenu;
import eu.pb4.sgui.api.gui.SlotGuiInterface;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.ServerFunctionManager;
import net.minecraft.server.level.ServerPlayer;

public record FunctionClickAction(ResourceLocation function) implements ClickAction {
    public static final MapCodec<FunctionClickAction> CODEC = ResourceLocation.CODEC.fieldOf("function").xmap(FunctionClickAction::new, FunctionClickAction::function);

    @Override
    public ClickActionType getType() {
        return ClickActionTypes.FUNCTION;
    }

    @Override
    public void click(SimpleMenu menu, SlotGuiInterface slotGui) {
        ServerPlayer serverPlayer = slotGui.getPlayer();
        ServerFunctionManager functionManager = serverPlayer.level().getServer().getFunctions();
        functionManager.get(this.function).ifPresent(commandFunction -> {
            CommandSourceStack commandSourceStack = serverPlayer.createCommandSourceStack().withSuppressedOutput().withPermission(Commands.LEVEL_GAMEMASTERS);
            functionManager.execute(commandFunction, commandSourceStack);
        });
    }
}
