package eu.cj4.declarativeui.api.menu.slot;

import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.datafixers.util.Pair;
import com.mojang.serialization.Codec;
import com.mojang.serialization.DataResult;
import com.mojang.serialization.DynamicOps;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import eu.cj4.declarativeui.api.ClickAction;
import eu.cj4.declarativeui.api.menu.DeclaredMenu;
import eu.pb4.sgui.api.ClickType;
import eu.pb4.sgui.api.gui.SlotGuiInterface;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.network.chat.ComponentUtils;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.util.ExtraCodecs;
import net.minecraft.util.StringRepresentable;

import java.util.Locale;
import java.util.Optional;
import java.util.function.Function;
import java.util.function.ToIntFunction;

public record ClickEvent(Optional<ResourceLocation> function, ClickAction action, Optional<ClickType> clickType) {
    private static final Codec<ClickType> CLICK_TYPE_CODEC = new StandardEnumCodec<>(ClickType.values(), StringRepresentable.createNameLookup(ClickType.values(), clickType1 -> clickType1.name().toLowerCase(Locale.ROOT)), Enum::ordinal);
    public static final Codec<ClickEvent> CODEC = RecordCodecBuilder.create(instance ->
            instance.group(
                    ResourceLocation.CODEC.optionalFieldOf("function").forGetter(ClickEvent::function),
                    ClickAction.CODEC.optionalFieldOf("action", ClickAction.NONE).forGetter(ClickEvent::action),
                    CLICK_TYPE_CODEC.optionalFieldOf("click_type").forGetter(ClickEvent::clickType)
            ).apply(instance, ClickEvent::new));

    public void click(DeclaredMenu menu, SlotGuiInterface slotGuiInterface) {
        ServerPlayer serverPlayer = slotGuiInterface.getPlayer();
        MinecraftServer minecraftServer = serverPlayer.level().getServer();
        this.function().flatMap((function) -> minecraftServer.getFunctions().get(function)).ifPresent((commandFunction) -> {
            CommandSourceStack commandSourceStack = serverPlayer.createCommandSourceStack().withSuppressedOutput().withPermission(Commands.LEVEL_GAMEMASTERS);
            minecraftServer.getFunctions().execute(commandFunction, commandSourceStack);
        });
        switch (this.action()) {
            case REFRESH -> {
                ServerPlayer player = slotGuiInterface.getPlayer();
                try {
                    CommandSourceStack commandSourceStack = player.createCommandSourceStack().withSuppressedOutput().withPermission(Commands.LEVEL_GAMEMASTERS);
                    menu.open(commandSourceStack, player);
                } catch (CommandSyntaxException e) {
                    throw new RuntimeException(e);
                }
            }
            case REFRESH_TITLE -> {
                ServerPlayer player = slotGuiInterface.getPlayer();
                try {
                    CommandSourceStack commandSourceStack = player.createCommandSourceStack().withSuppressedOutput().withPermission(Commands.LEVEL_GAMEMASTERS);
                    slotGuiInterface.setTitle(ComponentUtils.updateForEntity(commandSourceStack, menu.title().orElse(menu.getDefaultTitle(serverPlayer.registryAccess())), player, 0));
                } catch (CommandSyntaxException e) {
                    throw new RuntimeException(e);
                }
                slotGuiInterface.open();
            }
            case CLOSE -> slotGuiInterface.close();
        }
    }

    public static class StandardEnumCodec<S extends Enum<?>> implements Codec<S> {
        private final Codec<S> codec;

        protected StandardEnumCodec(S[] stringRepresentables, Function<String, S> function, ToIntFunction<S> toIntFunction) {
            this.codec = ExtraCodecs.orCompressed(Codec.stringResolver(s -> s.name().toLowerCase(Locale.ROOT), function), ExtraCodecs.idResolverCodec(toIntFunction, (i) -> i >= 0 && i < stringRepresentables.length ? stringRepresentables[i] : null, -1));
        }

        public <T> DataResult<Pair<S, T>> decode(DynamicOps<T> dynamicOps, T object) {
            return this.codec.decode(dynamicOps, object);
        }

        public <T> DataResult<T> encode(S stringRepresentable, DynamicOps<T> dynamicOps, T object) {
            return this.codec.encode(stringRepresentable, dynamicOps, object);
        }
    }
}
