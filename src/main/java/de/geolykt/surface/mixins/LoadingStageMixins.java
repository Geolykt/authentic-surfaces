package de.geolykt.surface.mixins;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Desc;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import de.geolykt.surface.draft.VirtualGroundDraft;

import info.flowersoft.theotown.scripting.ScriptingEnvironment;
import info.flowersoft.theotown.stages.LoadingStage;

@Mixin(LoadingStage.class)
public class LoadingStageMixins {

    @Inject(at = @At(value = "INVOKE", desc = @Desc(owner = ScriptingEnvironment.class, value = "getInstance", args = {}, ret = ScriptingEnvironment.class)),
            target = @Desc(owner = LoadingStage.class, value = "loadDrafts", args = {}, ret = void.class),
            require = 1)
    public void onPostLoadDrafts(CallbackInfo ci) {
        VirtualGroundDraft.registerAll();
    }
}
