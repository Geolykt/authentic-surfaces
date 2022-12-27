package de.geolykt.surface.mixins;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Desc;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import de.geolykt.surface.accessor.CityGeneratorAccessor;

import info.flowersoft.theotown.util.CityGenerator;

@Mixin(CityGenerator.class)
public class CityGeneratorMixins implements CityGeneratorAccessor {

    @Unique
    private boolean authenticSurfaces;

    @Override
    public void setAuthenticSurfaces(boolean authenticSurfaces) {
        this.authenticSurfaces = authenticSurfaces;
    }

    @Override
    public boolean getAuthenticSurfaces() {
        return this.authenticSurfaces;
    }

    @Inject(at = @At("HEAD"), target = @Desc(owner = CityGenerator.class, value = "setTo", args = CityGenerator.class))
    public void onSetTo(CityGenerator other, CallbackInfo ci) {
        this.authenticSurfaces = ((CityGeneratorAccessor) other).getAuthenticSurfaces();
    }

    @Inject(at = @At("RETURN"), target = @Desc(owner = CityGenerator.class, value = "equals", args = Object.class, ret = boolean.class), cancellable = true)
    public void onEqual(Object other, CallbackInfoReturnable<Boolean> cir) {
        if (cir.getReturnValueZ()) {
            cir.setReturnValue(((CityGeneratorAccessor) other).getAuthenticSurfaces() == this.authenticSurfaces);
        }
    }
}
