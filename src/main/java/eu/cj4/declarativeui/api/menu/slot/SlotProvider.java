package eu.cj4.declarativeui.api.menu.slot;

import com.mojang.serialization.MapCodec;
import eu.cj4.declarativeui.impl.menu.slot.provider.AnimatedProvider;
import eu.cj4.declarativeui.impl.menu.slot.provider.EmptyProvider;
import eu.cj4.declarativeui.impl.menu.slot.provider.SimpleProvider;
import eu.cj4.declarativeui.impl.menu.slot.provider.TagProvider;
import eu.cj4.declarativeui.impl.registry.DeclarativeUIBuiltInRegistries;
import eu.pb4.sgui.api.elements.GuiElementInterface;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.core.Registry;
import net.minecraft.resources.Identifier;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.storage.loot.functions.LootItemFunction;
import org.jspecify.annotations.NonNull;

import java.util.List;
import java.util.Optional;

public interface SlotProvider {
    MapCodec<? extends SlotProvider> codec();

    GuiElementInterface createElement(CommandSourceStack source, GuiElementInterface.ClickCallback clickCallback);

    static @NonNull EmptyProvider empty() {
        return EmptyProvider.INSTANCE;
    }

    static @NonNull SimpleProvider simple(ItemStack stack) {
        return new SimpleProvider(stack, Optional.empty());
    }

    static @NonNull SimpleProvider simple(ItemStack stack, LootItemFunction lootItemFunction) {
        return new SimpleProvider(stack, Optional.of(lootItemFunction));
    }

    static @NonNull TagProvider tag(TagKey<Item> tag, int interval, boolean random) {
        return new TagProvider(tag, interval, random);
    }

    static @NonNull AnimatedProvider animated(List<SimpleProvider> stack, int interval, boolean random) {
        return new AnimatedProvider(stack, interval, random);
    }

    static <T extends SlotProvider> MapCodec<T> register(Identifier id, MapCodec<T> mapCodec) {
        return Registry.register(DeclarativeUIBuiltInRegistries.SLOT_PROVIDER_TYPE, id, mapCodec);
    }
}
