package eu.cj4.declarativeui.impl.menu;

import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import eu.cj4.declarativeui.api.menu.Menu;
import eu.cj4.declarativeui.api.menu.slot.action.ClickAction;
import eu.cj4.declarativeui.impl.menu.container.provider.DeclaredContainerProvider;
import eu.cj4.declarativeui.impl.menu.gui.SimpleGui;
import eu.cj4.declarativeui.impl.menu.slot.action.ClickActionTypes;
import eu.cj4.declarativeui.impl.registry.DeclarativeUIRegistries;
import eu.cj4.declarativeui.impl.menu.slot.DeclaredRedirect;
import eu.cj4.declarativeui.impl.menu.slot.DeclaredSlot;
import eu.cj4.declarativeui.impl.menu.slot.LockedSlot;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.core.Holder;
import net.minecraft.core.Registry;
import net.minecraft.core.RegistryAccess;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.ComponentSerialization;
import net.minecraft.network.chat.ComponentUtils;
import net.minecraft.network.chat.ResolutionContext;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.RegistryFixedCodec;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.util.Util;
import net.minecraft.world.Container;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.inventory.Slot;
import org.jspecify.annotations.NonNull;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

public record SimpleMenu(Optional<Component> title, Holder<MenuType<?>> menuType, boolean manipulatePlayerSlots, boolean lockPlayerInventory, List<DeclaredSlot> slots, List<DeclaredContainerProvider> containers, List<ClickAction> closeActions) implements Menu {
    public static final MapCodec<SimpleMenu> CODEC = RecordCodecBuilder.mapCodec(instance ->
            instance.group(
                    ComponentSerialization.CODEC.optionalFieldOf("title").forGetter(SimpleMenu::title),
                    RegistryFixedCodec.create(Registries.MENU).fieldOf("menu_type").forGetter(SimpleMenu::menuType),
                    Codec.BOOL.optionalFieldOf("manipulate_player_slots", false).forGetter(SimpleMenu::manipulatePlayerSlots),
                    Codec.BOOL.optionalFieldOf("lock_player_inventory", false).forGetter(SimpleMenu::lockPlayerInventory),
                    Codec.list(DeclaredSlot.CODEC).fieldOf("slots").forGetter(SimpleMenu::slots),
                    Codec.list(DeclaredContainerProvider.CODEC).optionalFieldOf("containers", Collections.emptyList()).forGetter(SimpleMenu::containers),
                    ClickActionTypes.LIST_CODEC.optionalFieldOf("close_actions", Collections.emptyList()).forGetter(SimpleMenu::closeActions)
            ).apply(instance, SimpleMenu::new));

    @Override
    public eu.cj4.declarativeui.api.menu.MenuType getType() {
        return MenuTypes.SIMPLE;
    }

    public Component getDefaultTitle(RegistryAccess access) {
        Registry<Menu> MENU_REGISTRY = access.lookupOrThrow(DeclarativeUIRegistries.MENU);
        Identifier guiId = MENU_REGISTRY.getKey(this);
        return Component.translatable(Util.makeDescriptionId("container", guiId));
    }

    public void open(CommandSourceStack sourceStack, Collection<ServerPlayer> targets) throws CommandSyntaxException {
        MenuType<?> menuType = this.menuType.value();
        Component title = ComponentUtils.resolve(ResolutionContext.create(sourceStack), this.title.orElseGet(() -> getDefaultTitle(sourceStack.registryAccess())));
        for (ServerPlayer target : targets) {
            SimpleGui gui = new SimpleGui(this, menuType, target, this.manipulatePlayerSlots, this.closeActions);
            gui.setTitle(title);

            for (DeclaredSlot declaredSlot : this.slots) {
                gui.setSlot(declaredSlot.slot(), declaredSlot.createElement(sourceStack, declaredSlot.clickCallback(this)));
            }

            for (DeclaredContainerProvider declaredContainer : this.containers) {
                boolean viewOnly = declaredContainer.viewOnly();
                Container container = declaredContainer.provider().getContainer(sourceStack.getEntity());
                if (container == null) continue;
                for (DeclaredRedirect redirect : declaredContainer.redirects()) {
                    gui.setSlot(redirect.slot(), redirect.viewOnly().orElse(viewOnly)
                            ? new LockedSlot(container, redirect.containerSlot(), 0, 0)
                            : new Slot(container, redirect.containerSlot(), 0, 0)
                    );
                }
            }

            gui.open();
        }
    }
}
