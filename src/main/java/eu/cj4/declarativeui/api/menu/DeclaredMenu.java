package eu.cj4.declarativeui.api.menu;

import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import eu.cj4.declarativeui.api.menu.slot.ClickEvent;
import eu.cj4.declarativeui.api.providers.DeclaredContainerProvider;
import eu.cj4.declarativeui.impl.DeclarativeUI;
import eu.cj4.declarativeui.impl.LockableSlot;
import eu.cj4.declarativeui.api.menu.slot.DeclaredRedirect;
import eu.cj4.declarativeui.api.menu.slot.DeclaredSlot;
import eu.pb4.sgui.api.ClickType;
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
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.NotNull;

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
        Registry<DeclaredMenu> MENU_REGISTRY = access.lookupOrThrow(DeclarativeUI.UI_REGISTRY);
        ResourceLocation guiId = MENU_REGISTRY.getKey(this);
        return Component.translatable(Util.makeDescriptionId("container", guiId));
    }

    public @NotNull SimpleGuiBuilder instantiate(CommandSourceStack sourceStack) throws CommandSyntaxException {
        MenuType<?> menuType = this.menuType.value();
        SimpleGuiBuilder builder = new SimpleGuiBuilder(menuType, manipulatePlayerSlots);

        builder.setTitle(ComponentUtils.updateForEntity(sourceStack, title.orElseGet(() -> getDefaultTitle(sourceStack.registryAccess())), sourceStack.getEntity(), 0));

        for (DeclaredSlot declaredSlot : slots) {
            ItemStack stack = declaredSlot.getItemStack(sourceStack);
            if (declaredSlot.clickEvent().isEmpty()) {
                builder.setSlot(declaredSlot.slot(), stack);
            } else {
                builder.setSlot(declaredSlot.slot(), stack, (i, clickType, clickType1, slotGuiInterface) -> {
                    for (ClickEvent clickEvent : declaredSlot.clickEvent()) {
                        Optional<ClickType> clickType2 = clickEvent.clickType();
                        if (clickType2.isEmpty() || clickType2.get() == clickType) {
                            clickEvent.click(this, slotGuiInterface);
                        }
                    }
                });
            }
        }

        return builder;
    }

    public void open(CommandSourceStack sourceStack, ServerPlayer target) throws CommandSyntaxException {
        SimpleGui gui = instantiate(sourceStack).build(target);

        for (DeclaredContainerProvider declaredContainer : this.containers()) {
            boolean viewOnly = declaredContainer.viewOnly();
            Container container = declaredContainer.getContainer(target);
            if (container == null) continue;
            for (DeclaredRedirect redirect : declaredContainer.redirects()) {
                gui.setSlotRedirect(redirect.slot(), new LockableSlot(container, redirect.containerSlot(), 0, 0, redirect.viewOnly().orElse(viewOnly)));
            }
        }

        gui.setLockPlayerInventory(this.lockPlayerInventory());
        gui.open();
    }
}
