package voxelengine.gui;

import org.joml.Vector2i;
import voxelengine.Renderer;


/**
 * @author gabed
 * @Date 10/16/2022
 */

public class Crosshair implements UIElement {
    private final ScreenConstraint constraints;
    private final Vector2i size;
    private final int textureId;
    int vao = -1;


    public Crosshair(int textureId, Vector2i size) {
        this.constraints = new ScreenConstraint(null).addConstraint(Side.CENTER_X, "0px").addConstraint(Side.CENTER_Y, "0px");
        this.size = size;
        this.textureId = textureId;
    }

    @Override
    public boolean Render() {
        Renderer.getCrosshairShader().start();

        DrawImage(textureId);

        Renderer.getCrosshairShader().stop();
        return true;
    }

    @Override
    public int getVAO() {
        if (vao == -1) {
            vao = createImageQuad(constraints.calculatePixelPosition(size), size);
        }
        return vao;
    }

    @Override
    public void resetVAO() {
        vao = -1;
    }

    public ScreenConstraint getConstraints() {
        return constraints;
    }

    public Vector2i getSize() {
        return size;
    }
}
