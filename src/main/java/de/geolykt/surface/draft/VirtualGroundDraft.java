package de.geolykt.surface.draft;

import org.slf4j.LoggerFactory;

import de.geolykt.surface.ui.ASDraw;

import info.flowersoft.theotown.draft.GroundDraft;
import info.flowersoft.theotown.map.Drawer;
import info.flowersoft.theotown.resources.Drafts;
import io.blueflower.stapel2d.drawing.Color;
import io.blueflower.stapel2d.drawing.Colors;

public class VirtualGroundDraft extends GroundDraft {

    public static final VirtualGroundDraft VIRTUAL_GRASS;
    public static final VirtualGroundDraft VIRTUAL_CONCRETE;

    private final Color color;

    public VirtualGroundDraft(final Color color, final String id) {
        this.color = color;
        super.color = color;
        this.mapColor = color;
        this.mapColorWinter = color;
        this.id = id;
        this.author = "Geolykt";

        this.frames = new int[0];
        this.edgeFrames = new int[0];
        this.edgeBorderFrames = new int[0];
        this.iconFrames = new int[0];
        this.previewFrames = new int[0];
        this.texture = new int[0];
        this.isWater = false;
    }

    public void drawFlat(Drawer d, int baseY) {
        int r = d.engine.getRed();
        int g = d.engine.getGreen();
        int b = d.engine.getBlue();
        d.engine.setColor(color);
        int x0 = 0;
        int x1 = 16;
        int x2 = 32;
        int y0 = baseY;
        int y1 = baseY + 8;
        int y2 = baseY + 16;
        ASDraw.triangle(d, x0, y1, x2, y1, x1, y0); // Lower half
        ASDraw.triangle(d, x0, y1, x2, y1, x1, y2); // Upper half
        d.engine.setColor(r, g, b);
    }

    public void drawSloped(Drawer d, int baseY, boolean slopeU, boolean slopeR, boolean slopeD, boolean slopeL, int slopeHeight) {
        // Note: this method will not draw vertical rifts (i.e. slopes to the left and right) correctly.
        // Instead, call #drawVerticalRift should that constellation occur
        int r = d.engine.getRed();
        int g = d.engine.getGreen();
        int b = d.engine.getBlue();
        d.engine.setColor(color);
        int x0 = 0;
        int x1 = 16;
        int x2 = 32;

        int yU = baseY + 16;
        if (slopeU) {
            yU += slopeHeight;
        }
        int yR = baseY + 8;
        if (slopeR) {
            yR += slopeHeight;
        }
        int yD = baseY;
        if (slopeD) {
            yD += slopeHeight;
        }
        int yL = baseY + 8;
        if (slopeL) {
            yL += slopeHeight;
        }

        ASDraw.triangle(d, x0, yL, x2, yR, x1, yD); // Lower half
        ASDraw.triangle(d, x0, yL, x2, yR, x1, yU); // Upper half
        d.engine.setColor(r, g, b);
    }

    public void drawVerticalRift(Drawer d, int baseY, int slopeHeight) {
        // Basically a call to #drawSloped(d, baseY, false, true, false, true, slopeHeight);
        // However this method has correct math for that constellation of slopes.
        // The difference really is how the triangles are ordered.
        // Normally there are two triangles, one occupying the upper half, one occupying the lower half.
        // For vertical rifts the two triangles cannot be ordered like that, so instead they occupy the left
        // half and the right half.
        // The "normal" method cannot order the triangles like that without causing a similar issue
        // when it comes to other formations. - I believe that at least
        int r = d.engine.getRed();
        int g = d.engine.getGreen();
        int b = d.engine.getBlue();
        d.engine.setColor(color);

        int x0 = 0;
        int x1 = 16;
        int x2 = 32;

        int y0 = baseY;
        int y1 = baseY + 16;
        int y2 = baseY + 8 + slopeHeight;

        ASDraw.triangle(d, x0, y2, x1, y1, x1, y0); // Left half
        ASDraw.triangle(d, x2, y2, x1, y1, x1, y0); // Right half

        d.engine.setColor(r, g, b);
    }

    static {
        VIRTUAL_CONCRETE = new VirtualGroundDraft(Colors.GRAY, "authentic_surfaces:synthetic:ground_concrete");
        VIRTUAL_GRASS = new VirtualGroundDraft(Colors.GREEN, "authentic_surfaces:synthetic:ground_grass");
    }

    private void register() {
        if (Drafts.ALL.containsKey(this.id)) {
            LoggerFactory.getLogger(getClass()).error("Attempted to register two drafts with the same id of \"{}\"", this.id);
            return;
        }
        Drafts.GROUNDS.add(this);
        Drafts.ALL.put(this.id, this);
    }

    public static void registerAll() {
        VIRTUAL_CONCRETE.register();
        VIRTUAL_GRASS.register();
    }
}
