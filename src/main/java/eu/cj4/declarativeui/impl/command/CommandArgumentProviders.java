package eu.cj4.declarativeui.impl.command;

import com.google.common.collect.Maps;
import com.mojang.brigadier.arguments.*;
import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import eu.cj4.declarativeui.api.codec.LazyEnumCodec;
import eu.cj4.declarativeui.mixin.argument.*;
import net.minecraft.commands.arguments.*;
import net.minecraft.commands.arguments.coordinates.*;
import net.minecraft.commands.arguments.item.FunctionArgument;
import net.minecraft.commands.synchronization.ArgumentTypeInfo;
import net.minecraft.commands.synchronization.ArgumentTypeInfos;
import net.minecraft.core.registries.BuiltInRegistries;

import java.util.Map;

public final class CommandArgumentProviders {
    public static final Codec<ArgumentType<?>> TYPED_CODEC;
    private static final Map<ArgumentTypeInfo<?, ?>, MapCodec<? extends ArgumentType<?>>> BY_INFO = Maps.newHashMap();

    private static final Codec<StringArgumentType.StringType> STRING_TYPE_CODEC = LazyEnumCodec.fromEnum(StringArgumentType.StringType.values());
    private static final MapCodec<FloatArgumentType> FLOAT_ARGUMENT_MAP_CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
            Codec.FLOAT.optionalFieldOf("min", -Float.MAX_VALUE).forGetter(FloatArgumentType::getMinimum),
            Codec.FLOAT.optionalFieldOf("max", Float.MAX_VALUE).forGetter(FloatArgumentType::getMaximum)
    ).apply(instance, FloatArgumentType::floatArg));
    private static final MapCodec<DoubleArgumentType> DOUBLE_ARGUMENT_MAP_CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
            Codec.DOUBLE.optionalFieldOf("min", -Double.MAX_VALUE).forGetter(DoubleArgumentType::getMinimum),
            Codec.DOUBLE.optionalFieldOf("max", Double.MAX_VALUE).forGetter(DoubleArgumentType::getMaximum)
    ).apply(instance, DoubleArgumentType::doubleArg));
    private static final MapCodec<IntegerArgumentType> INTEGER_ARGUMENT_MAP_CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
            Codec.INT.optionalFieldOf("min", Integer.MIN_VALUE).forGetter(IntegerArgumentType::getMinimum),
            Codec.INT.optionalFieldOf("max", Integer.MAX_VALUE).forGetter(IntegerArgumentType::getMaximum)
    ).apply(instance, IntegerArgumentType::integer));
    private static final MapCodec<LongArgumentType> LONG_ARGUMENT_MAP_CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
            Codec.LONG.optionalFieldOf("min", Long.MIN_VALUE).forGetter(LongArgumentType::getMinimum),
            Codec.LONG.optionalFieldOf("max", Long.MAX_VALUE).forGetter(LongArgumentType::getMaximum)
    ).apply(instance, LongArgumentType::longArg));
    private static final MapCodec<EntityArgument> ENTITY_ARGUMENT_MAP_CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
            Codec.BOOL.fieldOf("single").forGetter(object -> ((EntityArgumentAccessor) object).isSingle()),
            Codec.BOOL.fieldOf("players_only").forGetter(object -> ((EntityArgumentAccessor) object).isPlayersOnly())
    ).apply(instance, EntityArgumentAccessor::create));
    private static final MapCodec<StringArgumentType> STRING_ARGUMENT_MAP_CODEC = STRING_TYPE_CODEC.fieldOf("variant").xmap(StringArgumentTypeAccessor::create, StringArgumentType::getType);
    private static final MapCodec<TimeArgument> TIME_ARGUMENT_MAP_CODEC = Codec.INT.optionalFieldOf("min", 0).xmap(TimeArgument::time, timeArgument -> ((TimeArgumentAccessor) timeArgument).getMinimum());
    private static final MapCodec<ScoreHolderArgument> SCORE_HOLDER_MAP_CODEC = Codec.BOOL.fieldOf("multiple").xmap(ScoreHolderArgument::new, scoreHolderArgument -> ((ScoreHolderArgumentAccessor) scoreHolderArgument).isMultiple());

    public static <T> void registerStable(Class<? extends ArgumentType<T>> argumentClass, ArgumentType<T> stableInstance) {
        register(argumentClass, RecordCodecBuilder.mapCodec(instance -> instance.stable(stableInstance)));
    }

    public static <T> void register(Class<? extends ArgumentType<T>> argumentClass, MapCodec<? extends ArgumentType<T>> mapCodec) {
        BY_INFO.put(ArgumentTypeInfosAccessor.getBY_CLASS().get(argumentClass), mapCodec);
    }

    public static void bootStrap() {
    }

    static {
        TYPED_CODEC = BuiltInRegistries.COMMAND_ARGUMENT_TYPE.byNameCodec().dispatch(ArgumentTypeInfos::byClass, BY_INFO::get);

        registerStable(BoolArgumentType.class, BoolArgumentType.bool());
        register(FloatArgumentType.class, FLOAT_ARGUMENT_MAP_CODEC);
        register(DoubleArgumentType.class, DOUBLE_ARGUMENT_MAP_CODEC);
        register(IntegerArgumentType.class, INTEGER_ARGUMENT_MAP_CODEC);
        register(LongArgumentType.class, LONG_ARGUMENT_MAP_CODEC);
        register(StringArgumentType.class, STRING_ARGUMENT_MAP_CODEC);
        register(EntityArgument.class, ENTITY_ARGUMENT_MAP_CODEC);
        registerStable(GameProfileArgument.class, GameProfileArgument.gameProfile());
        registerStable(BlockPosArgument.class, BlockPosArgument.blockPos());
        registerStable(ColumnPosArgument.class, ColumnPosArgument.columnPos());
        registerStable(Vec3Argument.class, Vec3Argument.vec3());
        registerStable(Vec2Argument.class, Vec2Argument.vec2());
        registerStable(ColorArgument.class, ColorArgument.color());
        registerStable(HexColorArgument.class, HexColorArgument.hexColor());
        registerStable(MessageArgument.class, MessageArgument.message());
        registerStable(CompoundTagArgument.class, CompoundTagArgument.compoundTag());
        registerStable(NbtTagArgument.class, NbtTagArgument.nbtTag());
        registerStable(NbtPathArgument.class, NbtPathArgument.nbtPath());
        registerStable(AngleArgument.class, AngleArgument.angle());
        registerStable(RotationArgument.class, RotationArgument.rotation());
        registerStable(ScoreboardSlotArgument.class, ScoreboardSlotArgument.displaySlot());
        register(ScoreHolderArgument.class, SCORE_HOLDER_MAP_CODEC);
        registerStable(SwizzleArgument.class, SwizzleArgument.swizzle());
        registerStable(TeamArgument.class, TeamArgument.team());
        registerStable(ResourceLocationArgument.class, ResourceLocationArgument.id());
        registerStable(FunctionArgument.class, FunctionArgument.functions());
        registerStable(EntityAnchorArgument.class, EntityAnchorArgument.anchor());
        registerStable(RangeArgument.Ints.class, RangeArgument.intRange());
        registerStable(RangeArgument.Floats.class, RangeArgument.floatRange());
        registerStable(DimensionArgument.class, DimensionArgument.dimension());
        registerStable(GameModeArgument.class, GameModeArgument.gameMode());
        register(TimeArgument.class, TIME_ARGUMENT_MAP_CODEC);
        registerStable(TemplateMirrorArgument.class, TemplateMirrorArgument.templateMirror());
        registerStable(TemplateRotationArgument.class, TemplateRotationArgument.templateRotation());
        registerStable(HeightmapTypeArgument.class, HeightmapTypeArgument.heightmap());
        registerStable(UuidArgument.class, UuidArgument.uuid());
        //ArgumentTypeInfos
    }
}
