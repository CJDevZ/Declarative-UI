package eu.cj4.declarativeui.api.menu;

import com.mojang.serialization.MapCodec;

public record MenuType(MapCodec<? extends Menu> codec) {

}
