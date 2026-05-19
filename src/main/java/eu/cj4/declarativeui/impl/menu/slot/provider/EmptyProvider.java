package eu.cj4.declarativeui.impl.menu.slot.provider;

import com.mojang.serialization.MapCodec;
import eu.cj4.declarativeui.api.menu.slot.provider.SlotProvider;
import eu.cj4.declarativeui.api.menu.slot.provider.SlotProviderType;
import eu.pb4.sgui.api.elements.GuiElement;
import eu.pb4.sgui.api.elements.GuiElementInterface;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.world.item.ItemStack;

public record EmptyProvider() implements SlotProvider {
    public static final EmptyProvider INSTANCE = new EmptyProvider();
    public static final MapCodec<EmptyProvider> CODEC = MapCodec.unit(INSTANCE);

    @Override
    public SlotProviderType getType() {
        return SlotProviders.EMPTY;
    }

    @Override
    public GuiElementInterface createElement(CommandSourceStack source, GuiElementInterface.ClickCallback clickCallback) {
        return new GuiElement(ItemStack.EMPTY, clickCallback);
    }
}
