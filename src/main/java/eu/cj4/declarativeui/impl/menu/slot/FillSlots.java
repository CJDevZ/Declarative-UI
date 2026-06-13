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
import net.minecraft.util.InclusiveRange;
import net.minecraft.world.level.storage.loot.LootContext;
import org.jspecify.annotations.NonNull;
import org.jspecify.annotations.Nullable;

import java.util.Collections;
import java.util.List;
import java.util.function.Supplier;

public record FillSlots(InclusiveRange<Integer> range, SlotProvider provider, List<ClickEvent> clickEvents) implements Slot {
    public static final MapCodec<FillSlots> MAP_CODEC = RecordCodecBuilder.mapCodec(instance ->
            instance.group(
                    InclusiveRange.INT.fieldOf("range").forGetter(FillSlots::range),
                    SlotProviders.TYPED_CODEC.fieldOf("provider").forGetter(FillSlots::provider),
                    ClickEvent.LIST_CODEC.optionalFieldOf("click_event", Collections.emptyList()).forGetter(FillSlots::clickEvents)
            ).apply(instance, FillSlots::new));

    @Override
    public MapCodec<FillSlots> codec() {
        return MAP_CODEC;
    }

    @Override
    public void build(Menu menu, CommandSourceStack sourceStack, SlotBasedGui gui, LootContext lootContext) {
        int max = Math.min(this.range.maxInclusive(), gui.getSize() - 1);
        for (int slot = Math.max(this.range.minInclusive(), 0); slot <= max; slot++) {
            gui.setSlot(slot, this.createElement(sourceStack, this.clickCallback(menu)));
        }
    }

    public GuiElement createElement(CommandSourceStack source, GuiElement.ClickCallback clickCallback) {
        return this.provider.createElement(source, clickCallback);
    }

    public GuiElement.ClickCallback clickCallback(@NonNull Menu declaredMenu) {
        return this.clickCallback(declaredMenu, null);
    }

    public GuiElement.ClickCallback clickCallback(@NonNull Menu declaredMenu, @Nullable Supplier<CompoundTag> compoundTagFunction) {
        return this.clickEvents.isEmpty() ? GuiElement.EMPTY_CALLBACK : new Callback(declaredMenu, this.clickEvents, compoundTagFunction);
    }
}
