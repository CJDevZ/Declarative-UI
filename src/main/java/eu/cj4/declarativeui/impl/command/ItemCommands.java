package eu.cj4.declarativeui.impl.command;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.exceptions.DynamicCommandExceptionType;
import eu.cj4.declarativeui.api.container.NamespacedContainerHolder;
import eu.cj4.declarativeui.impl.registry.DeclarativeUIRegistries;
import eu.cj4.declarativeui.mixin.ItemCommandsAccessor;
import eu.cj4.declarativeui.mixin.ResourceKeyArgumentAccessor;
import net.minecraft.commands.CommandBuildContext;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.commands.arguments.EntityArgument;
import net.minecraft.commands.arguments.ResourceKeyArgument;
import net.minecraft.commands.arguments.ResourceOrIdArgument;
import net.minecraft.commands.arguments.SlotArgument;
import net.minecraft.commands.arguments.coordinates.BlockPosArgument;
import net.minecraft.commands.arguments.item.ItemArgument;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.Container;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.storage.loot.functions.LootItemFunction;
import org.jspecify.annotations.NonNull;

import java.util.Collection;
import java.util.List;
import java.util.Map;

public final class ItemCommands {
    private static final DynamicCommandExceptionType ERROR_INVALID_CONTAINER = new DynamicCommandExceptionType((object) -> Component.literal(String.format("Unknown container: %s", object)));

