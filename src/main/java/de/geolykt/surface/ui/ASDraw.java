package de.geolykt.surface.ui;

import info.flowersoft.theotown.map.Drawer;
import info.flowersoft.theotown.resources.Resources;
import io.blueflower.stapel2d.drawing.Engine;
import io.blueflower.stapel2d.drawing.Texture;

public class ASDraw {

    public static void quad(Engine engine, float x0, float y0, float x1, float y1, float x2, float y2, float x3, float y3) {
        Texture texture = Resources.IMAGE_WORLD.getTexture(Resources.FRAME_RECT);
        float[] uvs = Resources.IMAGE_WORLD.getUVStream();
        int index = Resources.IMAGE_WORLD.getUVStreamPos(Resources.FRAME_RECT);
        float u0 = uvs[index];
        float v0 = uvs[index + 1];
        float u1 = uvs[index + 2];
        float v1 = uvs[index + 3];
        engine.drawTextureRect(texture, x0, y0, x1, y1, x2, y2, x3, y3, u0, v0, u1, v1, 0);
    }

    public static void triangle(Engine engine, float x0, float y0, float x1, float y1, float x2, float y2) {
        Texture texture = Resources.IMAGE_WORLD.getTexture(Resources.FRAME_RECT);
        float[] uvs = Resources.IMAGE_WORLD.getUVStream();
        int index = Resources.IMAGE_WORLD.getUVStreamPos(Resources.FRAME_RECT);
        float u0 = uvs[index];
        float v0 = uvs[index + 1];
        float u1 = uvs[index + 2];
        float v1 = uvs[index + 3];
        engine.drawTextureTriangle(texture, x0, y0, x1, y1, x2, y2, u0, v0, u0, v1, u1, v0, 0);
    }

    public static void triangle(Drawer drawer, float x0, float y0, float x1, float y1, float x2, float y2) {
        // TODO shading, perhaps?
        drawer.drawTriangle(Resources.IMAGE_WORLD, Resources.FRAME_RECT, x0, y0, x1, y1, x2, y2, 0, 0, 0, 0, 0, 0);
//        Texture texture = Resources.IMAGE_WORLD.getTexture(Resources.FRAME_RECT);
//        float[] uvs = Resources.IMAGE_WORLD.getUVStream();
//        int index = Resources.IMAGE_WORLD.getUVStreamPos(Resources.FRAME_RECT);
//        float u0 = uvs[index];
//        float v0 = uvs[index + 1];
//        float u1 = uvs[index + 2];
//        float v1 = uvs[index + 3];
//        x0 = drawer.x + (drawer.x + x0) * drawer.scaleX;
//        x1 = drawer.x + (drawer.x + x1) * drawer.scaleX;
//        x2 = drawer.x + (drawer.x + x2) * drawer.scaleX;
//        y0 = drawer.y + (drawer.x + y0) * drawer.scaleY;
//        y1 = drawer.y + (drawer.x + y1) * drawer.scaleY;
//        y2 = drawer.y + (drawer.x + y2) * drawer.scaleY;
//        drawer.engine.drawTextureTriangle(texture, x0, y0, x1, y1, x2, y2, u0, v0, u0, v1, u1, v0, 0);
    }
}
