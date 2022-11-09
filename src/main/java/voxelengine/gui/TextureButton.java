package voxelengine.gui;

import org.joml.Vector2i;
import org.joml.Vector4f;
import voxelengine.Renderer;
import voxelengine.gui.callback.Callback;
import voxelengine.input.Mouse;


/**
 * @author gabed
 * @Date 10/16/2022
 */

public class TextureButton implements UIElement {
    private final ScreenConstraint constraints;
    private final String text;
    private final Vector2i size;
    private final Text uiText;
    int vao = -1;
    private boolean disabled = false;
    private Callback callback;

    public TextureButton(ScreenConstraint constraints, String text, Vector2i size, boolean disabled) {
        this.constraints = constraints;
        this.text = text;
        this.size = size;
        this.disabled = disabled;
        uiText = new Text(new ScreenConstraint(this).addConstraint(Side.LEFT, (size.x / 2) - (Text.getStringSize(text, 20) / 2) + "px").addConstraint(Side.TOP, ((size.y / 2) - (20 / 2)) + "px"), text, 20, new Vector4f(1, 1, 1, 1));
    }

    public TextureButton(ScreenConstraint constraints, String text, Vector2i size, boolean disabled, Callback callback) {
        this.constraints = constraints;
        this.text = text;
        this.size = size;
        this.disabled = disabled;
        this.callback = callback;
        uiText = new Text(new ScreenConstraint(this).addConstraint(Side.LEFT, (size.x / 2) - (Text.getStringSize(text, 20) / 2) + "px").addConstraint(Side.TOP, ((size.y / 2) - (20 / 2)) + "px"), text, 20, new Vector4f(1, 1, 1, 1));
    }

    public TextureButton(ScreenConstraint constraints, String text, Vector2i size, Callback callback) {
        this.constraints = constraints;
        this.text = text;
        this.size = size;
        this.callback = callback;
        this.disabled = false;
        uiText = new Text(new ScreenConstraint(this).addConstraint(Side.LEFT, (size.x / 2) - (Text.getStringSize(text, 20) / 2) + "px").addConstraint(Side.TOP, ((size.y / 2) - (20 / 2)) + "px"), text, 20, new Vector4f(1, 1, 1, 1));

    }

    @Override
    public boolean Render() {
        Vector2i pos = constraints.calculatePixelPosition(size);
        Renderer.getButtonShader().start();
        Renderer.getButtonShader().loadDisabled(disabled);
        boolean hovered = Mouse.getMouseX() > pos.x && Mouse.getMouseX() < pos.x + size.x && Mouse.getMouseY() > pos.y && Mouse.getMouseY() < pos.y + size.y;
        Renderer.getButtonShader().loadHovered(hovered);
        if (hovered && Mouse.isMousePressed(0) && callback != null && !disabled)
            callback.call();
        if (size.x > 300)
            DrawTextureButton();
        else
            DrawSmallButton();
        Renderer.getButtonShader().stop();
        uiText.setColor(disabled ? new Vector4f(0.6f) : new Vector4f(1));
        uiText.Render();
        return true;
    }

    @Override
    public int getVAO() {
        if (vao == -1) {
            vao = createButtonQuad(constraints.calculatePixelPosition(size), size);
        }
        return vao;
    }

    @Override
    public void resetVAO() {
        vao = -1;
        uiText.resetVAO();
    }

    public void setDisabled(boolean disabled) {
        this.disabled = disabled;
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
}
