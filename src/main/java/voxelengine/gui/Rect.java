package voxelengine.gui;

import org.joml.Vector2i;
import org.joml.Vector4f;
import voxelengine.Renderer;
import voxelengine.engine.DisplayManager;


/**
 * @author gabed
 * @Date 10/16/2022
 */

public class Rect implements UIElement {
    private final ScreenConstraint constraints;
    private final Vector2i size;
    private final Vector4f color;
    int vao = -1;

    public Rect(ScreenConstraint constraints, Vector2i size, Vector4f color) {
        this.constraints = constraints;
        this.size = size;
        this.color = color;
    }

    @Override
    public boolean Render() {
        Renderer.getUiShader().start();
        Renderer.getUiShader().loadUIColor(color);
        Renderer.getUiShader().loadViewportSize(new Vector2i(DisplayManager.WIDTH, DisplayManager.HEIGHT));
        DrawRect();
        Renderer.getUiShader().stop();
        return true;
    }

    @Override
    public int getVAO() {
        if (vao == -1)
            vao = createQuad(constraints.calculatePixelPosition(size), size);
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
