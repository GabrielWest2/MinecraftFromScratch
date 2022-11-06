package voxelengine.gui;

import org.joml.Vector2i;
import voxelengine.Renderer;


/**
 * @author gabed
 * @Date 10/16/2022
 */

public class Image implements UIElement {
    private final ScreenConstraint constraints;
    private final Vector2i size;
    private final int textureId;
    int vao = -1;
    private boolean fullscreenX, fullscreenY, tileTex;

    public Image(ScreenConstraint constraints, int textureId, Vector2i size) {
        this.constraints = constraints;
        this.size = size;
        this.textureId = textureId;
    }


    public Image(ScreenConstraint constraints, int textureId, Vector2i size, boolean fullscreenX, boolean fullscreenY, boolean tileTex) {
        this.constraints = constraints;
        this.size = size;
        this.textureId = textureId;
        this.fullscreenX = fullscreenX;
        this.fullscreenY = fullscreenY;
        this.tileTex = tileTex;
    }

    @Override
    public boolean Render() {
        Renderer.getImageShader().start();

        DrawImage(textureId);

        Renderer.getImageShader().stop();
        return true;
    }

    @Override
    public int getVAO() {
        if (vao == -1) {
            vao = createImageQuad(constraints.calculatePixelPosition(size), size, fullscreenX, fullscreenY, tileTex);
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
