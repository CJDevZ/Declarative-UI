package eu.cj4.declarativeui.impl;

import net.minecraft.world.Container;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;

public class LockableSlot extends Slot {
    private final boolean locked;

    public LockableSlot(Container container, int slot, int x, int y, boolean locked) {
        super(container, slot, x, y);
        this.locked = locked;
    }

    @Override
    public boolean mayPlace(ItemStack itemStack) {
        return !locked && super.mayPlace(itemStack);
    }

    @Override
    public boolean mayPickup(Player player) {
        return !locked && super.mayPickup(player);
    }
}
