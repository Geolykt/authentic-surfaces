package de.geolykt.surface.mixins;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Desc;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import de.geolykt.surface.draft.VirtualGroundDraft;

import info.flowersoft.theotown.draft.GroundDraft;
import info.flowersoft.theotown.map.Drawer;
import info.flowersoft.theotown.map.objects.Ground;
import io.blueflower.stapel2d.drawing.Color;
import io.blueflower.stapel2d.drawing.Colors;
import io.blueflower.stapel2d.drawing.Engine;

@Mixin(Ground.class)
public class GroundMixins {
    @Shadow
    private static Color COLOR_BUF;
    @Shadow
    private GroundDraft draft;
    @Shadow
    private byte terrainHeights;
    @Shadow
    private byte baseTerrainHeight;

    @Unique
    private static final int SLOPE_L = 1 << 0;
    @Unique
    private static final int SLOPE_U = 1 << 1;
    @Unique
    private static final int SLOPE_R = 1 << 2;
    @Unique
    private static final int SLOPE_D = 1 << 3;
    @Unique
    private static final int RIFT_VERTICAL = SLOPE_L | SLOPE_R;

    // I don't quite know when this method gets called - whatever
    @Inject(at = @At("HEAD"), target = @Desc(owner = Ground.class, value = "drawFlat", args = {Drawer.class, int.class}, ret = void.class),
            cancellable = true)
    public void virtualDrawFlat(Drawer d, int level, CallbackInfo ci) {
        if (!(draft instanceof VirtualGroundDraft)) {
            return;
        }
        Engine engine = d.engine;

        int baseY = -level * 12;
        engine.setType(0); // I have no serious idea what this does
        ((VirtualGroundDraft) draft).drawFlat(d, baseY);
        engine.setColor(Colors.WHITE);
        ci.cancel();
    }

    @Inject(at = @At("HEAD"),
            target = @Desc(owner = Ground.class, value = "draw", args = {Drawer.class, GroundDraft.class, GroundDraft.class, GroundDraft.class}, ret = void.class),
            cancellable = true)
    public void virtualDraw(Drawer d, GroundDraft lower, GroundDraft upper, GroundDraft water, CallbackInfo ci) {
        if (!(draft instanceof VirtualGroundDraft)) {
            return;
        }
        Engine engine = d.engine;
        int baseY = -this.baseTerrainHeight * 12 - 7; // I don't quite understand why the grid is offset by 7 units exactly, but whatever.
        engine.setType(0); // No idea what this does, but seems important so it stays here

        if (this.terrainHeights == RIFT_VERTICAL) {
            ((VirtualGroundDraft) draft).drawVerticalRift(d, baseY, -12);
        } else {
            boolean slopeU = (this.terrainHeights & SLOPE_U) != 0;
            boolean slopeR = (this.terrainHeights & SLOPE_R) != 0;
            boolean slopeD = (this.terrainHeights & SLOPE_D) != 0;
            boolean slopeL = (this.terrainHeights & SLOPE_L) != 0;
            ((VirtualGroundDraft) draft).drawSloped(d, baseY, slopeU, slopeR, slopeD, slopeL, -12);
        }

        engine.setColor(Colors.WHITE);
        ci.cancel();
    }

    @Inject(at = @At("HEAD"),
            target = @Desc(owner = Ground.class, value = "drawEdge", args = {Drawer.class, int.class}, ret = void.class),
            cancellable = true)
    public void virtualDrawEdge(Drawer d, int dir, CallbackInfo ci) {
        if (!(draft instanceof VirtualGroundDraft)) {
            return;
        }
        // Edge drawing not supported as of now
        ci.cancel();
    }
}
