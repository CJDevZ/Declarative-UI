package eu.cj4.declarativeui.impl.menu;

import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import eu.cj4.declarativeui.api.menu.Menu;
import eu.cj4.declarativeui.api.menu.slot.ClickAction;
import eu.cj4.declarativeui.api.menu.slot.Slot;
import eu.cj4.declarativeui.impl.menu.gui.HotbarGui;
import eu.cj4.declarativeui.impl.menu.slot.SlotTypes;
import eu.cj4.declarativeui.impl.menu.slot.action.ClickActionTypes;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.core.RegistryAccess;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.LootParams;
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSets;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

public record HotbarMenu(List<Slot> slots, List<ClickAction> closeActions) implements Menu {
    public static final MapCodec<HotbarMenu> MAP_CODEC = RecordCodecBuilder.mapCodec(instance ->
            instance.group(
                    Codec.list(SlotTypes.TYPED_CODEC).fieldOf("slots").forGetter(HotbarMenu::slots),
                    ClickActionTypes.LIST_CODEC.optionalFieldOf("close_actions", Collections.emptyList()).forGetter(HotbarMenu::closeActions)
            ).apply(instance, HotbarMenu::new));

    @Override
    public MapCodec<HotbarMenu> codec() {
        return MAP_CODEC;
    }

    @Override
    public Optional<Component> title() {
        return Optional.empty();
    }

    @Override
    public Component getDefaultTitle(RegistryAccess registryAccess) {
        return Component.empty();
    }

    @Override
    public void open(CommandSourceStack sourceStack, Collection<ServerPlayer> targets) throws CommandSyntaxException {
        ServerLevel serverLevel = sourceStack.getLevel();
        LootParams lootParams = (new LootParams.Builder(serverLevel)).withParameter(LootContextParams.ORIGIN, sourceStack.getPosition()).withOptionalParameter(LootContextParams.THIS_ENTITY, sourceStack.getEntity()).create(LootContextParamSets.COMMAND);
        LootContext lootContext = (new LootContext.Builder(lootParams)).create(Optional.empty());

        for (ServerPlayer target : targets) {
            HotbarGui gui = new HotbarGui(target, this, this.closeActions);

            for (Slot declaredSlot : this.slots) {
                declaredSlot.build(this, sourceStack, gui, lootContext);
            }

            gui.open();
        }
    }
}
