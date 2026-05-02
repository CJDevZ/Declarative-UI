package eu.cj4.declarativeui.impl.menu.slot;

import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.logging.LogUtils;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import eu.cj4.declarativeui.api.menu.Menu;
import eu.cj4.declarativeui.api.menu.slot.provider.SlotProvider;
import eu.cj4.declarativeui.impl.menu.SimpleMenu;
import eu.cj4.declarativeui.impl.menu.slot.provider.SlotProviders;
import eu.pb4.sgui.api.ClickType;
import eu.pb4.sgui.api.elements.GuiElementInterface;
import eu.pb4.sgui.api.gui.SlotGuiInterface;
import net.minecraft.commands.CommandSourceStack;
import org.slf4j.Logger;

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

    public GuiElementInterface.ClickCallback clickCallback(SimpleMenu declaredMenu) {
        return clickEvents.isEmpty() ? GuiElementInterface.EMPTY_CALLBACK : new SlotCallback(declaredMenu, clickEvents);
    }

    protected record SlotCallback(SimpleMenu declaredMenu, List<ClickEvent> clickEvents) implements GuiElementInterface.ClickCallback {
        private static final Logger LOGGER = LogUtils.getLogger();

        @Override
        public void click(int i, ClickType clickType, net.minecraft.world.inventory.ClickType actionType, SlotGuiInterface slotGuiInterface) {
            for (ClickEvent clickEvent : this.clickEvents) {
                Optional<ClickType> clickType2 = clickEvent.clickType();
                Optional<net.minecraft.world.inventory.ClickType> actionType2 = clickEvent.actionType();
                if ((clickType2.isEmpty() || clickType2.get() == clickType) && (actionType2.isEmpty() || actionType2.get() == actionType)) {
                    try {
                        clickEvent.click(this.declaredMenu, slotGuiInterface);
                    } catch (CommandSyntaxException e) {
                        LOGGER.warn("Failed to process Click", e);
                    }
                }
            }
        }
    }
}
