package eu.cj4.declarativeui.impl.menu.slot;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import eu.cj4.declarativeui.api.menu.Menu;
import eu.cj4.declarativeui.api.menu.slot.Slot;
import eu.cj4.declarativeui.api.container.ContainerProvider;
import eu.cj4.declarativeui.impl.container.provider.ContainerProviders;
import eu.pb4.sgui.api.gui.SlotBasedGui;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.world.Container;
import net.minecraft.world.level.storage.loot.LootContext;

import java.util.List;

public record RedirectSlots(ContainerProvider provider, List<DeclaredRedirect> redirects, boolean viewOnly) implements Slot {
    public static final MapCodec<RedirectSlots> MAP_CODEC = RecordCodecBuilder.mapCodec(instance ->
            instance.group(
                    ContainerProviders.TYPED_CODEC.fieldOf("provider").forGetter(RedirectSlots::provider),
                    Codec.list(DeclaredRedirect.CODEC).fieldOf("redirects").forGetter(RedirectSlots::redirects),
                    Codec.BOOL.optionalFieldOf("view_only", false).forGetter(RedirectSlots::viewOnly)
            ).apply(instance, RedirectSlots::new));

    @Override
    public MapCodec<RedirectSlots> codec() {
        return MAP_CODEC;
    }

    @Override
    public void build(Menu menu, CommandSourceStack sourceStack, SlotBasedGui gui, LootContext lootContext) {
        boolean viewOnly = this.viewOnly;
        Container container = this.provider.getContainer(sourceStack.getEntity());
        if (container != null) {
            for (DeclaredRedirect redirect : this.redirects) {
                gui.setSlot(redirect.slot(), redirect.viewOnly().orElse(viewOnly)
                        ? new LockedSlot(container, redirect.containerSlot(), 0, 0)
                        : new net.minecraft.world.inventory.Slot(container, redirect.containerSlot(), 0, 0)
                );
            }
        }
    }
}
