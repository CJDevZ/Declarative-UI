package eu.cj4.declarativeui.impl.menu.slot;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import eu.cj4.declarativeui.api.menu.slot.Slot;
import eu.cj4.declarativeui.api.menu.slot.SlotType;
import eu.cj4.declarativeui.impl.DeclarativeUI;
import eu.cj4.declarativeui.impl.registry.DeclarativeUIBuiltInRegistries;
import net.minecraft.resources.Identifier;

public class SlotTypes {
    public static final Codec<Slot> TYPED_CODEC;
    public static final SlotType SIMPLE;
    public static final SlotType REDIRECT;
    public static final SlotType FILL;

    private static SlotType register(String name, MapCodec<? extends Slot> mapCodec) {
        return SlotType.register(Identifier.fromNamespaceAndPath(DeclarativeUI.MOD_ID, name), mapCodec);
    }

    public static void bootStrap() {
    }

    static {
        TYPED_CODEC = DeclarativeUIBuiltInRegistries.SLOT_TYPE.byNameCodec().dispatch(Slot::getType, SlotType::codec);
        SIMPLE = register("simple", SimpleSlot.MAP_CODEC);
        REDIRECT = register("redirect", RedirectSlots.MAP_CODEC);
        FILL = register("fill", FillSlots.MAP_CODEC);
    }
}
