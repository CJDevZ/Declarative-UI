package eu.cj4.declarativeui.impl.menu.slot;

import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import eu.cj4.declarativeui.api.codec.LazyEnumCodec;
import eu.cj4.declarativeui.api.menu.slot.action.ClickAction;
import eu.cj4.declarativeui.impl.menu.DeclaredMenu;
import eu.cj4.declarativeui.impl.menu.slot.action.ClickActionTypes;
import eu.pb4.sgui.api.ClickType;
import eu.pb4.sgui.api.gui.SlotGuiInterface;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

public record ClickEvent(List<ClickAction> actions, Optional<ClickType> clickType, Optional<net.minecraft.world.inventory.ClickType> actionType) {
    private static final Codec<ClickType> CLICK_TYPE_CODEC = LazyEnumCodec.fromEnum(ClickType.values());
    private static final Codec<net.minecraft.world.inventory.ClickType> ACTION_TYPE_CODEC = LazyEnumCodec.fromEnum(net.minecraft.world.inventory.ClickType.values());
    public static final Codec<ClickEvent> CODEC = RecordCodecBuilder.create(instance ->
            instance.group(
                    Codec.withAlternative(Codec.list(ClickActionTypes.TYPED_CODEC), ClickActionTypes.TYPED_CODEC, Collections::singletonList).optionalFieldOf("actions", Collections.emptyList()).forGetter(ClickEvent::actions),
                    CLICK_TYPE_CODEC.optionalFieldOf("click_type").forGetter(ClickEvent::clickType),
                    ACTION_TYPE_CODEC.optionalFieldOf("action_type").forGetter(ClickEvent::actionType)
            ).apply(instance, ClickEvent::new));

    public void click(DeclaredMenu menu, SlotGuiInterface slotGuiInterface) throws CommandSyntaxException {
        for (ClickAction clickAction : this.actions) {
            clickAction.click(menu, slotGuiInterface);
        }
    }
}
