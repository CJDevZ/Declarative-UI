package eu.cj4.declarativeui.api.menu.slot;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.LootParams;
import net.minecraft.world.level.storage.loot.functions.FunctionReference;
import net.minecraft.world.level.storage.loot.functions.LootItemFunction;
import net.minecraft.world.level.storage.loot.functions.LootItemFunctions;
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSets;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

/// TODO: Add Click Type Predicates and Saved/maybe Temporary Inventories
public record DeclaredSlot(int slot, ItemStack item, Optional<LootItemFunction> itemModifier, List<ClickEvent> clickEvent) {
    public static final Codec<DeclaredSlot> CODEC = RecordCodecBuilder.create(instance ->
            instance.group(
                    Codec.INT.fieldOf("slot").forGetter(DeclaredSlot::slot),
                    Codec.withAlternative(ItemStack.STRICT_CODEC, Item.CODEC, ItemStack::new).optionalFieldOf("item", ItemStack.EMPTY).forGetter(DeclaredSlot::item),
                    Codec.withAlternative(LootItemFunctions.ROOT_CODEC, FunctionReference.CODEC.codec()).optionalFieldOf("item_modifier").forGetter(DeclaredSlot::itemModifier),
                    Codec.withAlternative(Codec.list(ClickEvent.CODEC), ClickEvent.CODEC, Collections::singletonList).optionalFieldOf("click_event", Collections.emptyList()).forGetter(DeclaredSlot::clickEvent)
            ).apply(instance, DeclaredSlot::new));

    public ItemStack getItemStack(CommandSourceStack source) {
        if (itemModifier.isPresent()) {
            ServerLevel serverLevel = source.getLevel();
            LootParams lootParams = (new LootParams.Builder(serverLevel)).withParameter(LootContextParams.ORIGIN, source.getPosition()).withOptionalParameter(LootContextParams.THIS_ENTITY, source.getEntity()).create(LootContextParamSets.COMMAND);
            LootContext lootContext = (new LootContext.Builder(lootParams)).create(Optional.empty());
            LootItemFunction lootItemFunction = this.itemModifier.get();
            lootContext.pushVisitedElement(LootContext.createVisitedEntry(lootItemFunction));
            ItemStack itemStack2 = lootItemFunction.apply(this.item.copy(), lootContext);
            itemStack2.limitSize(itemStack2.getMaxStackSize());
            return itemStack2;
        } else {
            return this.item.copy();
        }
    }
}
