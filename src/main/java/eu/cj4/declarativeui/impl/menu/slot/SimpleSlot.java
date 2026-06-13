package eu.cj4.declarativeui.impl.menu.slot;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import eu.cj4.declarativeui.api.menu.Menu;
import eu.cj4.declarativeui.api.menu.slot.Slot;
import eu.cj4.declarativeui.api.menu.slot.SlotProvider;
import eu.cj4.declarativeui.impl.menu.slot.provider.SlotProviders;
import eu.pb4.sgui.api.elements.GuiElement;
import eu.pb4.sgui.api.gui.SlotBasedGui;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.providers.number.NumberProvider;
import net.minecraft.world.level.storage.loot.providers.number.NumberProviders;
import org.jspecify.annotations.NonNull;
import org.jspecify.annotations.Nullable;

import java.util.Collections;
import java.util.List;
import java.util.function.Supplier;

/// TODO: Add Click Type Predicates and Saved/maybe Temporary Inventories
public record SimpleSlot(NumberProvider slot, SlotProvider provider, List<ClickEvent> clickEvents) implements Slot {
    public static final MapCodec<SimpleSlot> MAP_CODEC = RecordCodecBuilder.mapCodec(instance ->
            instance.group(
                    NumberProviders.CODEC.fieldOf("slot").forGetter(SimpleSlot::slot),
                    SlotProviders.TYPED_CODEC.fieldOf("provider").forGetter(SimpleSlot::provider),
                    ClickEvent.LIST_CODEC.optionalFieldOf("click_event", Collections.emptyList()).forGetter(SimpleSlot::clickEvents)
            ).apply(instance, SimpleSlot::new));

    public GuiElement createElement(CommandSourceStack source, GuiElement.ClickCallback clickCallback) {
        return this.provider.createElement(source, clickCallback);
    }

    public GuiElement.ClickCallback clickCallback(@NonNull Menu declaredMenu) {
        return this.clickCallback(declaredMenu, null);
    }

    public GuiElement.ClickCallback clickCallback(@NonNull Menu declaredMenu, @Nullable Supplier<CompoundTag> compoundTagFunction) {
        return this.clickEvents.isEmpty() ? GuiElement.EMPTY_CALLBACK : new Callback(declaredMenu, this.clickEvents, compoundTagFunction);
    }

    @Override
    public MapCodec<SimpleSlot> codec() {
        return MAP_CODEC;
    }

    @Override
    public void build(Menu menu, CommandSourceStack sourceStack, SlotBasedGui gui, LootContext lootContext) {
        int size = gui.getSize();
        int slot = this.slot.getInt(lootContext);
        if (size >= 0 && slot < size) {
            gui.setSlot(slot, this.createElement(sourceStack, this.clickCallback(menu)));
        }
    }
}
