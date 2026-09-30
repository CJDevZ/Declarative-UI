package eu.cj4.declarativeui.impl.command.item;

import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.serialization.MapCodec;
import eu.cj4.declarativeui.api.container.NamespacedContainerHolder;
import eu.cj4.declarativeui.mixin.ItemCommandsAccessor;
import it.unimi.dsi.fastutil.ints.IntArrayList;
import net.minecraft.advancements.predicates.MinMaxBounds;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.commands.arguments.EntityArgument;
import net.minecraft.commands.arguments.IdentifierArgument;
import net.minecraft.commands.arguments.RangeArgument;
import net.minecraft.resources.Identifier;
import net.minecraft.server.commands.ArgProvider;
import net.minecraft.server.commands.CommandResponseTracker;
import net.minecraft.server.commands.item.ItemAccessor;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.Container;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.slot.SlotCollection;
import net.minecraft.world.item.slot.SlotSource;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;
import org.jspecify.annotations.NonNull;
import org.jspecify.annotations.Nullable;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.IntStream;

public class ContainerItemAccessor implements ItemAccessor<Container> {
    public static final ArgProvider.Factory<ItemAccessor<?>> PROVIDER = (arg) -> ArgProvider.create("container", () -> Commands.argument(arg, EntityArgument.players()).then(Commands.argument(arg + "Id", IdentifierArgument.id())), (c) -> new ContainerItemAccessor(EntityArgument.getPlayers(c, arg), IdentifierArgument.getId(c, arg + "Id")));
    private final Collection<ServerPlayer> players;
    private final Identifier containerId;

    public ContainerItemAccessor(Collection<ServerPlayer> players, Identifier container) {
        this.players = players;
        this.containerId = container;
    }

    public static SlotSource createSlotSource(Identifier sourceContainer, MinMaxBounds.Ints slotRange) {
        return new SlotSource() {
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
                return container.getSlotsFromRange(IntArrayList.toList(IntStream.rangeClosed(slotRange.min().orElse(0), slotRange.max().orElse(container.getContainerSize()))));
            }
        };
    }

    @Override
    public void setItems(@NonNull CommandSourceStack source, @NonNull SlotSource slotSource, @NonNull SetterFunction<Container> function) throws CommandSyntaxException {
        for(ServerPlayer player : this.players) {
            SlotCollection targetSlots = ItemCommandsAccessor.callGetSlotsFromProvider(source, player, slotSource);
            int updatedSlots = function.apply(NamespacedContainerHolder.of(player).declarative_ui$namespacedContainer(this.containerId), targetSlots);
        }
    }

    @Override
    public @NonNull SlotCollection getSlots(@NonNull CommandSourceStack source, @NonNull SlotSource slotSource) throws CommandSyntaxException {
        List<SlotCollection> slotCollections = new ArrayList<>();

        for(ServerPlayer player : this.players) {
            SlotCollection slots = ItemCommandsAccessor.callGetSlotsFromProvider(source, player, slotSource);
            slotCollections.add(slots);
        }

        return SlotCollection.concat(slotCollections);
    }

    @Override
    public int getReplaceSuccess(@NonNull CommandSourceStack source, @NonNull CommandResponseTracker<Container> tracker, @Nullable ItemStack knownItem) throws CommandSyntaxException {
        return 0;
    }

    @Override
    public int getModifySuccess(@NonNull CommandSourceStack source, @NonNull CommandResponseTracker<Container> tracker) throws CommandSyntaxException {
        return 0;
    }
}
