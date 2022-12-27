package de.geolykt.surface.ui;

import de.geolykt.surface.accessor.CreateCityStageAccessor;

import info.flowersoft.theotown.resources.Resources;
import info.flowersoft.theotown.ui.IconRadioButton;
import io.blueflower.stapel2d.gui.Gadget;

public class GenerateCitySetAuthenticSurfaceButton extends IconRadioButton {

    private final CreateCityStageAccessor createCityStage;

    public GenerateCitySetAuthenticSurfaceButton(CreateCityStageAccessor createCityStage, Gadget parent) {
        super(getIcon(createCityStage), "Use Authentic Surfaces", parent);
        this.createCityStage = createCityStage;
    }

    @Override
    public void onClick() {
        super.onClick();
        this.createCityStage.getGenerator().setAuthenticSurfaces(!this.createCityStage.getGenerator().getAuthenticSurfaces());
        super.icon.setFrame(getIcon(this.createCityStage));
    }

    @Override
    public boolean isPressed() {
        return this.createCityStage.getGenerator().getAuthenticSurfaces();
    }

    private static int getIcon(CreateCityStageAccessor accessor) {
        return accessor.getGenerator().getAuthenticSurfaces() ? Resources.FRAME_CHECKBOX_ON : Resources.FRAME_CHECKBOX_OFF;
    }
}
