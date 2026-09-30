package eu.cj4.declarativeui.impl.command.action;

import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.serialization.MapCodec;
import eu.cj4.declarativeui.api.command.CommandAction;
import eu.cj4.declarativeui.api.menu.Menu;
import eu.cj4.declarativeui.impl.registry.DeclarativeUIRegistries;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.codec.RegistryCodecs;
import net.minecraft.server.level.ServerPlayer;

import java.util.Collections;

public record OpenMenuCommandAction(Holder<Menu> menu) implements CommandAction {
    public static final MapCodec<OpenMenuCommandAction> MAP_CODEC = RegistryCodecs.holder(DeclarativeUIRegistries.MENU).fieldOf("menu").xmap(OpenMenuCommandAction::new, OpenMenuCommandAction::menu);

    @Override
    public MapCodec<OpenMenuCommandAction> codec() {
        return MAP_CODEC;
    }

    @Override
    public int run(CommandContext<CommandSourceStack> context) throws CommandSyntaxException {
        CommandSourceStack sourceStack = context.getSource();
        ServerPlayer player = sourceStack.getPlayerOrException();
        Menu menu = this.menu.value();
        menu.open(sourceStack, Collections.singletonList(player));
        return 1;
    }
}
