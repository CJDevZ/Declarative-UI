package eu.cj4.declarativeui.api.menu.slot;

import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.logging.LogUtils;
import com.mojang.serialization.MapCodec;
import eu.cj4.declarativeui.api.container.ContainerProvider;
import eu.cj4.declarativeui.api.menu.Menu;
import eu.cj4.declarativeui.impl.menu.slot.*;
import eu.cj4.declarativeui.impl.registry.DeclarativeUIBuiltInRegistries;
import eu.pb4.sgui.api.ClickType;
import eu.pb4.sgui.api.elements.GuiElement;
import eu.pb4.sgui.api.gui.SlotBasedGui;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.core.Registry;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.Identifier;
import net.minecraft.util.InclusiveRange;
import net.minecraft.world.inventory.ContainerInput;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.providers.number.NumberProvider;
import org.jspecify.annotations.NonNull;
import org.jspecify.annotations.Nullable;
import org.slf4j.Logger;

import java.util.List;
import java.util.Optional;
import java.util.function.Supplier;

public interface Slot {
    MapCodec<? extends Slot> codec();

    void build(Menu menu, CommandSourceStack sourceStack, SlotBasedGui gui, LootContext lootContext);

    static SimpleSlot simple(NumberProvider slot, SlotProvider provider, List<ClickEvent> clickEvents) {
        return new SimpleSlot(slot, provider, clickEvents);
    }

    static RedirectSlots redirect(ContainerProvider provider, List<DeclaredRedirect> redirects, boolean viewOnly) {
        return new RedirectSlots(provider, redirects, viewOnly);
    }

    static FillSlots fill(InclusiveRange<Integer> range, SlotProvider provider, List<ClickEvent> clickEvents) {
        return new FillSlots(range, provider, clickEvents);
    }

    static <T extends Slot> MapCodec<T> register(Identifier id, MapCodec<T> codec) {
        return Registry.register(DeclarativeUIBuiltInRegistries.SLOT_TYPE, id, codec);
    }

    record Callback(@NonNull Menu declaredMenu, @NonNull List<ClickEvent> clickEvents, @Nullable Supplier<CompoundTag> compoundTagFunction) implements GuiElement.ClickCallback {
        private static final Logger LOGGER = LogUtils.getLogger();

        @Override
        public void click(int index, ClickType clickType, ContainerInput containerInput, SlotBasedGui gui) {
            for (ClickEvent clickEvent : this.clickEvents) {
                Optional<ClickType> clickType2 = clickEvent.clickType();
                Optional<ContainerInput> containerInput1 = clickEvent.actionType();
                if ((clickType2.isEmpty() || clickType2.get() == clickType) && (containerInput1.isEmpty() || containerInput1.get() == containerInput)) {
                    CompoundTag compoundTag = compoundTagFunction == null ? null : compoundTagFunction.get();
                    try {
                        clickEvent.click(this.declaredMenu, gui, compoundTag);
                    } catch (CommandSyntaxException e) {
                        LOGGER.warn("Failed to process Click", e);
                    }
                }
            }
        }
    }
}
