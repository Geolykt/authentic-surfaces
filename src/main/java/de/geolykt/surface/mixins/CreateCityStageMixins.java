package de.geolykt.surface.mixins;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Desc;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

import de.geolykt.surface.accessor.CityGeneratorAccessor;
import de.geolykt.surface.accessor.CreateCityStageAccessor;
import de.geolykt.surface.ui.GenerateCitySetAuthenticSurfaceButton;

import info.flowersoft.theotown.stages.CreateCityStageExt;
import info.flowersoft.theotown.ui.Page;
import info.flowersoft.theotown.util.CityGenerator;
import info.flowersoft.theotown.util.LineLayoutFiller;
import io.blueflower.stapel2d.gui.Panel;

@Mixin(CreateCityStageExt.class)
public class CreateCityStageMixins implements CreateCityStageAccessor {

    @Shadow
    private CityGenerator generator;

    @Inject(at = @At("TAIL"), target = @Desc(owner = CreateCityStageExt.class, value = "enter", args = {}, ret = void.class),
            locals = LocalCapture.CAPTURE_FAILHARD)
    private void onEnter(CallbackInfo ci, Page page, Panel panel, LineLayoutFiller filler) {
        filler.addGadget(new GenerateCitySetAuthenticSurfaceButton(this, panel));
        filler.finalizeLine();
    }

    @Override
    public CityGeneratorAccessor getGenerator() {
        return (CityGeneratorAccessor) generator;
    }
}
