package de.geolykt.surface.mixins;

import java.util.List;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Desc;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

import de.geolykt.surface.accessor.CityCreatorAccessor;
import de.geolykt.surface.draft.VirtualGroundDraft;

import info.flowersoft.theotown.creation.CityCreator;
import info.flowersoft.theotown.draft.GroundDraft;
import info.flowersoft.theotown.resources.Drafts;

@Mixin(CityCreator.class)
public class CityCreatorMixins implements CityCreatorAccessor {

    @Unique
    private boolean authenticSurfaces;

    @Override
    public void setAuthenticSurfaces(boolean authenticSurfaces) {
        this.authenticSurfaces = authenticSurfaces;
    }

    @Inject(at = @At("HEAD"), target = @Desc(owner = CityCreator.class, value = "collectTiles",
            args = {String.class, List.class, List.class, List.class}, ret = void.class),
            require = 1,
            cancellable = true,
            locals = LocalCapture.NO_CAPTURE)
    private void onCollectTiles(String seed, List<GroundDraft> waterTiles, List<GroundDraft> landTiles,
            List<GroundDraft> spawnableTile, CallbackInfo ci) {
        if (!authenticSurfaces) {
            return;
        }
        for(int i = 0; i < Drafts.WATER_TILES.size(); ++i) {// 822
            GroundDraft draft = Drafts.WATER_TILES.get(i);// 823
            if (draft.autoBuild) {// 824
                waterTiles.add(draft);
            }
        }
        landTiles.add(VirtualGroundDraft.VIRTUAL_GRASS);
        ci.cancel();
    }
}
