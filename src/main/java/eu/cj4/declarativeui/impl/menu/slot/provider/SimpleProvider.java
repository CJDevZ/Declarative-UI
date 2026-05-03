package eu.cj4.declarativeui.impl.menu.slot.provider;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import eu.cj4.declarativeui.api.menu.slot.provider.SlotProvider;
import eu.cj4.declarativeui.api.menu.slot.provider.SlotProviderType;
import eu.pb4.sgui.api.elements.GuiElement;
import eu.pb4.sgui.api.elements.GuiElementInterface;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
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
import org.jspecify.annotations.NonNull;

import java.util.Optional;

public record SimpleProvider(ItemStack item, Optional<LootItemFunction> itemModifier) implements SlotProvider {
    public static final MapCodec<SimpleProvider> CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
            Codec.withAlternative(ItemStack.STRICT_CODEC, Item.CODEC, ItemStack::new).optionalFieldOf("item", ItemStack.EMPTY).forGetter(SimpleProvider::item),
            Codec.withAlternative(LootItemFunctions.ROOT_CODEC, ResourceKey.codec(Registries.ITEM_MODIFIER), resourceKey -> FunctionReference.functionReference(resourceKey).build()).optionalFieldOf("item_modifier").forGetter(SimpleProvider::itemModifier)
    ).apply(instance, SimpleProvider::new));

    @Override
    public SlotProviderType getType() {
        return SlotProviders.SIMPLE;
    }

    public static SimpleProvider fromHolder(Holder<Item> holder) {
        return new SimpleProvider(new ItemStack(holder), Optional.empty());
    }

    public @NonNull ItemStack createStack(CommandSourceStack source) {
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

    @Override
    public GuiElementInterface createElement(CommandSourceStack source, GuiElementInterface.ClickCallback clickCallback) {
        return new GuiElement(createStack(source), clickCallback);
    }
}
