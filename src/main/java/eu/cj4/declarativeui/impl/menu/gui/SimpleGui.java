package eu.cj4.declarativeui.impl.menu.gui;

import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.logging.LogUtils;
import eu.cj4.declarativeui.api.menu.Menu;
import eu.cj4.declarativeui.api.menu.slot.ClickAction;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.inventory.MenuType;
import org.slf4j.Logger;

import java.util.List;

public class SimpleGui extends eu.pb4.sgui.api.gui.SimpleGui {
    private static final Logger LOGGER = LogUtils.getLogger();

    private final Menu menu;
    private final List<ClickAction> closeActions;

    /**
     * Constructs a new simple container gui for the supplied player.
     *
     * @param type                  the screen handler that the client should display
     * @param player                the player to server this gui to
     * @param manipulatePlayerSlots if <code>true</code> the players inventory
     *                              will be treated as slots of this gui
     */
    public SimpleGui(Menu menu, MenuType<?> type, ServerPlayer player, boolean manipulatePlayerSlots, List<ClickAction> closeActions) {
        super(type, player, manipulatePlayerSlots);
        this.menu = menu;
        this.closeActions = closeActions;
    }

    @Override
    public void onManualClose() {
        if (this.closeActions != null) {
            for (ClickAction closeAction : this.closeActions) {
                try {
                    closeAction.click(menu, this);
                } catch (CommandSyntaxException e) {
                    LOGGER.warn("Failed to process Close", e);
                }
            }
        }
    }
}
