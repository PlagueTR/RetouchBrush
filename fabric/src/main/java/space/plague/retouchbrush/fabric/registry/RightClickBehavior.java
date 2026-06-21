package space.plague.retouchbrush.fabric.registry;

import net.fabricmc.fabric.api.event.player.UseEntityCallback;

import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.decoration.Painting;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.GameType;

import space.plague.retouchbrush.Main;
import space.plague.retouchbrush.config.ModConfig;
import space.plague.retouchbrush.util.PaintingCycleUtil;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class RightClickBehavior {

    // FIX: Vanilla clients/clients without the mod cause interactions to happen twice, so we throttle the interaction speed as a fix
    private static final Map<UUID, Long> LAST_INTERACTION_TICK = new HashMap<>();

    public static void register() {

        UseEntityCallback.EVENT.register(((player, world, hand, entity, hitResult) -> {


            if (!player.getItemInHand(hand).is(Items.BRUSH) || !(entity instanceof Painting painting)) {
                return InteractionResult.PASS;
            }

            if (world.isClientSide()){
                return InteractionResult.SUCCESS;
            }

            long currentTick = world.getGameTime();
            UUID playerUUID = player.getUUID();

            if (LAST_INTERACTION_TICK.containsKey(playerUUID)) {
                long lastTick = LAST_INTERACTION_TICK.get(playerUUID);
                if (currentTick - lastTick < 1) {
                    return InteractionResult.FAIL;
                }
            }

            ModConfig config = Main.getConfig();

            if (!config.isEnableMod() || !config.isEnableRightClickBehavior()) {
                return InteractionResult.PASS;
            }

            if (player instanceof ServerPlayer serverPlayer) {
                GameType gamemode = serverPlayer.gameMode.getGameModeForPlayer();
                if (gamemode == GameType.SPECTATOR || gamemode == GameType.ADVENTURE) {
                    return InteractionResult.PASS;
                }
            }

            PaintingCycleUtil.CycleTo mode;
            try {
                mode = PaintingCycleUtil.CycleTo.valueOf(config.getCycleTo());
            }
            catch (IllegalArgumentException e) {
                Main.LOGGER.warn("[" + Main.MOD_NAME + "] Invalid painting cycle to: " + config.getCycleTo());
                Main.LOGGER.info("[" + Main.MOD_NAME + "] Setting painting cycle to: " + PaintingCycleUtil.CycleTo.NEXT.getCode());
                config.setCycleTo(PaintingCycleUtil.CycleTo.NEXT.getCode());
                mode = PaintingCycleUtil.CycleTo.NEXT;
            }

            boolean success = PaintingCycleUtil.cyclePainting(painting, world, mode, config.isKeepSize());

            if (!success) {
                return InteractionResult.PASS;
            }

            LAST_INTERACTION_TICK.put(playerUUID, currentTick);

            if (!player.isCreative() && config.isEnableUseDamage()) {
                player.getItemInHand(hand).hurtAndBreak(1, player, p -> p.broadcastBreakEvent(hand));
            }

            return InteractionResult.SUCCESS;

        }));

    }

}
