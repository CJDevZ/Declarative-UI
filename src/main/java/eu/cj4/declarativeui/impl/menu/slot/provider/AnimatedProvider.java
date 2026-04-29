package eu.cj4.declarativeui.impl.menu.slot.provider;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import eu.cj4.declarativeui.api.menu.slot.provider.SlotProvider;
import eu.cj4.declarativeui.api.menu.slot.provider.SlotProviderType;
import eu.pb4.sgui.api.elements.AnimatedGuiElement;
import eu.pb4.sgui.api.elements.GuiElementInterface;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

import java.util.List;

public record AnimatedProvider(List<SimpleProvider> providers, int interval, boolean random) implements SlotProvider {
    public static final MapCodec<AnimatedProvider> CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
            Codec.list(Codec.withAlternative(SimpleProvider.CODEC.codec(), Item.CODEC, SimpleProvider::fromHolder)).fieldOf("providers").forGetter(AnimatedProvider::providers),
            Codec.INT.fieldOf("interval").forGetter(AnimatedProvider::interval),
            Codec.BOOL.optionalFieldOf("random", false).forGetter(AnimatedProvider::random)
    ).apply(instance, AnimatedProvider::new));

    @Override
    public SlotProviderType getType() {
        return SlotProviders.SIMPLE;
    }

    @Override
    public GuiElementInterface createElement(CommandSourceStack source, GuiElementInterface.ClickCallback clickCallback) {
        ItemStack[] itemStacks = new ItemStack[this.providers.size()];
        for (int i = 0; i < this.providers.size(); i++) {
            itemStacks[i] = this.providers.get(i).createStack(source);
        }
        return new AnimatedGuiElement(itemStacks, this.interval, this.random, clickCallback);
    }
}
