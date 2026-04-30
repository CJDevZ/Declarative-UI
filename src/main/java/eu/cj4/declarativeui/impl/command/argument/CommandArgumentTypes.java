package eu.cj4.declarativeui.impl.command.argument;

import com.google.common.collect.Maps;
import com.mojang.brigadier.arguments.*;
import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import eu.cj4.declarativeui.api.codec.LazyEnumCodec;
import eu.cj4.declarativeui.api.command.argument.CommandArgument;
import eu.cj4.declarativeui.api.command.argument.CommandArgumentType;
import eu.cj4.declarativeui.mixin.argument.*;
import net.minecraft.commands.CommandBuildContext;
import net.minecraft.commands.arguments.*;
import net.minecraft.commands.arguments.blocks.BlockPredicateArgument;
import net.minecraft.commands.arguments.blocks.BlockStateArgument;
import net.minecraft.commands.arguments.coordinates.*;
import net.minecraft.commands.arguments.item.FunctionArgument;
import net.minecraft.commands.arguments.item.ItemArgument;
import net.minecraft.commands.arguments.item.ItemPredicateArgument;
import net.minecraft.commands.synchronization.ArgumentTypeInfo;
import net.minecraft.commands.synchronization.ArgumentTypeInfos;
import net.minecraft.core.registries.BuiltInRegistries;

import java.util.Map;
import java.util.function.Function;

public final class CommandArgumentTypes {
    public static final Codec<CommandArgument<?>> TYPED_CODEC;
    private static final Map<ArgumentTypeInfo<?, ?>, MapCodec<? extends CommandArgument<?>>> BY_TYPE_INFO = Maps.newHashMap();

    private static final Codec<StringArgumentType.StringType> STRING_TYPE_CODEC = LazyEnumCodec.fromEnum(StringArgumentType.StringType.values());
    public static final CommandArgumentType FLOAT_ARGUMENT = register(FloatArgumentType.class, RecordCodecBuilder.<FloatArgument>mapCodec(instance -> instance.group(
            Codec.FLOAT.optionalFieldOf("min", -Float.MAX_VALUE).forGetter(FloatArgument::min),
            Codec.FLOAT.optionalFieldOf("max", Float.MAX_VALUE).forGetter(FloatArgument::max)
    ).apply(instance, FloatArgument::new)));
    public static final CommandArgumentType DOUBLE_ARGUMENT = register(DoubleArgumentType.class, RecordCodecBuilder.<DoubleArgument>mapCodec(instance -> instance.group(
            Codec.DOUBLE.optionalFieldOf("min", -Double.MAX_VALUE).forGetter(DoubleArgument::min),
            Codec.DOUBLE.optionalFieldOf("max", Double.MAX_VALUE).forGetter(DoubleArgument::max)
    ).apply(instance, DoubleArgument::new)));
    public static final CommandArgumentType INTEGER_ARGUMENT = register(IntegerArgumentType.class, RecordCodecBuilder.<IntegerArgument>mapCodec(instance -> instance.group(
            Codec.INT.optionalFieldOf("min", Integer.MIN_VALUE).forGetter(IntegerArgument::min),
            Codec.INT.optionalFieldOf("max", Integer.MAX_VALUE).forGetter(IntegerArgument::max)
    ).apply(instance, IntegerArgument::new)));
    public static final CommandArgumentType LONG_ARGUMENT = register(LongArgumentType.class, RecordCodecBuilder.<LongArgument>mapCodec(instance -> instance.group(
            Codec.LONG.optionalFieldOf("min", Long.MIN_VALUE).forGetter(LongArgument::min),
            Codec.LONG.optionalFieldOf("max", Long.MAX_VALUE).forGetter(LongArgument::max)
    ).apply(instance, LongArgument::new)));
    public static final CommandArgumentType ENTITY_ARGUMENT = register(net.minecraft.commands.arguments.EntityArgument.class, RecordCodecBuilder.<EntityArgument>mapCodec(instance -> instance.group(
            Codec.BOOL.fieldOf("single").forGetter(EntityArgument::single),
            Codec.BOOL.fieldOf("players_only").forGetter(EntityArgument::playersOnly)
    ).apply(instance, EntityArgument::new)));
    public static final CommandArgumentType STRING_ARGUMENT = register(StringArgumentType.class, STRING_TYPE_CODEC.fieldOf("variant").xmap(StringArgument::new, StringArgument::type));
    public static final CommandArgumentType TIME_ARGUMENT = register(net.minecraft.commands.arguments.TimeArgument.class, Codec.INT.optionalFieldOf("min", 0).xmap(TimeArgument::new, TimeArgument::min));
    public static final CommandArgumentType SCORE_HOLDER_ARGUMENT = register(net.minecraft.commands.arguments.ScoreHolderArgument.class, Codec.BOOL.fieldOf("multiple").xmap(ScoreHolderArgument::new, ScoreHolderArgument::multiple));

    public static <T> void registerContextAware(Class<? extends ArgumentType<T>> argumentClass, Function<CommandBuildContext, ArgumentType<T>> function) {
        var mapCodec = RecordCodecBuilder.build(RecordCodecBuilder.stable(new ContextAwareArgument<>(ArgumentTypeInfosAccessor.getBY_CLASS().get(argumentClass), function)));
        register(argumentClass, mapCodec);
    }

