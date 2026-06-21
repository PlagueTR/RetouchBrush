package space.plague.retouchbrush.registry;

import net.minecraft.core.BlockPos;
import net.minecraft.core.BlockSource;
import net.minecraft.core.Direction;
import net.minecraft.core.dispenser.OptionalDispenseItemBehavior;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.decoration.Painting;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.DispenserBlock;
import net.minecraft.world.phys.AABB;

import space.plague.retouchbrush.Main;
import space.plague.retouchbrush.config.ModConfig;
import space.plague.retouchbrush.util.PaintingCycleUtil;

public class DispenserBehavior {

    public static void register() {

        DispenserBlock.registerBehavior(Items.BRUSH, new OptionalDispenseItemBehavior() {
            @Override
            protected ItemStack execute(BlockSource source, ItemStack stack) {

                ModConfig config =  Main.getConfig();

                if (!config.isEnableMod() || !config.isEnableDispenserBehavior()) {
                    this.setSuccess(false);
                    return super.execute(source, stack);
                }

                Level level = source.getLevel();

                Direction direction = source.getBlockState().getValue(DispenserBlock.FACING);
                BlockPos targetPos = source.getPos().relative(direction);

                AABB searchBox = new AABB(targetPos);
                java.util.List<Painting> paintings = level.getEntitiesOfClass(Painting.class, searchBox);

                if (!paintings.isEmpty()) {
                    Painting targetPainting = paintings.get(0);

                    PaintingCycleUtil.CycleTo mode;
                    try {
                        mode = PaintingCycleUtil.CycleTo.valueOf(config.getCycleTo());
                    }
                    catch (IllegalArgumentException e) {
                        LOGGER.warn("[" + Main.MOD_NAME + "] Invalid painting cycle to: " + config.getCycleTo());
                        LOGGER.info("[" + Main.MOD_NAME + "] Setting painting cycle to: " + PaintingCycleUtil.CycleTo.NEXT.getCode());
                        config.setCycleTo(PaintingCycleUtil.CycleTo.NEXT.getCode());
                        mode = PaintingCycleUtil.CycleTo.NEXT;
                    }

                    boolean success = PaintingCycleUtil.cyclePainting(targetPainting, level, mode, config.isKeepSize());

                    if (!success) {
                        this.setSuccess(false);
                        return super.execute(source, stack);
                    }

                    if (config.isEnableDispenserUseDamage()) {
                        stack.hurt(1, level.random, (ServerPlayer)null);
                        if (stack.getDamageValue() >= stack.getMaxDamage()) {
                            stack.shrink(1);
                        }
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
