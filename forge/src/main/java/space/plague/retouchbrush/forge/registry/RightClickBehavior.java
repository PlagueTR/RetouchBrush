package space.plague.retouchbrush.forge.registry;

import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.Level;
import net.minecraft.world.entity.player.Player;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.GameType;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.item.Items;
import net.minecraft.world.entity.decoration.Painting;

import space.plague.retouchbrush.Main;
import space.plague.retouchbrush.config.ModConfig;
import space.plague.retouchbrush.util.PaintingCycleUtil;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Mod.EventBusSubscriber(modid = Main.MOD_ID)
public class RightClickBehavior {

    // PARITY: Vanilla clients/clients without the mod cause interactions to happen twice on Fabric
    // So we throttled the interaction speed as a fix
    private static final Map<UUID, Long> LAST_INTERACTION_TICK = new HashMap<>();

    @SubscribeEvent
    public static void onEntityInteract(PlayerInteractEvent.EntityInteract event) {

        Player player = event.getEntity();
        Level world = event.getLevel();
        InteractionHand hand = event.getHand();
        Entity entity = event.getTarget();

        if (!player.getItemInHand(hand).is(Items.BRUSH) || !(entity instanceof Painting painting)) {
            return;
        }

        if (world.isClientSide()) {
            event.setCancellationResult(InteractionResult.SUCCESS);
            event.setCanceled(true);
            return;
        }

        long currentTick = world.getGameTime();
        UUID playerUUID = player.getUUID();

        if (LAST_INTERACTION_TICK.containsKey(playerUUID)) {
            long lastTick = LAST_INTERACTION_TICK.get(playerUUID);
            if (currentTick - lastTick < 1) {
                event.setCancellationResult(InteractionResult.FAIL);
                event.setCanceled(true);
                return;
            }
        }

        ModConfig config = Main.getConfig();

        if (!config.isEnableMod() || !config.isEnableRightClickBehavior()) {
            return;
        }

        if (player instanceof ServerPlayer serverPlayer) {
            GameType gamemode = serverPlayer.gameMode.getGameModeForPlayer();
            if (gamemode == GameType.SPECTATOR || gamemode == GameType.ADVENTURE) {
                return;
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

        boolean success = PaintingCycleUtil.cyclePainting(painting, event.getLevel(), mode, config.isKeepSize());

        if (!success) {
            return;
        }

        LAST_INTERACTION_TICK.put(playerUUID, currentTick);

        if (!player.isCreative() && config.isEnableUseDamage()) {
            player.getItemInHand(hand).hurtAndBreak(1, player, p -> p.broadcastBreakEvent(hand));
        }

        event.setCancellationResult(InteractionResult.SUCCESS);
        event.setCanceled(true);

    }

}
