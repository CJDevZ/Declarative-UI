package eu.cj4.declarativeui.mixin;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.llamalad7.mixinextras.sugar.Local;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.builder.ArgumentBuilder;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.builder.RequiredArgumentBuilder;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.exceptions.DynamicCommandExceptionType;
import com.mojang.brigadier.exceptions.SimpleCommandExceptionType;
import com.mojang.serialization.MapCodec;
import eu.cj4.declarativeui.api.container.NamespacedContainerHolder;
import eu.cj4.declarativeui.impl.command.item.ContainerItemAccessor;
import eu.cj4.declarativeui.impl.registry.DeclarativeUIRegistries;
import it.unimi.dsi.fastutil.ints.IntArrayList;
import it.unimi.dsi.fastutil.ints.IntList;
import net.minecraft.advancements.predicates.MinMaxBounds;
import net.minecraft.commands.CommandBuildContext;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.commands.arguments.RangeArgument;
import net.minecraft.commands.arguments.ResourceKeyArgument;
import net.minecraft.commands.arguments.ResourceOrIdArgument;
import net.minecraft.commands.arguments.SlotSourceArgument;
import net.minecraft.commands.arguments.item.ItemArgument;
import net.minecraft.core.Holder;
import net.minecraft.resources.Identifier;
import net.minecraft.server.commands.ArgProvider;
import net.minecraft.server.commands.CommandResponseTracker;
import net.minecraft.server.commands.item.ItemAccessor;
import net.minecraft.server.commands.item.ItemCommands;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.Container;
import net.minecraft.world.item.ItemProvider;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.slot.SlotCollection;
import net.minecraft.world.item.slot.SlotSelector;
import net.minecraft.world.item.slot.SlotSource;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.functions.LootItemFunction;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;
import org.apache.commons.lang3.mutable.MutableInt;
import org.jspecify.annotations.NonNull;
import org.jspecify.annotations.Nullable;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;

import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.IntStream;

@Mixin(ItemCommands.class)
public class ItemCommandsMixin {
    @Shadow
    private static ArgumentBuilder<CommandSourceStack, ?> wrapSetItems(CommandBuildContext context, ArgumentBuilder<CommandSourceStack, ?> builder, ArgProvider<ItemAccessor<?>> targetProvider, ItemCommands.ItemDistributor distributor) {
        throw new UnsupportedOperationException("Implemented via mixin");
    }

    @Shadow
    private static <Target> int modifyItems(CommandSourceStack source, ItemAccessor<Target> accessor, SlotSourceArgument.Result slotSource, Holder<LootItemFunction> modifier) throws CommandSyntaxException {
        throw new UnsupportedOperationException("Implemented via mixin");
    }

    @Shadow
    private static int setItem(CommandSourceStack source, ItemAccessor<?> accessor, SlotSourceArgument.Result slotSource, ItemCommands.ItemDistributor distributor, ItemStack item) throws CommandSyntaxException {
        throw new UnsupportedOperationException("Implemented via mixin");
    }

    @Shadow
    private static <Target> int setItems(CommandSourceStack source, ItemAccessor<Target> accessor, SlotSourceArgument.Result slotSource, ItemProvider items, @Nullable ItemStack knownItem) throws CommandSyntaxException {
        throw new UnsupportedOperationException("Implemented via mixin");
    }

    @Shadow
    private static Collection<ItemStack> getItems(CommandSourceStack source, ItemAccessor<?> accessor, SlotSourceArgument.Result slotSource, @Nullable Holder<LootItemFunction> modifier) throws CommandSyntaxException {
        throw new UnsupportedOperationException("Implemented via mixin");
    }

    @Shadow
    private static int setItems(CommandSourceStack source, ItemAccessor<?> accessor, SlotSourceArgument.Result slotSource, ItemCommands.ItemDistributor distributor, Collection<ItemStack> items) throws CommandSyntaxException {
        throw new UnsupportedOperationException("Implemented via mixin");
    }

    @Shadow
    @Final
    public static DynamicCommandExceptionType ERROR_TARGET_INAPPLICABLE_SLOT;

    @Shadow
    @Final
    private static SimpleCommandExceptionType ERROR_TARGET_INAPPLICABLE_UNNAMED_SLOT;

    @Unique
    private static <Target> int declarativeUI$setItems(final CommandSourceStack source, final ItemAccessor<Target> accessor, final MinMaxBounds.Ints slotSource, final ItemProvider items, final @Nullable ItemStack knownItem) throws CommandSyntaxException {
        MutableInt selectedCount = new MutableInt();
        SlotSelector slotSelector = SlotSelector.tracking(selectedCount);
        CommandResponseTracker<Target> tracker = CommandResponseTracker.create();
        accessor.setItems(source, null, (target, slots) -> {
            items.restart();
            int count = slots.replaceSlotItems(items, slotSelector);
            tracker.track(target, count);
            return count;
        });
        if (selectedCount.intValue() == 0) {
            throw ERROR_TARGET_INAPPLICABLE_SLOT.create(slotSource.toString());
        } else {
            return accessor.getReplaceSuccess(source, tracker, knownItem);
        }
    }

    @Unique
    private static int declarativeUI$setItems(final CommandSourceStack source, final ItemAccessor<?> accessor, final MinMaxBounds.Ints slotSource, final ItemCommands.ItemDistributor distributor, final Collection<ItemStack> items) throws CommandSyntaxException {
        return declarativeUI$setItems(source, accessor, slotSource, distributor.apply(items), null);
    }

    @Unique
    private static int declarativeUI$setItems(final CommandSourceStack source, final ItemAccessor<?> accessor, final MinMaxBounds.Ints slotSource, final ItemCommands.ItemDistributor distributor, final ItemStack item) throws CommandSyntaxException {
        return declarativeUI$setItems(source, accessor, slotSource, distributor.apply(List.of(item)), item);
    }

