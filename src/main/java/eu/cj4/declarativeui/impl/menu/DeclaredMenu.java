package eu.cj4.declarativeui.impl.menu;

import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import eu.cj4.declarativeui.impl.menu.container.provider.DeclaredContainerProvider;
import eu.cj4.declarativeui.impl.registry.DeclarativeUIRegistries;
import eu.cj4.declarativeui.impl.menu.slot.DeclaredRedirect;
import eu.cj4.declarativeui.impl.menu.slot.DeclaredSlot;
import eu.cj4.declarativeui.impl.menu.slot.LockedSlot;
import eu.pb4.sgui.api.gui.SimpleGui;
import eu.pb4.sgui.api.gui.SimpleGuiBuilder;
import net.minecraft.Util;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.core.Holder;
import net.minecraft.core.Registry;
import net.minecraft.core.RegistryAccess;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.ComponentSerialization;
import net.minecraft.network.chat.ComponentUtils;
import net.minecraft.resources.RegistryFixedCodec;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.Container;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.inventory.Slot;
import org.jetbrains.annotations.NotNull;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

public record DeclaredMenu(Optional<Component> title, Holder<MenuType<?>> menuType, boolean manipulatePlayerSlots, boolean lockPlayerInventory, List<DeclaredSlot> slots, List<DeclaredContainerProvider> containers) {
    public static final Codec<DeclaredMenu> DIRECT_CODEC = RecordCodecBuilder.create(instance ->
            instance.group(
                    ComponentSerialization.CODEC.optionalFieldOf("title").forGetter(DeclaredMenu::title),
                    RegistryFixedCodec.create(Registries.MENU).fieldOf("menu_type").forGetter(DeclaredMenu::menuType),
                    Codec.BOOL.optionalFieldOf("manipulate_player_slots", false).forGetter(DeclaredMenu::manipulatePlayerSlots),
                    Codec.BOOL.optionalFieldOf("lock_player_inventory", false).forGetter(DeclaredMenu::lockPlayerInventory),
                    Codec.list(DeclaredSlot.CODEC).fieldOf("slots").forGetter(DeclaredMenu::slots),
                    Codec.list(DeclaredContainerProvider.CODEC).optionalFieldOf("containers", Collections.emptyList()).forGetter(DeclaredMenu::containers)
            ).apply(instance, DeclaredMenu::new));

    public Component getDefaultTitle(RegistryAccess access) {
        Registry<DeclaredMenu> MENU_REGISTRY = access.lookupOrThrow(DeclarativeUIRegistries.MENU_REGISTRY);
        ResourceLocation guiId = MENU_REGISTRY.getKey(this);
        return Component.translatable(Util.makeDescriptionId("container", guiId));
    }

    public @NotNull SimpleGuiBuilder instantiate(CommandSourceStack sourceStack) throws CommandSyntaxException {
        MenuType<?> menuType = this.menuType.value();
        SimpleGuiBuilder builder = new SimpleGuiBuilder(menuType, manipulatePlayerSlots);

        builder.setTitle(ComponentUtils.updateForEntity(sourceStack, title.orElseGet(() -> getDefaultTitle(sourceStack.registryAccess())), sourceStack.getEntity(), 0));

        for (DeclaredSlot declaredSlot : this.slots) {
            builder.setSlot(declaredSlot.slot(), declaredSlot.createElement(sourceStack, declaredSlot.clickCallback(this)));
        }

        for (DeclaredContainerProvider declaredContainer : this.containers) {
            boolean viewOnly = declaredContainer.viewOnly();
            Container container = declaredContainer.provider().getContainer(sourceStack.getEntity());
            if (container == null) continue;
            for (DeclaredRedirect redirect : declaredContainer.redirects()) {
                builder.setSlotRedirect(redirect.slot(), redirect.viewOnly().orElse(viewOnly)
                        ? new LockedSlot(container, redirect.containerSlot(), 0, 0)
                        : new Slot(container, redirect.containerSlot(), 0, 0)
                );
            }
        }

        return builder;
    }

    public void open(CommandSourceStack sourceStack, Collection<ServerPlayer> targets) throws CommandSyntaxException {
        SimpleGuiBuilder builder = instantiate(sourceStack);
        for (ServerPlayer target : targets) {
            SimpleGui gui = builder.build(target);
            gui.setLockPlayerInventory(this.lockPlayerInventory());
            gui.open();
        }
    }
}