    public static <T> void registerStable(Class<? extends ArgumentType<T>> argumentClass, ArgumentType<T> argumentType) {
        var mapCodec = RecordCodecBuilder.build(RecordCodecBuilder.stable(new StableArgument<>(ArgumentTypeInfos.byClass(argumentType), argumentType)));
        register(argumentClass, mapCodec);
    }

    public static <T> CommandArgumentType register(Class<? extends ArgumentType<T>> argumentClass, MapCodec<? extends CommandArgument<T>> mapCodec) {
        var argumentTypeInfo = ArgumentTypeInfosAccessor.getBY_CLASS().get(argumentClass);
        BY_TYPE_INFO.put(argumentTypeInfo, mapCodec);
        return new CommandArgumentType(argumentTypeInfo, mapCodec);
        //return Registry.register(DeclarativeUIBuiltInRegistries.COMMAND_ARGUMENT_TYPE, ResourceLocation.parse(name), new CommandArgumentType(argumentTypeInfo, mapCodec));
    }

    public static void bootStrap() {
    }

    static {
        TYPED_CODEC = BuiltInRegistries.COMMAND_ARGUMENT_TYPE.byNameCodec().dispatch(CommandArgument::getType, BY_TYPE_INFO::get);

        registerStable(BoolArgumentType.class, BoolArgumentType.bool());
        registerStable(GameProfileArgument.class, GameProfileArgument.gameProfile());
        registerStable(BlockPosArgument.class, BlockPosArgument.blockPos());
        registerStable(ColumnPosArgument.class, ColumnPosArgument.columnPos());
        registerStable(Vec3Argument.class, Vec3Argument.vec3());
        registerStable(Vec2Argument.class, Vec2Argument.vec2());
        registerContextAware(BlockStateArgument.class, BlockStateArgument::new);
        registerContextAware(BlockPredicateArgument.class, BlockPredicateArgument::new);
        registerContextAware(ItemArgument.class, ItemArgument::new);
        registerContextAware(ItemPredicateArgument.class, ItemPredicateArgument::new);
        registerStable(ColorArgument.class, ColorArgument.color());
        registerStable(HexColorArgument.class, HexColorArgument.hexColor());
        registerContextAware(ComponentArgument.class, ComponentArgument::textComponent);
        registerContextAware(StyleArgument.class, StyleArgument::style);
        registerStable(MessageArgument.class, MessageArgument.message());
        registerStable(CompoundTagArgument.class, CompoundTagArgument.compoundTag());
        registerStable(NbtTagArgument.class, NbtTagArgument.nbtTag());
        registerStable(NbtPathArgument.class, NbtPathArgument.nbtPath());
        registerStable(ObjectiveArgument.class, ObjectiveArgument.objective());
        registerStable(ObjectiveCriteriaArgument.class, ObjectiveCriteriaArgument.criteria());
        registerStable(OperationArgument.class, OperationArgument.operation());
        registerContextAware(ParticleArgument.class, ParticleArgument::new);
        registerStable(AngleArgument.class, AngleArgument.angle());
        registerStable(RotationArgument.class, RotationArgument.rotation());
        registerStable(ScoreboardSlotArgument.class, ScoreboardSlotArgument.displaySlot());
        registerStable(SwizzleArgument.class, SwizzleArgument.swizzle());
        registerStable(TeamArgument.class, TeamArgument.team());
        registerStable(SlotArgument.class, SlotArgument.slot());
        registerStable(SlotsArgument.class, SlotsArgument.slots());
        registerStable(ResourceLocationArgument.class, ResourceLocationArgument.id());
        registerStable(FunctionArgument.class, FunctionArgument.functions());
        registerStable(EntityAnchorArgument.class, EntityAnchorArgument.anchor());
        registerStable(RangeArgument.Ints.class, RangeArgument.intRange());
        registerStable(RangeArgument.Floats.class, RangeArgument.floatRange());
        registerStable(DimensionArgument.class, DimensionArgument.dimension());
        registerStable(GameModeArgument.class, GameModeArgument.gameMode());
        registerStable(TemplateMirrorArgument.class, TemplateMirrorArgument.templateMirror());
        registerStable(TemplateRotationArgument.class, TemplateRotationArgument.templateRotation());
        registerStable(HeightmapTypeArgument.class, HeightmapTypeArgument.heightmap());
        registerContextAware(ResourceOrIdArgument.LootTableArgument.class, ResourceOrIdArgument::lootTable);
        registerContextAware(ResourceOrIdArgument.LootPredicateArgument.class, ResourceOrIdArgument::lootPredicate);
        registerContextAware(ResourceOrIdArgument.LootModifierArgument.class, ResourceOrIdArgument::lootModifier);
        registerContextAware(ResourceOrIdArgument.DialogArgument.class, ResourceOrIdArgument::dialog);
        registerStable(UuidArgument.class, UuidArgument.uuid());
        //ArgumentTypeInfos
    }
}
