package voxelengine.gui;

import org.joml.Vector2i;
import voxelengine.Renderer;


/**
 * @author gabed
 * @Date 10/16/2022
 */

public class UVImage implements UIElement {
    private final ScreenConstraint constraints;
    private final Vector2i size;
    private final int textureId;
    private final float[] uvs;
    int vao = -1;

    public UVImage(ScreenConstraint constraints, int textureId, Vector2i size, float[] uvs) {
        this.constraints = constraints;
        this.size = size;
        this.textureId = textureId;
        this.uvs = uvs;
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
            vao = createUVImageQuad(constraints.calculatePixelPosition(size), size, uvs);
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
