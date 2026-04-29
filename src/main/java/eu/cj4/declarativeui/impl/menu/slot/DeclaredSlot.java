package eu.cj4.declarativeui.impl.menu.slot;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import eu.cj4.declarativeui.impl.menu.DeclaredMenu;
import eu.cj4.declarativeui.api.menu.slot.provider.SlotProvider;
import eu.cj4.declarativeui.impl.menu.slot.provider.SlotProviders;
import eu.pb4.sgui.api.ClickType;
import eu.pb4.sgui.api.elements.GuiElementInterface;
import eu.pb4.sgui.api.gui.SlotGuiInterface;
import net.minecraft.commands.CommandSourceStack;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

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

    public GuiElementInterface.ClickCallback clickCallback(DeclaredMenu declaredMenu) {
        return clickEvents.isEmpty() ? GuiElementInterface.EMPTY_CALLBACK : new SlotCallback(declaredMenu, clickEvents);
    }

    protected record SlotCallback(DeclaredMenu declaredMenu, List<ClickEvent> clickEvents) implements GuiElementInterface.ClickCallback {

        @Override
        public void click(int i, ClickType clickType, net.minecraft.world.inventory.ClickType actionType, SlotGuiInterface slotGuiInterface) {
            for (ClickEvent clickEvent : this.clickEvents) {
                Optional<ClickType> clickType2 = clickEvent.clickType();
                Optional<net.minecraft.world.inventory.ClickType> actionType2 = clickEvent.actionType();
                if ((clickType2.isEmpty() || clickType2.get() == clickType) && (actionType2.isEmpty() || actionType2.get() == actionType)) {
                    clickEvent.click(this.declaredMenu, slotGuiInterface);
                }
            }
        }
    }
}
