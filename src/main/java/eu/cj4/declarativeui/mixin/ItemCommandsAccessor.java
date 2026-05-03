package eu.cj4.declarativeui.mixin;

import com.mojang.brigadier.exceptions.Dynamic2CommandExceptionType;
import com.mojang.brigadier.exceptions.DynamicCommandExceptionType;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.server.commands.ItemCommands;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.SlotProvider;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.storage.loot.functions.LootItemFunction;
import org.jspecify.annotations.NonNull;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;
import org.spongepowered.asm.mixin.gen.Invoker;

import java.util.Collection;

@Mixin(ItemCommands.class)
public interface ItemCommandsAccessor {
    @Invoker
    static @NonNull ItemStack callApplyModifier(CommandSourceStack commandSourceStack, Holder<LootItemFunction> holder, ItemStack itemStack) {
        throw new UnsupportedOperationException();
    }

    @Invoker
    static @NonNull ItemStack callGetItemInSlot(SlotProvider slotProvider, int slot) {
        throw new UnsupportedOperationException();
    }

    @Invoker
    static @NonNull ItemStack callGetBlockItem(CommandSourceStack commandSourceStack, BlockPos blockPos, int slot) {
        throw new UnsupportedOperationException();
    }

    @Invoker
    static int callSetEntityItem(CommandSourceStack commandSourceStack, Collection<? extends Entity> collection, int slot, ItemStack itemStack) {
        throw new UnsupportedOperationException();
    }

    @Invoker
    static int callSetBlockItem(CommandSourceStack commandSourceStack, BlockPos blockPos, int slot, ItemStack itemStack) {
        throw new UnsupportedOperationException();
    }

    @Accessor
    static @NonNull DynamicCommandExceptionType getERROR_SOURCE_INAPPLICABLE_SLOT() {
        throw new UnsupportedOperationException();
    }

    @Accessor
    static @NonNull DynamicCommandExceptionType getERROR_TARGET_NO_CHANGES() {
        throw new UnsupportedOperationException();
    }

    @Accessor
    static @NonNull Dynamic2CommandExceptionType getERROR_TARGET_NO_CHANGES_KNOWN_ITEM() {
        throw new UnsupportedOperationException();
    }
}
