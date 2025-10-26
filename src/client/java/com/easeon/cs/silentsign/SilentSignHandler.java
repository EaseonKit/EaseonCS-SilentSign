package com.easeon.cs.silentsign;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.screen.ingame.HangingSignEditScreen;
import net.minecraft.client.gui.screen.ingame.SignEditScreen;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.SignItem;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.hit.HitResult;
import net.minecraft.world.World;

@Environment(EnvType.CLIENT)
public class SilentSignHandler {
    private static boolean isInstallationPhase = false;

    public static ActionResult register(ClientPlayerEntity player, World world, Hand hand, BlockHitResult hitResult) {
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
                isInstallationPhase = player.isSneaking();
            } else {
                isInstallationPhase = true;
            }
        }

        return ActionResult.PASS;
    }

    public static void screen(MinecraftClient client, Screen screen, int scaledWidth, int scaledHeight)
    {
        if (!(screen instanceof SignEditScreen || screen instanceof HangingSignEditScreen)) return;

        if (isInstallationPhase) {
            screen.close();
            isInstallationPhase = false;
        }
    }
}
