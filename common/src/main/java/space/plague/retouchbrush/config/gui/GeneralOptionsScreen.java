package space.plague.retouchbrush.config.gui;

import me.shedaniel.clothconfig2.api.ConfigBuilder;
import me.shedaniel.clothconfig2.api.ConfigCategory;
import me.shedaniel.clothconfig2.api.ConfigEntryBuilder;

import net.minecraft.client.Minecraft;
import net.minecraft.network.chat.Component;

import space.plague.retouchbrush.Main;
import space.plague.retouchbrush.config.ModConfig;
import space.plague.retouchbrush.util.PaintingCycleUtil;

public class GeneralOptionsScreen {

    public static ConfigBuilder getConfigBuilder() {

        ModConfig defaults = new ModConfig();

        ConfigBuilder builder = ConfigBuilder.create()
                .setParentScreen(Minecraft.getInstance().screen)
                .setTitle(Component.literal(Main.MOD_NAME + " - General"));

        builder.setSavingRunnable(Main::saveConfig);

        ConfigEntryBuilder entryBuilder = builder.entryBuilder();

        ConfigCategory general = builder.getOrCreateCategory(Component.literal("General"));

        general.addEntry(entryBuilder.startBooleanToggle(Component.literal("Enable Retouch Brush"), Main.getConfig().isEnableMod())
                .setDefaultValue(defaults.isEnableMod())
                .setTooltip(Component.literal("Master switch to turn all mod features on or off."))
                .setSaveConsumer(newValue -> { Main.getConfig().setEnableMod(newValue); })
                .build());

        String[] options = new String[] {
                PaintingCycleUtil.CycleTo.NEXT.getCode(),
                PaintingCycleUtil.CycleTo.RANDOM.getCode()
        };
        general.addEntry(entryBuilder.startSelector(Component.literal("Painting Cycle Mode"), options, Main.getConfig().getCycleTo())
                .setDefaultValue(defaults.getCycleTo())
                .setTooltip(Component.literal("Determines if the brush cycles through paintings sequentially or randomly."))
                .setSaveConsumer(newValue -> { Main.getConfig().setCycleTo(newValue); })
                .build());

        general.addEntry(entryBuilder.startBooleanToggle(Component.literal("Match Same Size Only"), Main.getConfig().isKeepSize())
                .setDefaultValue(defaults.isKeepSize())
                .setTooltip(Component.literal("If enabled, the brush will only cycle through paintings that share the exact same dimensions."))
                .setSaveConsumer(newValue -> { Main.getConfig().setKeepSize(newValue); })
                .build());

        general.addEntry(entryBuilder.startBooleanToggle(Component.literal("Enable Dispenser Usage"), Main.getConfig().isEnableDispenserBehavior())
                .setDefaultValue(defaults.isEnableDispenserBehavior())
                .setTooltip(Component.literal("Allows dispensers to use the brush on a painting."))
                .setSaveConsumer(newValue -> { Main.getConfig().setEnableDispenserBehavior(newValue); })
                .build());

        general.addEntry(entryBuilder.startBooleanToggle(Component.literal("Enable Player Usage"), Main.getConfig().isEnableRightClickBehavior())
                .setDefaultValue(defaults.isEnableRightClickBehavior())
                .setTooltip(Component.literal("Allows players to manually use the brush on a painting."))
                .setSaveConsumer(newValue -> { Main.getConfig().setEnableRightClickBehavior(newValue); })
                .build());

        general.addEntry(entryBuilder.startBooleanToggle(Component.literal("Player Use Damages Tool"), Main.getConfig().isEnableUseDamage())
                .setDefaultValue(defaults.isEnableUseDamage())
                .setTooltip(Component.literal("Reduces brush durability when manually used by a player."))
                .setSaveConsumer(newValue -> { Main.getConfig().setEnableUseDamage(newValue); })
                .build());

        general.addEntry(entryBuilder.startBooleanToggle(Component.literal("Dispenser  Use Damages Tool"), Main.getConfig().isEnableDispenserUseDamage())
                .setDefaultValue(defaults.isEnableDispenserUseDamage())
                .setTooltip(Component.literal("Reduces brush durability when triggered automatically inside a dispenser."))
                .setSaveConsumer(newValue -> { Main.getConfig().setEnableDispenserUseDamage(newValue); })
                .build());

        builder.transparentBackground();

        return builder;
    }

}