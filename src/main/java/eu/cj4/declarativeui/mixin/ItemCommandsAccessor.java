package eu.cj4.declarativeui.mixin;

import com.mojang.brigadier.builder.ArgumentBuilder;
import com.mojang.brigadier.exceptions.Dynamic2CommandExceptionType;
import com.mojang.brigadier.exceptions.DynamicCommandExceptionType;
import com.mojang.brigadier.exceptions.SimpleCommandExceptionType;
import net.minecraft.commands.CommandBuildContext;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.arguments.SlotSourceArgument;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.server.commands.ArgProvider;
import net.minecraft.server.commands.item.ItemAccessor;
import net.minecraft.server.commands.item.ItemCommands;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.SlotProvider;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.slot.SlotCollection;
import net.minecraft.world.item.slot.SlotSource;
import net.minecraft.world.level.storage.loot.functions.LootItemFunction;
import org.jspecify.annotations.NonNull;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;
import org.spongepowered.asm.mixin.gen.Invoker;

import java.util.Collection;
import java.util.function.UnaryOperator;

@Mixin(ItemCommands.class)
public interface ItemCommandsAccessor {
    @Invoker
    static @NonNull UnaryOperator<ItemStack> callApplyModifier(CommandSourceStack commandSourceStack, LootItemFunction modifier) {
        throw new UnsupportedOperationException();
    }

    @Invoker
    static <Target> int callModifyItems(CommandSourceStack source, ItemAccessor<Target> accessor, SlotSourceArgument.Result slotSource, Holder<LootItemFunction> modifier) {
        throw new UnsupportedOperationException();
    }

    @Invoker
    static ArgumentBuilder<CommandSourceStack, ?> callWrapSetItems(CommandBuildContext context, ArgumentBuilder<CommandSourceStack, ?> builder, ArgProvider<ItemAccessor<?>> targetProvider, ItemCommands.ItemDistributor distributor) {
        throw new UnsupportedOperationException();
    }

    @Invoker
    static SlotCollection callGetSlotsFromProvider(CommandSourceStack source, SlotProvider container, SlotSource slotSource) {
        throw new UnsupportedOperationException();
    }

//    @Invoker
//    static @NonNull ItemStack callGetItemInSlot(SlotProvider slotProvider, int slot) {
//        throw new UnsupportedOperationException();
//    }
//
//    @Invoker
//    static @NonNull ItemStack callGetBlockItem(CommandSourceStack commandSourceStack, BlockPos blockPos, int slot) {
//        throw new UnsupportedOperationException();
//    }
//
//    @Invoker
//    static int callSetEntityItem(CommandSourceStack commandSourceStack, Collection<? extends Entity> collection, int slot, ItemStack itemStack) {
//        throw new UnsupportedOperationException();
//    }
//
//    @Invoker
//    static int callSetBlockItem(CommandSourceStack commandSourceStack, BlockPos blockPos, int slot, ItemStack itemStack) {
//        throw new UnsupportedOperationException();
//    }

    @Accessor
    static @NonNull DynamicCommandExceptionType getERROR_SOURCE_INAPPLICABLE_SLOT() {
        throw new UnsupportedOperationException();
    }
}