    public static void register(CommandDispatcher<CommandSourceStack> dispatcher, CommandBuildContext buildContext) {
        var itemCommand = dispatcher.getRoot().getChild("item");
        var modifyCommand = itemCommand.getChild("modify");
        var replaceCommand = itemCommand.getChild("replace");

        modifyCommand.addChild(Commands.literal("container").then(Commands.argument("targets", EntityArgument.players()).then(Commands.argument("container", ResourceKeyArgument.key(DeclarativeUIRegistries.CONTAINER)).then(Commands.argument("slot", IntegerArgumentType.integer(0)).then(Commands.argument("modifier", ResourceOrIdArgument.lootModifier(buildContext)).executes(context -> modifyContainerItem(context.getSource(), EntityArgument.getPlayers(context, "targets"), ResourceKeyArgumentAccessor.callResolveKey(context, "container", DeclarativeUIRegistries.CONTAINER, ERROR_INVALID_CONTAINER).key().identifier(), IntegerArgumentType.getInteger(context, "slot"), ResourceOrIdArgument.getLootModifier(context, "modifier"))))))).build());
        replaceCommand.addChild(Commands.literal("container").then(Commands.argument("targets", EntityArgument.players()).then(Commands.argument("container", ResourceKeyArgument.key(DeclarativeUIRegistries.CONTAINER)).then(Commands.argument("slot", IntegerArgumentType.integer(0)).then(Commands.literal("with").then(Commands.argument("item", ItemArgument.item(buildContext)).executes(context -> setContainerItem(context.getSource(), EntityArgument.getPlayers(context, "targets"), ResourceKeyArgumentAccessor.callResolveKey(context, "container", DeclarativeUIRegistries.CONTAINER, ERROR_INVALID_CONTAINER).key().identifier(), IntegerArgumentType.getInteger(context, "slot"), ItemArgument.getItem(context, "item").createItemStack(1))).then(Commands.argument("count", IntegerArgumentType.integer(1, 99)).executes(context -> setContainerItem(context.getSource(), EntityArgument.getPlayers(context, "targets"), ResourceKeyArgumentAccessor.callResolveKey(context, "container", DeclarativeUIRegistries.CONTAINER, ERROR_INVALID_CONTAINER).key().identifier(), IntegerArgumentType.getInteger(context, "slot"), ItemArgument.getItem(context, "item").createItemStack(IntegerArgumentType.getInteger(context, "count")))))))
                .then(Commands.literal("from")
                        .then(Commands.literal("block")
                                .then(Commands.argument("source", BlockPosArgument.blockPos())
                                        .then(Commands.argument("sourceSlot", SlotArgument.slot()).executes(context ->
                                                blockToContainer(
                                                        context.getSource(),
                                                        BlockPosArgument.getBlockPos(context, "source"),
                                                        IntegerArgumentType.getInteger(context, "sourceSlot"),
                                                        EntityArgument.getPlayers(context, "targets"),
                                                        ResourceKeyArgumentAccessor.callResolveKey(context, "container", DeclarativeUIRegistries.CONTAINER, ERROR_INVALID_CONTAINER).key().identifier(),
                                                        IntegerArgumentType.getInteger(context, "slot")
                                                )).then(Commands.argument("modifier", ResourceOrIdArgument.lootModifier(buildContext)).executes(context ->
                                                blockToContainer(
                                                        context.getSource(),
                                                        BlockPosArgument.getBlockPos(context, "source"),
                                                        IntegerArgumentType.getInteger(context, "sourceSlot"),
                                                        EntityArgument.getPlayers(context, "targets"),
                                                        ResourceKeyArgumentAccessor.callResolveKey(context, "container", DeclarativeUIRegistries.CONTAINER, ERROR_INVALID_CONTAINER).key().identifier(),
                                                        IntegerArgumentType.getInteger(context, "slot"),
                                                        ResourceOrIdArgument.getLootModifier(context, "modifier")
                                                ))))))
                        .then(Commands.literal("entity").then(
                                Commands.argument("source", EntityArgument.entity()).then(
                                        Commands.argument("sourceSlot", SlotArgument.slot()).executes(context ->
                                                entityToContainer(
                                                        context.getSource(),
                                                        EntityArgument.getEntity(context, "source"),
                                                        SlotArgument.getSlot(context, "sourceSlot"),
                                                        EntityArgument.getPlayers(context, "targets"),
                                                        ResourceKeyArgumentAccessor.callResolveKey(context, "container", DeclarativeUIRegistries.CONTAINER, ERROR_INVALID_CONTAINER).key().identifier(),
                                                        IntegerArgumentType.getInteger(context, "slot")
                                                )).then(Commands.argument("modifier", ResourceOrIdArgument.lootModifier(buildContext)).executes(context ->
                                                entityToContainer(context.getSource(),
                                                        EntityArgument.getEntity(context, "source"),
                                                        SlotArgument.getSlot(context, "sourceSlot"),
                                                        EntityArgument.getPlayers(context, "targets"),
                                                        ResourceKeyArgumentAccessor.callResolveKey(context, "container", DeclarativeUIRegistries.CONTAINER, ERROR_INVALID_CONTAINER).key().identifier(),
                                                        IntegerArgumentType.getInteger(context, "slot"),
                                                        ResourceOrIdArgument.getLootModifier(context, "modifier")
                                                ))))))
                        .then(Commands.literal("container").then(
                                Commands.argument("source", EntityArgument.player()).then(
                                        Commands.argument("sourceContainer", ResourceKeyArgument.key(DeclarativeUIRegistries.CONTAINER)).then(
                                            Commands.argument("sourceSlot", IntegerArgumentType.integer()).executes(context ->
                                                    containerToContainer(
                                                            context.getSource(),
                                                            EntityArgument.getPlayer(context, "source"),
                                                            ResourceKeyArgumentAccessor.callResolveKey(context, "sourceContainer", DeclarativeUIRegistries.CONTAINER, ERROR_INVALID_CONTAINER).key().identifier(),
                                                            SlotArgument.getSlot(context, "sourceSlot"),
                                                            EntityArgument.getPlayers(context, "targets"),
                                                            ResourceKeyArgumentAccessor.callResolveKey(context, "container", DeclarativeUIRegistries.CONTAINER, ERROR_INVALID_CONTAINER).key().identifier(),
                                                            IntegerArgumentType.getInteger(context, "slot")
                                                    )).then(Commands.argument("modifier", ResourceOrIdArgument.lootModifier(buildContext)).executes(context ->
                                                    containerToContainer(context.getSource(),
                                                            EntityArgument.getPlayer(context, "source"),
                                                            ResourceKeyArgumentAccessor.callResolveKey(context, "sourceContainer", DeclarativeUIRegistries.CONTAINER, ERROR_INVALID_CONTAINER).key().identifier(),
                                                            SlotArgument.getSlot(context, "sourceSlot"),
                                                            EntityArgument.getPlayers(context, "targets"),
                                                            ResourceKeyArgumentAccessor.callResolveKey(context, "container", DeclarativeUIRegistries.CONTAINER, ERROR_INVALID_CONTAINER).key().identifier(),
                                                            IntegerArgumentType.getInteger(context, "slot"),
                                                            ResourceOrIdArgument.getLootModifier(context, "modifier")
                                                    )))))))
                )))).build());

        replaceCommand.getChild("block").getChild("pos").getChild("slot").getChild("from").addChild(
                Commands.literal("container").then(Commands.argument("source", EntityArgument.player()).then(
                        Commands.argument("sourceContainer", ResourceKeyArgument.key(DeclarativeUIRegistries.CONTAINER)).then(
                                Commands.argument("sourceSlot", IntegerArgumentType.integer()).executes(context ->
                                        containerToBlock(
                                                context.getSource(),
                                                EntityArgument.getPlayer(context, "source"),
                                                ResourceKeyArgumentAccessor.callResolveKey(context, "sourceContainer", DeclarativeUIRegistries.CONTAINER, ERROR_INVALID_CONTAINER).key().identifier(),
                                                IntegerArgumentType.getInteger(context, "sourceSlot"),
                                                BlockPosArgument.getBlockPos(context, "pos"),
                                                SlotArgument.getSlot(context, "slot")
                                        )).then(Commands.argument("modifier", ResourceOrIdArgument.lootModifier(buildContext)).executes(context ->
                                        containerToBlock(
                                                context.getSource(),
                                                EntityArgument.getPlayer(context, "source"),
                                                ResourceKeyArgumentAccessor.callResolveKey(context, "sourceContainer", DeclarativeUIRegistries.CONTAINER, ERROR_INVALID_CONTAINER).key().identifier(),
                                                IntegerArgumentType.getInteger(context, "sourceSlot"),
                                                BlockPosArgument.getBlockPos(context, "pos"),
                                                SlotArgument.getSlot(context, "slot"),
                                                ResourceOrIdArgument.getLootModifier(context, "modifier")
                                        )))))).build());
        replaceCommand.getChild("entity").getChild("targets").getChild("slot").getChild("from").addChild(
                Commands.literal("container").then(Commands.argument("source", EntityArgument.player()).then(
                        Commands.argument("sourceContainer", ResourceKeyArgument.key(DeclarativeUIRegistries.CONTAINER)).then(
                                Commands.argument("sourceSlot", IntegerArgumentType.integer()).executes(context ->
                                        containerToEntity(
                                                context.getSource(),
                                                EntityArgument.getPlayer(context, "source"),
                                                ResourceKeyArgumentAccessor.callResolveKey(context, "sourceContainer", DeclarativeUIRegistries.CONTAINER, ERROR_INVALID_CONTAINER).key().identifier(),
                                                IntegerArgumentType.getInteger(context, "sourceSlot"),
                                                EntityArgument.getEntities(context, "targets"),
                                                SlotArgument.getSlot(context, "slot")
                                        )).then(Commands.argument("modifier", ResourceOrIdArgument.lootModifier(buildContext)).executes(context ->
                                        containerToEntity(
                                                context.getSource(),
                                                EntityArgument.getPlayer(context, "source"),
                                                ResourceKeyArgumentAccessor.callResolveKey(context, "sourceContainer", DeclarativeUIRegistries.CONTAINER, ERROR_INVALID_CONTAINER).key().identifier(),
                                                IntegerArgumentType.getInteger(context, "sourceSlot"),
                                                EntityArgument.getEntities(context, "targets"),
                                                SlotArgument.getSlot(context, "slot"),
                                                ResourceOrIdArgument.getLootModifier(context, "modifier")
                                        )))))).build());
    }

