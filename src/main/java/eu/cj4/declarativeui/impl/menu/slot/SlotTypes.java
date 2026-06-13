package eu.cj4.declarativeui.impl.menu.slot;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import eu.cj4.declarativeui.api.menu.slot.Slot;
import eu.cj4.declarativeui.impl.DeclarativeUI;
import eu.cj4.declarativeui.impl.registry.DeclarativeUIBuiltInRegistries;
import net.minecraft.core.Registry;
import net.minecraft.resources.Identifier;

public class SlotTypes {
    public static final Codec<Slot> TYPED_CODEC;

    private static void register(String name, MapCodec<? extends Slot> mapCodec) {
        Registry.register(DeclarativeUIBuiltInRegistries.SLOT_TYPE, Identifier.fromNamespaceAndPath(DeclarativeUI.MOD_ID, name), mapCodec);
    }

    public static void bootStrap() {
        register("simple", SimpleSlot.MAP_CODEC);
        register("redirect", RedirectSlots.MAP_CODEC);
        register("fill", FillSlots.MAP_CODEC);
    }

    static {
        TYPED_CODEC = DeclarativeUIBuiltInRegistries.SLOT_TYPE.byNameCodec().dispatch(Slot::codec, c -> c);
    }
}
