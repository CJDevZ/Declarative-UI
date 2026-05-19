package eu.cj4.declarativeui.impl.menu;

import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import eu.cj4.declarativeui.api.menu.Menu;
import eu.cj4.declarativeui.api.menu.slot.action.ClickAction;
import eu.cj4.declarativeui.impl.menu.container.provider.DeclaredContainerProvider;
import eu.cj4.declarativeui.impl.menu.gui.SearchGui;
import eu.cj4.declarativeui.impl.menu.slot.ClickEvent;
import eu.cj4.declarativeui.impl.menu.slot.DeclaredRedirect;
import eu.cj4.declarativeui.impl.menu.slot.DeclaredSlot;
import eu.cj4.declarativeui.impl.menu.slot.LockedSlot;
import eu.cj4.declarativeui.impl.menu.slot.action.ClickActionTypes;
import eu.cj4.declarativeui.impl.registry.DeclarativeUIRegistries;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.core.Registry;
import net.minecraft.core.RegistryAccess;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.ComponentSerialization;
import net.minecraft.network.chat.ComponentUtils;
import net.minecraft.resources.Identifier;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.util.Util;
import net.minecraft.world.Container;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.LootParams;
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSets;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

public record SearchMenu(Optional<Component> title, String searchTag, boolean manipulatePlayerSlots, boolean lockPlayerInventory, List<DeclaredSlot> slots, List<DeclaredContainerProvider> containers, List<ClickEvent> searchActions, List<ClickAction> closeActions) implements Menu {
    public static final MapCodec<SearchMenu> CODEC = RecordCodecBuilder.mapCodec(instance ->
            instance.group(
                    ComponentSerialization.CODEC.optionalFieldOf("title").forGetter(SearchMenu::title),
                    Codec.STRING.optionalFieldOf("search_tag", "search").forGetter(SearchMenu::searchTag),
                    Codec.BOOL.optionalFieldOf("manipulate_player_slots", false).forGetter(SearchMenu::manipulatePlayerSlots),
                    Codec.BOOL.optionalFieldOf("lock_player_inventory", false).forGetter(SearchMenu::lockPlayerInventory),
                    Codec.list(DeclaredSlot.CODEC).optionalFieldOf("slots", Collections.emptyList()).forGetter(SearchMenu::slots),
                    Codec.list(DeclaredContainerProvider.CODEC).optionalFieldOf("containers", Collections.emptyList()).forGetter(SearchMenu::containers),
                    ClickEvent.LIST_CODEC.optionalFieldOf("search_actions", Collections.emptyList()).forGetter(SearchMenu::searchActions),
                    ClickActionTypes.LIST_CODEC.optionalFieldOf("close_actions", Collections.emptyList()).forGetter(SearchMenu::closeActions)
            ).apply(instance, SearchMenu::new));

    @Override
    public eu.cj4.declarativeui.api.menu.MenuType getType() {
        return MenuTypes.SEARCH;
    }

    public Component getDefaultTitle(RegistryAccess access) {
        Registry<Menu> MENU_REGISTRY = access.lookupOrThrow(DeclarativeUIRegistries.MENU);
        Identifier guiId = MENU_REGISTRY.getKey(this);
        return Component.translatable(Util.makeDescriptionId("container", guiId));
    }

    public void open(CommandSourceStack sourceStack, Collection<ServerPlayer> targets) throws CommandSyntaxException {
        Component title = ComponentUtils.updateForEntity(sourceStack, this.title.orElseGet(() -> getDefaultTitle(sourceStack.registryAccess())), sourceStack.getEntity(), 0);

        ServerLevel serverLevel = sourceStack.getLevel();
        LootParams lootParams = (new LootParams.Builder(serverLevel)).withParameter(LootContextParams.ORIGIN, sourceStack.getPosition()).withOptionalParameter(LootContextParams.THIS_ENTITY, sourceStack.getEntity()).create(LootContextParamSets.COMMAND);
        LootContext lootContext = (new LootContext.Builder(lootParams)).create(Optional.empty());

        for (ServerPlayer target : targets) {
            SearchGui gui = new SearchGui(target, this.manipulatePlayerSlots, this, this.searchTag, this.searchActions, this.closeActions);
            int size = gui.getSize();
            gui.setTitle(title);
            gui.setLockPlayerInventory(this.lockPlayerInventory);

            for (DeclaredSlot declaredSlot : this.slots) {
                int slot = declaredSlot.slot().getInt(lootContext);
                if (size < 0 || slot >= size) continue;
                gui.setSlot(slot, declaredSlot.createElement(sourceStack, declaredSlot.clickCallback(this)));
            }

            for (DeclaredContainerProvider declaredContainer : this.containers) {
                boolean viewOnly = declaredContainer.viewOnly();
                Container container = declaredContainer.provider().getContainer(sourceStack.getEntity());
                if (container == null) continue;
                for (DeclaredRedirect redirect : declaredContainer.redirects()) {
                    gui.setSlotRedirect(redirect.slot(), redirect.viewOnly().orElse(viewOnly)
                            ? new LockedSlot(container, redirect.containerSlot(), 0, 0)
                            : new Slot(container, redirect.containerSlot(), 0, 0)
                    );
                }
            }

            gui.open();
        }
    }
}