    private static int blockToContainer(CommandSourceStack commandSourceStack, BlockPos blockPos, int sourceSlot, Collection<ServerPlayer> collection, @NonNull Identifier targetContainer, int targetSlot) throws CommandSyntaxException {
        return setContainerItem(commandSourceStack, collection, targetContainer, targetSlot, ItemCommandsAccessor.callGetBlockItem(commandSourceStack, blockPos, sourceSlot));
    }

    private static int blockToContainer(CommandSourceStack commandSourceStack, BlockPos blockPos, int sourceSlot, Collection<ServerPlayer> collection, @NonNull Identifier targetContainer, int targetSlot, Holder<LootItemFunction> holder) throws CommandSyntaxException {
        return setContainerItem(commandSourceStack, collection, targetContainer, targetSlot, ItemCommandsAccessor.callApplyModifier(commandSourceStack, holder, ItemCommandsAccessor.callGetBlockItem(commandSourceStack, blockPos, sourceSlot)));
    }

    private static int entityToContainer(CommandSourceStack commandSourceStack, Entity entity, int sourceSlot, Collection<ServerPlayer> collection, @NonNull Identifier targetContainer, int targetSlot) throws CommandSyntaxException {
        return setContainerItem(commandSourceStack, collection, targetContainer, targetSlot, ItemCommandsAccessor.callGetItemInSlot(entity, sourceSlot));
    }

