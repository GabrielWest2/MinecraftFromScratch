package voxelengine.gui;

import org.joml.Vector2i;
import org.joml.Vector4f;
import voxelengine.Renderer;
import voxelengine.engine.DisplayManager;
import voxelengine.gui.callback.Callback;
import voxelengine.input.Mouse;


/**
 * @author gabed
 * @Date 10/16/2022
 */

public class Button implements UIElement {
    private final ScreenConstraint constraints;
    private final String text;
    private final Vector4f color;
    private final Text uiText;
    boolean wasPressed = false;
    int vao = -1;
    private Vector2i size;
    private Callback callback;

    public Button(ScreenConstraint constraints, String text, Vector2i size, Vector4f color) {
        this.constraints = constraints;
        this.text = text;
        this.size = size;
        this.color = color;
        uiText = new Text(new ScreenConstraint(this).addConstraint(Side.LEFT, (size.x / 2) - (Text.getStringSize(text, 25) / 2) + "px").addConstraint(Side.TOP, (size.y / 2) - (25 / 2) + "px"), text, 25, new Vector4f(1, 1, 1, 1));
    }

    public Button(ScreenConstraint constraints, String text, Vector2i size, Vector4f color, Callback callback) {
        this.constraints = constraints;
        this.text = text;
        this.size = size;
        this.color = color;
        this.callback = callback;
        uiText = new Text(new ScreenConstraint(this).addConstraint(Side.LEFT, (size.x / 2) - (Text.getStringSize(text, 25) / 2) + "px").addConstraint(Side.TOP, (size.y / 2) - (25 / 2) + "px"), text, 25, new Vector4f(1, 1, 1, 1));
    }

    @Override
    public boolean Render() {
        Renderer.getUiShader().start();
        if (isInBoundingBox(Mouse.getMousePosInt()) && !isObstructed(Mouse.getMousePosInt())) {
            if (!Mouse.isMousePressed(0) && wasPressed && callback != null) {
                callback.call();
            } else {
                wasPressed = true;
            }
            Renderer.getUiShader().loadUIColor(new Vector4f(0f, 0f, 0f, 0.8f));
        } else {
            Renderer.getUiShader().loadUIColor(color);
        }
        if (!Mouse.isMousePressed(0)) {
            wasPressed = false;
        }
        Renderer.getUiShader().loadViewportSize(new Vector2i(DisplayManager.WIDTH, DisplayManager.HEIGHT));
        DrawRect();

        Renderer.getUiShader().stop();
        uiText.Render();
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
        uiText.resetVAO();
    }

    public ScreenConstraint getConstraints() {
        return constraints;
    }

    public String getText() {
        return text;
    }

    public Vector2i getSize() {
        return size;
    }

    public void setSize(Vector2i size) {
        this.size = new Vector2i(size);
        resetVAO();
    }
}
