package eu.cj4.declarativeui.impl.menu.gui;

import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.logging.LogUtils;
import eu.cj4.declarativeui.api.menu.Menu;
import eu.cj4.declarativeui.api.menu.slot.action.ClickAction;
import eu.cj4.declarativeui.impl.menu.slot.ClickEvent;
import eu.cj4.declarativeui.impl.menu.slot.DeclaredSlot;
import eu.pb4.sgui.api.elements.GuiElement;
import net.minecraft.core.component.DataComponents;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import org.jspecify.annotations.NonNull;
import org.jspecify.annotations.Nullable;
import org.slf4j.Logger;

import java.util.List;

public class SearchGui extends eu.pb4.sgui.api.gui.AnvilInputGui {
    private static final Logger LOGGER = LogUtils.getLogger();

    private final Menu menu;
    private final String searchTag;
    private final List<ClickEvent> searchActions;
    private final List<ClickAction> closeActions;

    /**
     * Constructs a new input gui for the provided player.
     *
     * @param player                the player to serve this gui to
     * @param manipulatePlayerSlots if <code>true</code> the players inventory
     *                              will be treated as slots of this gui
     */
    public SearchGui(ServerPlayer player, boolean manipulatePlayerSlots, @NonNull Menu menu, String searchTag, @Nullable List<ClickEvent> searchActions, @Nullable List<ClickAction> closeActions) {
        super(player, manipulatePlayerSlots);
        this.menu = menu;
        this.searchTag = searchTag;
        this.searchActions = searchActions;
        this.closeActions = closeActions;
        this.setDefaultInputValue("");
    }

    @Override
    public void setDefaultInputValue(String input) {
        super.setDefaultInputValue(input);
        GuiElement.ClickCallback searchCallback = this.searchActions == null ? GuiElement.EMPTY_CALLBACK : new DeclaredSlot.SlotCallback(this.menu, this.searchActions, this::getSearchTag);
        ItemStack searchItem = Items.PAPER.getDefaultInstance();
        searchItem.set(DataComponents.CUSTOM_NAME, Component.literal("Search..."));
        this.setSlot(2, searchItem, searchCallback);
    }

    @Override
    public void onManualClose() {
        if (this.closeActions != null) {
            for (ClickAction closeAction : this.closeActions) {
                try {
                    closeAction.click(this.menu, this);
                } catch (CommandSyntaxException e) {
                    LOGGER.warn("Failed to process Close", e);
                }
            }
        }
    }

    private CompoundTag getSearchTag() {
        CompoundTag compoundTag = new CompoundTag();
        compoundTag.putString(this.searchTag, this.getInput());
        return compoundTag;
    }
}