    private static int entityToContainer(CommandSourceStack commandSourceStack, Entity entity, int sourceSlot, Collection<ServerPlayer> collection, @NonNull Identifier targetContainer, int targetSlot, Holder<LootItemFunction> holder) throws CommandSyntaxException {
        return setContainerItem(commandSourceStack, collection, targetContainer, targetSlot, ItemCommandsAccessor.callApplyModifier(commandSourceStack, holder, ItemCommandsAccessor.callGetItemInSlot(entity, sourceSlot)));
    }

    private static int containerToContainer(CommandSourceStack commandSourceStack, ServerPlayer player, @NonNull Identifier sourceContainer, int sourceSlot, Collection<ServerPlayer> collection, @NonNull Identifier targetContainer, int targetSlot) throws CommandSyntaxException {
        return setContainerItem(commandSourceStack, collection, targetContainer, targetSlot, getContainerItem(player, sourceContainer, sourceSlot));
    }

    private static int containerToContainer(CommandSourceStack commandSourceStack, ServerPlayer player, @NonNull Identifier sourceContainer, int sourceSlot, Collection<ServerPlayer> collection, @NonNull Identifier targetContainer, int targetSlot, Holder<LootItemFunction> holder) throws CommandSyntaxException {
        return setContainerItem(commandSourceStack, collection, targetContainer, targetSlot, ItemCommandsAccessor.callApplyModifier(commandSourceStack, holder, getContainerItem(player, sourceContainer, sourceSlot)));
    }

    private static int containerToBlock(CommandSourceStack commandSourceStack, ServerPlayer player, @NonNull Identifier sourceContainer, int sourceSlot, BlockPos blockPos, int targetSlot) throws CommandSyntaxException {
        return ItemCommandsAccessor.callSetBlockItem(commandSourceStack, blockPos, targetSlot, getContainerItem(player, sourceContainer, sourceSlot));
    }

    private static int containerToBlock(CommandSourceStack commandSourceStack, ServerPlayer player, @NonNull Identifier sourceContainer, int sourceSlot, BlockPos blockPos, int targetSlot, Holder<LootItemFunction> holder) throws CommandSyntaxException {
        return ItemCommandsAccessor.callSetBlockItem(commandSourceStack, blockPos, targetSlot, ItemCommandsAccessor.callApplyModifier(commandSourceStack, holder, getContainerItem(player, sourceContainer, sourceSlot)));
    }

