package eu.cj4.declarativeui.api.menu.slot;

import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import eu.cj4.declarativeui.api.ClickAction;
import eu.cj4.declarativeui.api.codec.LazyEnumCodec;
import eu.cj4.declarativeui.api.menu.DeclaredMenu;
import eu.pb4.sgui.api.ClickType;
import eu.pb4.sgui.api.gui.SlotGuiInterface;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.network.chat.ComponentUtils;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerPlayer;

import java.util.Collections;
import java.util.Optional;

public record ClickEvent(Optional<ResourceLocation> function, ClickAction action, Optional<ClickType> clickType, Optional<net.minecraft.world.inventory.ClickType> actionType) {
    private static final Codec<ClickType> CLICK_TYPE_CODEC = LazyEnumCodec.fromEnum(ClickType.values());
    private static final Codec<net.minecraft.world.inventory.ClickType> ACTION_TYPE_CODEC = LazyEnumCodec.fromEnum(net.minecraft.world.inventory.ClickType.values());
    public static final Codec<ClickEvent> CODEC = RecordCodecBuilder.create(instance ->
            instance.group(
                    ResourceLocation.CODEC.optionalFieldOf("function").forGetter(ClickEvent::function),
                    ClickAction.CODEC.optionalFieldOf("action", ClickAction.NONE).forGetter(ClickEvent::action),
                    CLICK_TYPE_CODEC.optionalFieldOf("click_type").forGetter(ClickEvent::clickType),
                    ACTION_TYPE_CODEC.optionalFieldOf("action_type").forGetter(ClickEvent::actionType)
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
                    menu.open(commandSourceStack, Collections.singleton(player));
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
}
