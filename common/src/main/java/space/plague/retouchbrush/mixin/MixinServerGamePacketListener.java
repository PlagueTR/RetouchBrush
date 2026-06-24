package space.plague.retouchbrush.mixin;

import net.minecraft.network.protocol.game.ServerboundInteractPacket;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.network.ServerGamePacketListenerImpl;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.decoration.painting.Painting;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.GameType;
import net.minecraft.world.phys.Vec3;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import space.plague.retouchbrush.Main;
import space.plague.retouchbrush.config.ModConfig;
import space.plague.retouchbrush.util.PaintingCycleUtil;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Mixin(ServerGamePacketListenerImpl.class)
public class MixinServerGamePacketListener {

    @Shadow
    public ServerPlayer player;

    private static final Map<UUID, Long> LAST_INTERACTION_TICK = new HashMap<>();

    @Inject(method = "handleInteract(Lnet/minecraft/network/protocol/game/ServerboundInteractPacket;)V", at = @At("HEAD"), cancellable = true)
    private void handleInteract(ServerboundInteractPacket packet, CallbackInfo ci) {
        packet.dispatch(new ServerboundInteractPacket.Handler() {
            @Override
            public void onInteraction(InteractionHand hand) {
                Entity entity = packet.getTarget(player.level());

                if (!(entity instanceof Painting painting) || !player.getItemInHand(hand).is(Items.BRUSH)) {
                    return;
                }

                UUID playerUUID = player.getUUID();
                long currentTick = player.level().getGameTime();

                if (LAST_INTERACTION_TICK.containsKey(playerUUID)) {
                    long lastTick = LAST_INTERACTION_TICK.get(playerUUID);
                    if (currentTick - lastTick < 1) {
                        ci.cancel();
                        return;
                    }
                }

                ModConfig config = Main.getConfig();
                if (!config.isEnableMod() || !config.isEnableUseBehavior()) {
                    return;
                }

                GameType gamemode = player.gameMode.getGameModeForPlayer();
                if (gamemode == GameType.SPECTATOR){
                    return;
                }

                PaintingCycleUtil.CycleTo mode;
                try {
                    mode = PaintingCycleUtil.CycleTo.valueOf(config.getCycleToUse());
                } catch (IllegalArgumentException e) {
                    Main.LOGGER.warn("[" + Main.MOD_NAME + "] Invalid painting use cycle to: " + config.getCycleToUse());
                    Main.LOGGER.info("[" + Main.MOD_NAME + "] Setting painting use cycle to: " + PaintingCycleUtil.CycleTo.SEQUENTIAL.getCode());
                    config.setCycleToUse(PaintingCycleUtil.CycleTo.SEQUENTIAL.getCode());
                    mode = PaintingCycleUtil.CycleTo.SEQUENTIAL;
                }

                boolean success = PaintingCycleUtil.cyclePainting(painting, player.level(), mode, config.isKeepSizeUse());
                if (!success) {
                    ci.cancel();
                    return;
                }

                LAST_INTERACTION_TICK.put(playerUUID, currentTick);

                if (!player.isCreative() && config.isEnableUseDamage()) {
                    if (hand == InteractionHand.MAIN_HAND) {
                        player.getItemInHand(hand).hurtAndBreak(1, player, EquipmentSlot.MAINHAND);
                    }
                    else {
                        player.getItemInHand(hand).hurtAndBreak(1, player, EquipmentSlot.OFFHAND);
                    }
                }
                ci.cancel();

            }

            @Override
            public void onInteraction(InteractionHand interactionHand, Vec3 vec3) { }

            @Override
            public void onAttack() { }
        });
    }

}
