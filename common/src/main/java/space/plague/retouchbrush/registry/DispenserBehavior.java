package space.plague.retouchbrush.registry;

import net.minecraft.core.BlockPos;
import net.minecraft.core.dispenser.BlockSource;
import net.minecraft.core.Direction;
import net.minecraft.core.dispenser.OptionalDispenseItemBehavior;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.decoration.Painting;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.DispenserBlock;
import net.minecraft.world.phys.AABB;

import org.jetbrains.annotations.NotNull;
import space.plague.retouchbrush.Main;
import space.plague.retouchbrush.config.ModConfig;
import space.plague.retouchbrush.util.PaintingCycleUtil;

public class DispenserBehavior {

    public static void register() {

        DispenserBlock.registerBehavior(Items.BRUSH, new OptionalDispenseItemBehavior() {
            @Override
            protected @NotNull ItemStack execute(BlockSource source, ItemStack stack) {

                ModConfig config =  Main.getConfig();

                if (!config.isEnableMod() || !config.isEnableDispenserBehavior()) {
                    this.setSuccess(false);
                    return super.execute(source, stack);
                }

                ServerLevel level = source.level();

                Direction direction = source.blockEntity().getBlockState().getValue(DispenserBlock.FACING);
                BlockPos targetPos = source.pos().relative(direction);

                AABB searchBox = new AABB(targetPos);
                java.util.List<Painting> paintings = level.getEntitiesOfClass(Painting.class, searchBox);

                if (!paintings.isEmpty()) {
                    Painting targetPainting = paintings.get(0);

                    PaintingCycleUtil.CycleTo mode;
                    try {
                        mode = PaintingCycleUtil.CycleTo.valueOf(config.getCycleToDispenser());
                    }
                    catch (IllegalArgumentException e) {
                        LOGGER.warn("[" + Main.MOD_NAME + "] Invalid painting dispenser cycle to: " + config.getCycleToDispenser());
                        LOGGER.info("[" + Main.MOD_NAME + "] Setting painting dispenser cycle to: " + PaintingCycleUtil.CycleTo.SEQUENTIAL.getCode());
                        config.setCycleToDispenser(PaintingCycleUtil.CycleTo.SEQUENTIAL.getCode());
                        mode = PaintingCycleUtil.CycleTo.SEQUENTIAL;
                    }

                    boolean success = PaintingCycleUtil.cyclePainting(targetPainting, level, mode, config.isKeepSizeDispenser());

                    if (!success) {
                        this.setSuccess(false);
                        return super.execute(source, stack);
                    }

                    if (config.isEnableDispenserDamage()) {
                        stack.hurtAndBreak(1, level, (ServerPlayer)null, (item) -> {});
                    }

                    this.setSuccess(true);
                    return stack;

                }

                this.setSuccess(false);
                return super.execute(source, stack);
            }
        });

    }

}
