package eu.cj4.declarativeui.api.menu.slot;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import eu.cj4.declarativeui.api.menu.slot.provider.SlotProvider;
import eu.cj4.declarativeui.impl.providers.SlotProviders;
import eu.pb4.sgui.api.elements.GuiElementInterface;
import net.minecraft.commands.CommandSourceStack;

import java.util.Collections;
import java.util.List;

/// TODO: Add Click Type Predicates and Saved/maybe Temporary Inventories
public record DeclaredSlot(int slot, SlotProvider provider, List<ClickEvent> clickEvents) {
    public static final Codec<DeclaredSlot> CODEC = RecordCodecBuilder.create(instance ->
            instance.group(
                    Codec.INT.fieldOf("slot").forGetter(DeclaredSlot::slot),
                    SlotProviders.TYPED_CODEC.fieldOf("provider").forGetter(DeclaredSlot::provider),
                    Codec.withAlternative(Codec.list(ClickEvent.CODEC), ClickEvent.CODEC, Collections::singletonList).optionalFieldOf("click_event", Collections.emptyList()).forGetter(DeclaredSlot::clickEvents)
            ).apply(instance, DeclaredSlot::new));

    public GuiElementInterface createElement(CommandSourceStack source, GuiElementInterface.ClickCallback clickCallback) {
        return this.provider.createElement(source, clickCallback);
    }
}
