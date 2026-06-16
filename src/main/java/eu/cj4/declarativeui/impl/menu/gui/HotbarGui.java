package eu.cj4.declarativeui.impl.menu.gui;

import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.logging.LogUtils;
import eu.cj4.declarativeui.api.menu.Menu;
import eu.cj4.declarativeui.api.menu.slot.ClickAction;
import net.minecraft.server.level.ServerPlayer;
import org.slf4j.Logger;

import java.util.List;

public class HotbarGui extends eu.pb4.sgui.api.gui.HotbarGui {
    private static final Logger LOGGER = LogUtils.getLogger();

    private final Menu menu;
    private final List<ClickAction> closeActions;

    /**
     * Constructs a new hotbar gui for the provided player.
     *
     * @param player                the player to serve this gui to
     * @param closeActions          the actions which will run when this gui is closed
     *
     */
    public HotbarGui(ServerPlayer player, Menu menu, List<ClickAction> closeActions) {
        super(player);
        this.menu = menu;
        this.closeActions = closeActions;
    }

    @Override
    public void onClose() {
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
