package eu.cj4.declarativeui.impl.container;

import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.exceptions.DynamicCommandExceptionType;
import com.mojang.serialization.Codec;
import eu.cj4.declarativeui.api.container.NamespacedContainerHolder;
import eu.cj4.declarativeui.impl.registry.DeclarativeUIRegistries;
import eu.cj4.declarativeui.mixin.ResourceKeyArgumentAccessor;
import net.minecraft.advancements.criterion.MinMaxBounds;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.arguments.EntityArgument;
import net.minecraft.commands.arguments.RangeArgument;
import net.minecraft.commands.arguments.item.ItemPredicateArgument;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.util.ExtraCodecs;
import net.minecraft.world.Container;
import net.minecraft.world.item.ItemStack;

public record DeclaredContainer(int size) {
    public static final Codec<DeclaredContainer> CODEC = ExtraCodecs.POSITIVE_INT.fieldOf("size").codec().xmap(DeclaredContainer::new, DeclaredContainer::size);
    public static final DynamicCommandExceptionType ERROR_INVALID_CONTAINER = new DynamicCommandExceptionType((object) -> Component.literal(String.format("Unknown container: %s", object)));

    public PlayerContainer createContainer() {
        return new PlayerContainer(this.size);
    }

    public static int countItems(CommandContext<CommandSourceStack> commandContext) throws CommandSyntaxException {
        ServerPlayer serverPlayer = EntityArgument.getPlayer(commandContext, "player");
        Identifier containerId = ResourceKeyArgumentAccessor.callResolveKey(commandContext, "container", DeclarativeUIRegistries.CONTAINER, ERROR_INVALID_CONTAINER).key().identifier();
        Container container = NamespacedContainerHolder.of(serverPlayer).declarative_ui$namespacedContainer(containerId);
        if (container == null) {
            return 0;
        }
        MinMaxBounds.Ints range = RangeArgument.Ints.getRange(commandContext, "range");
        ItemPredicateArgument.Result itemPredicate = ItemPredicateArgument.getItemPredicate(commandContext, "item_predicate");

        int maxIndex = container.getContainerSize() - 1;
        int max = Math.min(range.max().orElse(maxIndex), maxIndex);
        int match = 0;
        for (int slot = Math.max(range.min().orElse(0), 0); slot <= max; slot++) {
            ItemStack itemStack = container.getItem(slot);
            if (itemPredicate.test(itemStack)) {
                match += itemStack.getCount();
            }
        }
        return match;
    }
}
