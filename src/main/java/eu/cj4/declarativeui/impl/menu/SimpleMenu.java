package eu.cj4.declarativeui.impl.menu;

import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import eu.cj4.declarativeui.api.menu.Menu;
import eu.cj4.declarativeui.api.menu.slot.Slot;
import eu.cj4.declarativeui.api.menu.slot.action.ClickAction;
import eu.cj4.declarativeui.impl.menu.slot.*;
import eu.cj4.declarativeui.impl.menu.gui.SimpleGui;
import eu.cj4.declarativeui.impl.menu.slot.action.ClickActionTypes;
import eu.cj4.declarativeui.impl.registry.DeclarativeUIRegistries;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.core.Holder;
import net.minecraft.core.Registry;
import net.minecraft.core.RegistryAccess;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.ComponentSerialization;
import net.minecraft.network.chat.ComponentUtils;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.RegistryFixedCodec;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.util.Util;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.LootParams;
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSets;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

public record SimpleMenu(Optional<Component> title, Holder<MenuType<?>> menuType, boolean manipulatePlayerSlots, boolean lockPlayerInventory, List<Slot> slots, List<ClickAction> closeActions) implements Menu {
    public static final MapCodec<SimpleMenu> MAP_CODEC = RecordCodecBuilder.mapCodec(instance ->
            instance.group(
                    ComponentSerialization.CODEC.optionalFieldOf("title").forGetter(SimpleMenu::title),
                    RegistryFixedCodec.create(Registries.MENU).fieldOf("menu_type").forGetter(SimpleMenu::menuType),
                    Codec.BOOL.optionalFieldOf("manipulate_player_slots", false).forGetter(SimpleMenu::manipulatePlayerSlots),
                    Codec.BOOL.optionalFieldOf("lock_player_inventory", false).forGetter(SimpleMenu::lockPlayerInventory),
                    Codec.list(SlotTypes.TYPED_CODEC).fieldOf("slots").forGetter(SimpleMenu::slots),
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
        Component title = ComponentUtils.updateForEntity(sourceStack, this.title.orElseGet(() -> getDefaultTitle(sourceStack.registryAccess())), sourceStack.getEntity(), 0);

        ServerLevel serverLevel = sourceStack.getLevel();
        LootParams lootParams = (new LootParams.Builder(serverLevel)).withParameter(LootContextParams.ORIGIN, sourceStack.getPosition()).withOptionalParameter(LootContextParams.THIS_ENTITY, sourceStack.getEntity()).create(LootContextParamSets.COMMAND);
        LootContext lootContext = (new LootContext.Builder(lootParams)).create(Optional.empty());

        MenuType<?> menuType = this.menuType.value();
        for (ServerPlayer target : targets) {
            SimpleGui gui = new SimpleGui(this, menuType, target, this.manipulatePlayerSlots, this.closeActions);
            gui.setTitle(title);

            for (Slot declaredSlot : this.slots) {
                declaredSlot.build(this, sourceStack, gui, lootContext);
            }

            gui.open();
        }
    }
}