    @Unique
    private static ArgumentBuilder<CommandSourceStack, ?> declarativeUI$wrapSetItems(final CommandBuildContext context, final ArgumentBuilder<CommandSourceStack, ?> builder, final ArgProvider<ItemAccessor<?>> targetProvider, final ItemCommands.ItemDistributor distributor) {
        return targetProvider.wrap(builder, (t) -> {
            ArgumentBuilder<CommandSourceStack, ?> slotsNode = Commands.argument("slots", RangeArgument.intRange());

            for(ArgProvider<ItemAccessor<?>> sourceProvider : ItemCommands.SOURCE_PROVIDERS) {
                slotsNode.then(sourceProvider.wrap(Commands.literal("from"), (p) -> p.then(
                        Commands.argument("sourceSlots", SlotSourceArgument.slotSource(context))
                                .executes((c) ->
                                        setItems(c.getSource(), targetProvider.access(c), SlotSourceArgument.getSlotSource(c, "slots"), distributor, getItems((CommandSourceStack)c.getSource(), sourceProvider.access(c), SlotSourceArgument.getSlotSource(c, "sourceSlots"), null))
                                )
                                .then(Commands.argument("modifier", ResourceOrIdArgument.lootModifier(context))
                                        .executes(c -> setItems(c.getSource(), targetProvider.access(c), SlotSourceArgument.getSlotSource(c, "slots"), distributor, getItems((CommandSourceStack)c.getSource(), sourceProvider.access(c), SlotSourceArgument.getSlotSource(c, "sourceSlots"), ResourceOrIdArgument.getLootModifier(c, "modifier")))))
                                )
                        )
                );
            }

            slotsNode.then(Commands.literal("with")
                    .then(Commands.argument("item", ItemArgument.item(context))
                            .executes(c ->
                                    declarativeUI$setItems(
                                            c.getSource(),
                                            targetProvider.access(c),
                                            RangeArgument.Ints.getRange(c, "slots"),
                                            distributor,
                                            ItemArgument.getItem(c, "item").createItemStack(1)
                                    )
                            ).then(Commands.argument("count", IntegerArgumentType.integer(1, 99))
                                    .executes(c ->
                                            declarativeUI$setItems(
                                                    c.getSource(),
                                                    targetProvider.access(c),
                                                    RangeArgument.Ints.getRange(c, "slots"),
                                                    distributor,
                                                    ItemArgument.getItem(c, "item").createItemStack(IntegerArgumentType.getInteger(c, "count"))
                                            )
                                    )
                            )
                    )
            );
            return t.then(slotsNode);
        });
    }

    @WrapOperation(method = "register", at = @At(value = "INVOKE", target = "Lnet/minecraft/commands/Commands;literal(Ljava/lang/String;)Lcom/mojang/brigadier/builder/LiteralArgumentBuilder;", ordinal = 0))
    private static LiteralArgumentBuilder<CommandSourceStack> registerContainer(
            String literal,
            Operation<LiteralArgumentBuilder<CommandSourceStack>> original,
            @Local(argsOnly = true, name = "context") CommandBuildContext context
    ) {
        ArgProvider<ItemAccessor<?>> CONTAINER_PROVIDER = ContainerItemAccessor.PROVIDER.create("target");

        return original.call(literal).then(declarativeUI$wrapSetItems(context, Commands.literal("replace"), CONTAINER_PROVIDER, ItemCommands::replaceItemProvider)
                .then(declarativeUI$wrapSetItems(context, Commands.literal("fill"), CONTAINER_PROVIDER, ItemCommands::fillItemProvider)))
                .then(declarativeUI$wrapSetItems(context, Commands.literal("override"), CONTAINER_PROVIDER, ItemCommands::overrideItemProvider))
                .then(CONTAINER_PROVIDER.wrap(Commands.literal("modify"), (p) -> p.then(Commands.argument("sourceContainer", ResourceKeyArgument.key(DeclarativeUIRegistries.CONTAINER)).then(Commands.argument("slots", RangeArgument.intRange())
                        .then(Commands.argument("modifier", ResourceOrIdArgument.lootModifier(context))
                                .executes((c) -> {
                                    Identifier sourceContainer = ResourceKeyArgument.getRegistryKey(c, "sourceContainer", DeclarativeUIRegistries.CONTAINER, eu.cj4.declarativeui.impl.command.ItemCommands.ERROR_INVALID_CONTAINER).identifier();
                                    MinMaxBounds.Ints slots = RangeArgument.Ints.getRange(c, "slots");
                                    int minRange = slots.min().orElse(0);
                                    SlotSourceArgument.Result result = new SlotSourceArgument.HolderResult(Holder.direct(new SlotSource() {
                                        @Override
                                        public @NonNull MapCodec<? extends SlotSource> codec() {
                                            return null;
                                        }
                                        @Override
                                        public @NonNull SlotCollection provide(@NonNull LootContext context) {
                                            if (!(context.getOptional(LootContextParams.THIS_ENTITY) instanceof NamespacedContainerHolder containerHolder)) {
                                                return SlotCollection.EMPTY;
                                            }
                                            Container container = containerHolder.declarative_ui$namespacedContainer(sourceContainer);
                                            return container.getSlotsFromRange(IntArrayList.toList(IntStream.rangeClosed(minRange, slots.max().orElse(container.getContainerSize()))));
                                        }
                                    }));
                                    return modifyItems(c.getSource(), CONTAINER_PROVIDER.access(c), result, ResourceOrIdArgument.getLootModifier(c, "modifier"));
                                })
                        )
                ))));
    }
}
