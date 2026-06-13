package eu.cj4.declarativeui.impl.slotsource;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import eu.cj4.declarativeui.api.container.NamespacedContainerHolder;
import eu.cj4.declarativeui.impl.DeclarativeUI;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.Identifier;
import net.minecraft.util.InclusiveRange;
import net.minecraft.world.Container;
import net.minecraft.world.entity.SlotAccess;
import net.minecraft.world.item.slot.SlotCollection;
import net.minecraft.world.item.slot.SlotSource;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.LootContextArg;
import org.jspecify.annotations.NonNull;

import java.util.ArrayList;
import java.util.List;

public record ContainerSlotSource(LootContextArg<Object> source, Identifier container, InclusiveRange<Integer> range) implements SlotSource {
    public static final MapCodec<ContainerSlotSource> MAP_CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
            LootContextArg.createArgCodec(argCodecBuilder -> argCodecBuilder.anyOf(LootContext.EntityTarget.values())).fieldOf("source").forGetter(ContainerSlotSource::source),
            Identifier.CODEC.fieldOf("container").forGetter(ContainerSlotSource::container),
            InclusiveRange.INT.fieldOf("range").forGetter(ContainerSlotSource::range)
    ).apply(instance, ContainerSlotSource::new));

    @Override
    public @NonNull MapCodec<? extends SlotSource> codec() {
        return MAP_CODEC;
    }

    @Override
    public @NonNull SlotCollection provide(@NonNull LootContext lootContext) {
        Object object = this.source.get(lootContext);
        if (!(object instanceof NamespacedContainerHolder containerHolder)) {
            return SlotCollection.EMPTY;
        }

        Container container = containerHolder.declarative_ui$namespacedContainer(this.container);
        if (container == null) {
            return SlotCollection.EMPTY;
        }

        int min = Math.max(this.range.minInclusive(), 0);
        int max = Math.min(range.maxInclusive(), container.getContainerSize() - 1);
        if (max < min) {
            return SlotCollection.EMPTY;
        }

        List<SlotAccess> slotAccessList = new ArrayList<>(max - min + 1);
        for (int slot = min; slot <= max; slot++) {
            slotAccessList.add(container.getSlot(slot));
        }

        return SlotCollection.of(slotAccessList);
    }

    public static void bootStrap() {
        Registry.register(BuiltInRegistries.SLOT_SOURCE_TYPE, Identifier.fromNamespaceAndPath(DeclarativeUI.MOD_ID, "container"), MAP_CODEC);
    }
}
