package com.easeon.cs.silentsign;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.screen.v1.ScreenEvents;
import net.fabricmc.fabric.api.event.player.UseBlockCallback;
import net.minecraft.client.gui.screen.ingame.HangingSignEditScreen;
import net.minecraft.client.gui.screen.ingame.SignEditScreen;
import net.minecraft.item.Item;
import net.minecraft.item.SignItem;
import net.minecraft.util.ActionResult;
import net.minecraft.util.hit.HitResult;

@Environment(EnvType.CLIENT)
public class ItemSignEditBlockerHandler {
    private static boolean isInstallationPhase = false;

    public static void register() {
        UseBlockCallback.EVENT.register((player, world, hand, hitResult) -> {
            if (!world.isClient()) return ActionResult.PASS;

            Item held = player.getStackInHand(hand).getItem();

            if (!(held instanceof SignItem)) return ActionResult.PASS;

            if (hitResult.getType() == HitResult.Type.BLOCK) {
                var pos = hitResult.getBlockPos();
                var state = world.getBlockState(pos);
                var block = state.getBlock();

                boolean isExistingSign =
                    block instanceof net.minecraft.block.SignBlock ||
                    block instanceof net.minecraft.block.WallSignBlock ||
                    block instanceof net.minecraft.block.HangingSignBlock ||
                    block instanceof net.minecraft.block.WallHangingSignBlock;

                if (isExistingSign) {
                    if (player.isSneaking()) {
                        isInstallationPhase = true;
                    } else {
                        isInstallationPhase = false;
                    }
                } else {
                    isInstallationPhase = true;
                }
            }

            return ActionResult.PASS;
        });
        ScreenEvents.AFTER_INIT.register((client, screen, scaledWidth, scaledHeight) -> {
            if (!(screen instanceof SignEditScreen || screen instanceof HangingSignEditScreen)) return;

            if (isInstallationPhase) {
                screen.close();
                isInstallationPhase = false;
            }
        });
    }
}
