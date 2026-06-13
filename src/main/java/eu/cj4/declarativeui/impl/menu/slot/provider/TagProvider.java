package eu.cj4.declarativeui.impl.menu.slot.provider;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import eu.cj4.declarativeui.api.menu.slot.SlotProvider;
import eu.pb4.sgui.api.elements.AnimatedGuiElement;
import eu.pb4.sgui.api.elements.GuiElementInterface;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

import java.util.ArrayList;
import java.util.List;

public record TagProvider(TagKey<Item> tag, int interval, boolean random) implements SlotProvider {
    public static final MapCodec<TagProvider> MAP_CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
            TagKey.codec(Registries.ITEM).fieldOf("tag").forGetter(TagProvider::tag),
            Codec.INT.fieldOf("interval").forGetter(TagProvider::interval),
            Codec.BOOL.optionalFieldOf("random", false).forGetter(TagProvider::random)
    ).apply(instance, TagProvider::new));

    @Override
    public MapCodec<TagProvider> codec() {
        return MAP_CODEC;
    }

    @Override
    public GuiElementInterface createElement(CommandSourceStack source, GuiElementInterface.ClickCallback clickCallback) {
        List<ItemStack> itemStacks = new ArrayList<>();
        for (Holder<Item> itemHolder : BuiltInRegistries.ITEM.getTagOrEmpty(tag)) {
            itemStacks.add(new ItemStack(itemHolder));
        }
        return new AnimatedGuiElement(itemStacks.toArray(ItemStack[]::new), this.interval, this.random, clickCallback);
    }
}
