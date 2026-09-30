package eu.cj4.declarativeui.impl.menu.slot.provider;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import eu.cj4.declarativeui.api.menu.slot.SlotProvider;
import eu.pb4.sgui.api.elements.GuiElement;
import eu.pb4.sgui.api.elements.SimpleGuiElement;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.core.Holder;
import net.minecraft.core.component.DataComponentPatch;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ItemStackTemplate;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.LootParams;
import net.minecraft.world.level.storage.loot.functions.LootItemFunction;
import net.minecraft.world.level.storage.loot.functions.LootItemFunctions;
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSets;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;
import org.jspecify.annotations.NonNull;

import java.util.Optional;

public record SimpleProvider(Optional<ItemStackTemplate> template, Optional<Holder<LootItemFunction>> modifier) implements SlotProvider {
    public static final MapCodec<SimpleProvider> MAP_CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
            ItemStackTemplate.CODEC.optionalFieldOf("item").forGetter(SimpleProvider::template),
            LootItemFunctions.CODEC.optionalFieldOf("modifier").forGetter(SimpleProvider::modifier)
    ).apply(instance, SimpleProvider::new));

    @Override
    public MapCodec<SimpleProvider> codec() {
        return MAP_CODEC;
    }

    public static SimpleProvider fromHolder(Holder<Item> holder) {
        return new SimpleProvider(Optional.of(new ItemStackTemplate(holder, 1, DataComponentPatch.EMPTY)), Optional.empty());
    }

    public @NonNull ItemStack createStack(CommandSourceStack source) {
        if (this.template.isEmpty()) {
            return ItemStack.EMPTY;
        }
        if (modifier.isPresent()) {
            ServerLevel serverLevel = source.getLevel();
            LootParams lootParams = (new LootParams.Builder(serverLevel)).withParameter(LootContextParams.ORIGIN, source.getPosition()).withOptionalParameter(LootContextParams.THIS_ENTITY, source.getEntity()).create(LootContextParamSets.COMMAND);
            LootContext lootContext = (new LootContext.Builder(lootParams)).create(Optional.empty());
            LootItemFunction lootItemFunction = this.modifier.get().value();
            lootContext.pushVisitedElement(LootContext.createVisitedEntry(lootItemFunction));
            ItemStack itemStack2 = lootItemFunction.apply(this.template.get().create(), lootContext);
            itemStack2.limitSize(itemStack2.getMaxStackSize());
            return itemStack2;
        } else return this.template.get().create();
    }

    @Override
    public GuiElement createElement(CommandSourceStack source, GuiElement.ClickCallback clickCallback) {
        return new SimpleGuiElement(createStack(source), clickCallback);
    }
}
