package eu.cj4.declarativeui.impl.menu.slot.action;

import com.mojang.logging.LogUtils;
import com.mojang.serialization.MapCodec;
import eu.cj4.declarativeui.api.menu.Menu;
import eu.cj4.declarativeui.api.menu.slot.ClickAction;
import eu.pb4.sgui.api.gui.SlotGuiInterface;
import net.minecraft.commands.CommandResultCallback;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.commands.FunctionInstantiationException;
import net.minecraft.commands.execution.ExecutionContext;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.Identifier;
import net.minecraft.server.ServerFunctionManager;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.permissions.LevelBasedPermissionSet;
import net.minecraft.util.profiling.Profiler;
import net.minecraft.util.profiling.ProfilerFiller;
import org.jspecify.annotations.Nullable;
import org.slf4j.Logger;

public record FunctionClickAction(Identifier function) implements ClickAction {
    private static final Logger LOGGER = LogUtils.getLogger();

    public static final MapCodec<FunctionClickAction> MAP_CODEC = Identifier.CODEC.fieldOf("function").xmap(FunctionClickAction::new, FunctionClickAction::function);

    @Override
    public MapCodec<FunctionClickAction> codec() {
        return MAP_CODEC;
    }

    @Override
    public void click(Menu menu, SlotGuiInterface slotGui, @Nullable CompoundTag compoundTag) {
        ServerPlayer serverPlayer = slotGui.getPlayer();
        ServerFunctionManager functionManager = serverPlayer.level().getServer().getFunctions();
        functionManager.get(this.function).ifPresent(commandFunction -> {
            ProfilerFiller profilerFiller = Profiler.get();
            profilerFiller.push(() -> "function " + commandFunction.id());

            CommandSourceStack commandSourceStack = serverPlayer.createCommandSourceStack();
            CommandSourceStack commandSourceStack2 = commandSourceStack.withSuppressedOutput().withPermission(LevelBasedPermissionSet.GAMEMASTER);
            try {
                var instantiatedFunction = commandFunction.instantiate(compoundTag, commandSourceStack.dispatcher());
                Commands.executeCommandInContext(commandSourceStack, executionContext -> ExecutionContext.queueInitialFunctionCall(
                        executionContext,
                        instantiatedFunction,
                        commandSourceStack2,
                        CommandResultCallback.EMPTY
                ));
            } catch (FunctionInstantiationException e) {
                 LOGGER.warn("Failed to execute function {}", commandFunction.id(), e);
            } finally {
                profilerFiller.pop();
            }
        });
    }
}
