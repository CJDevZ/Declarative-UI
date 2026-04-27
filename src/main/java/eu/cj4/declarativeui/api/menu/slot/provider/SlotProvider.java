package eu.cj4.declarativeui.api.menu.slot.provider;

import eu.pb4.sgui.api.elements.GuiElement;
import eu.pb4.sgui.api.elements.GuiElementInterface;
import net.minecraft.commands.CommandSourceStack;

public interface SlotProvider {
    SlotProviderType getType();

    GuiElementInterface createElement(CommandSourceStack source, GuiElementInterface.ClickCallback clickCallback);
}