    private static int containerToEntity(CommandSourceStack commandSourceStack, ServerPlayer player, @NonNull Identifier sourceContainer, int sourceSlot, Collection<? extends Entity> collection, int targetSlot) throws CommandSyntaxException {
        return ItemCommandsAccessor.callSetEntityItem(commandSourceStack, collection, targetSlot, getContainerItem(player, sourceContainer, sourceSlot));
    }

    private static int containerToEntity(CommandSourceStack commandSourceStack, ServerPlayer player, @NonNull Identifier sourceContainer, int sourceSlot, Collection<? extends Entity> collection, int targetSlot, Holder<LootItemFunction> holder) throws CommandSyntaxException {
        return ItemCommandsAccessor.callSetEntityItem(commandSourceStack, collection, targetSlot, ItemCommandsAccessor.callApplyModifier(commandSourceStack, holder, getContainerItem(player, sourceContainer, sourceSlot)));
    }

    private static ItemStack getContainerItem(ServerPlayer player, @NonNull Identifier containerLocation, int slot) throws CommandSyntaxException {
        Container container = ((NamespacedContainerHolder) player).declarative_ui$namespacedContainer(containerLocation);
        if (container == null) {
            throw new IllegalArgumentException("Container cannot be null.");
        }
        if (slot < 0 || slot >= container.getContainerSize()) {
            throw ItemCommandsAccessor.getERROR_SOURCE_INAPPLICABLE_SLOT().create(slot);
        } else {
            return container.getItem(slot).copy();
        }
    }

    private static int modifyContainerItem(CommandSourceStack commandSourceStack, Collection<ServerPlayer> collection, Identifier containerLocation, int slot, Holder<LootItemFunction> holder) throws CommandSyntaxException {
        Map<Entity, ItemStack> map = Maps.newHashMapWithExpectedSize(collection.size());

        for (ServerPlayer player : collection) {
            Container container = ((NamespacedContainerHolder) player).declarative_ui$namespacedContainer(containerLocation);
            if (container == null) continue;
            net.minecraft.world.item.ItemStack stack = container.getItem(slot);
            ItemStack itemStack = ItemCommandsAccessor.callApplyModifier(commandSourceStack, holder, stack.copy());
            container.setItem(slot, itemStack);
            map.put(player, itemStack);
            player.containerMenu.broadcastChanges();
        }

        if (map.isEmpty()) {
            throw ItemCommandsAccessor.getERROR_TARGET_NO_CHANGES().create(slot);
        } else {
            if (map.size() == 1) {
                var entry = map.entrySet().iterator().next();
                commandSourceStack.sendSuccess(() -> Component.translatable("commands.item.entity.set.success.single", entry.getKey().getDisplayName(), entry.getValue().getDisplayName()), true);
            } else {
                commandSourceStack.sendSuccess(() -> Component.translatable("commands.item.entity.set.success.multiple", map.size()), true);
            }

            return map.size();
        }
    }

    private static int setContainerItem(CommandSourceStack commandSourceStack, Collection<ServerPlayer> collection, @NonNull Identifier containerLocation, int slot, ItemStack itemStack) throws CommandSyntaxException {
        List<ServerPlayer> list = Lists.newArrayListWithCapacity(collection.size());

        for (ServerPlayer player : collection) {
            Container container = ((NamespacedContainerHolder) player).declarative_ui$namespacedContainer(containerLocation);
            if (container == null || slot < 0 || slot >= container.getContainerSize()) continue;
            ItemStack copy = itemStack.copy();
            container.setItem(slot, copy);
            list.add(player);
            player.containerMenu.broadcastChanges();
        }

        if (list.isEmpty()) {
            throw ItemCommandsAccessor.getERROR_TARGET_NO_CHANGES_KNOWN_ITEM().create(itemStack.getDisplayName(), slot);
        } else {
            if (list.size() == 1) {
                commandSourceStack.sendSuccess(() -> Component.translatable("commands.item.entity.set.success.single", list.getFirst().getDisplayName(), itemStack.getDisplayName()), true);
            } else {
                commandSourceStack.sendSuccess(() -> Component.translatable("commands.item.entity.set.success.multiple", list.size()), true);
            }

            return list.size();
        }
    }
}
