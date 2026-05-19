package eu.cj4.declarativeui.impl.command.argument;

import com.mojang.brigadier.arguments.*;
import com.mojang.serialization.Codec;
import eu.cj4.declarativeui.api.codec.LazyEnumCodec;
import eu.cj4.declarativeui.mixin.argument.*;
import net.minecraft.commands.arguments.*;
import net.minecraft.commands.arguments.blocks.BlockPredicateArgument;
import net.minecraft.commands.arguments.blocks.BlockStateArgument;
import net.minecraft.commands.arguments.coordinates.*;
import net.minecraft.commands.arguments.item.FunctionArgument;
import net.minecraft.commands.arguments.item.ItemArgument;
import net.minecraft.commands.arguments.item.ItemPredicateArgument;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;

import static eu.cj4.declarativeui.api.command.argument.CommandArgumentType.*;

public final class CommandArgumentTypes {
    private static final Codec<StringArgumentType.StringType> STRING_TYPE_CODEC = LazyEnumCodec.fromEnum(StringArgumentType.StringType.values());

    public static void bootStrap() {
    }

    static {
        registerStable(BoolArgumentType.class, BoolArgumentType.bool());
        registerCodec2(FloatArgumentType.class, FloatArgumentType::floatArg, Codec.FLOAT.optionalFieldOf("min", -Float.MAX_VALUE), Codec.FLOAT.optionalFieldOf("max", Float.MAX_VALUE));
        registerCodec2(DoubleArgumentType.class, DoubleArgumentType::doubleArg, Codec.DOUBLE.optionalFieldOf("min", -Double.MAX_VALUE), Codec.DOUBLE.optionalFieldOf("max", Double.MAX_VALUE));
        registerCodec2(IntegerArgumentType.class, IntegerArgumentType::integer, Codec.INT.optionalFieldOf("min", Integer.MIN_VALUE), Codec.INT.optionalFieldOf("max", Integer.MAX_VALUE));
        registerCodec2(LongArgumentType.class, LongArgumentType::longArg, Codec.LONG.optionalFieldOf("min", Long.MIN_VALUE), Codec.LONG.optionalFieldOf("max", Long.MAX_VALUE));
        registerCodec1(StringArgumentType.class, StringArgumentTypeAccessor::create, STRING_TYPE_CODEC.fieldOf("variant"));
        registerCodec2(EntityArgument.class, EntityArgumentAccessor::create, Codec.BOOL.fieldOf("single"), Codec.BOOL.fieldOf("players_only"));
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
        registerCodec1(ScoreHolderArgument.class, ScoreHolderArgument::new, Codec.BOOL.fieldOf("multiple"));
        registerStable(SwizzleArgument.class, SwizzleArgument.swizzle());
        registerStable(TeamArgument.class, TeamArgument.team());
        registerStable(SlotArgument.class, SlotArgument.slot());
        registerStable(SlotsArgument.class, SlotsArgument.slots());
        registerStable(IdentifierArgument.class, IdentifierArgument.id());
        registerStable(FunctionArgument.class, FunctionArgument.functions());
        registerStable(EntityAnchorArgument.class, EntityAnchorArgument.anchor());
        registerStable(RangeArgument.Ints.class, RangeArgument.intRange());
        registerStable(RangeArgument.Floats.class, RangeArgument.floatRange());
        registerStable(DimensionArgument.class, DimensionArgument.dimension());
        registerStable(GameModeArgument.class, GameModeArgument.gameMode());
        registerCodec1(TimeArgument.class, TimeArgument::time, Codec.INT.optionalFieldOf("min", 0));

        ResourceKey<? extends Registry<Registry<Object>>> rootRegistry = ResourceKey.createRegistryKey(Registries.ROOT_REGISTRY_NAME);
        Codec<ResourceKey<Registry<Object>>> resourceKeyCodec = ResourceKey.codec(rootRegistry);
        registerCodec1((Class) ResourceOrTagArgument.class, (buildContext, resourceKey) -> ResourceOrTagArgument.resourceOrTag(buildContext, resourceKey), resourceKeyCodec.fieldOf("registry"));
        registerCodec1((Class) ResourceOrTagKeyArgument.class, resourceKey -> ResourceOrTagKeyArgument.resourceOrTagKey(resourceKey), resourceKeyCodec.fieldOf("registry"));
        registerCodec1((Class) ResourceArgument.class, (buildContext, resourceKey) -> ResourceArgument.resource(buildContext, resourceKey), resourceKeyCodec.fieldOf("registry"));
        registerCodec1((Class) ResourceKeyArgument.class, resourceKey -> ResourceKeyArgument.key(resourceKey), resourceKeyCodec.fieldOf("registry"));
        registerCodec1((Class) ResourceSelectorArgument.class, (buildContext, resourceKey) -> ResourceSelectorArgument.resourceSelector(buildContext, resourceKey), resourceKeyCodec.fieldOf("registry"));

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
