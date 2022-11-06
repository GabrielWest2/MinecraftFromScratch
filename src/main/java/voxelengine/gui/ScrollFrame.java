package voxelengine.gui;

import org.joml.Vector2i;
import org.joml.Vector4f;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL30;
import voxelengine.Renderer;
import voxelengine.engine.DisplayManager;
import voxelengine.gui.callback.Callback;
import voxelengine.input.Mouse;
import voxelengine.texture.TextureLoader;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL13.glActiveTexture;

/**
 * @author gabed
 * @Date 10/23/2022
 */
public class ScrollFrame implements UIElement {
    private static final List<ScrollFrame> scrollFrames = new ArrayList<>();
    private final ScrollFrameEntry[] elements;
    private final int distanceFromTop, distanceFromBottom;
    private final int sliderHeight = 75;
    private final Image backgroundImage = new Image(new ScreenConstraint(null), 6, new Vector2i(10000, 10000), false, false, true);
    public boolean wasPressed = false;
    int vao = -1;
    int vao2 = -1;
    private Vector2i size;
    private Vector2i position;
    private int sliderMin;
    private int sliderMax;
    private float sliderValue = 0f;
    private Callback scrollCallback;
    private float lastSliderValue;

    public ScrollFrame(int distanceFromTop, int distanceFromBottom, ScrollFrameEntry[] elements) {
        this.distanceFromTop = distanceFromTop;
        this.distanceFromBottom = distanceFromBottom;
        recalculatePosition();
        resetSliderBounds();
        this.elements = elements;
        scrollFrames.add(this);
    }

    public ScrollFrame(int distanceFromTop, int distanceFromBottom, Callback scrollCallback, ScrollFrameEntry... elements) {
        this.distanceFromTop = distanceFromTop;
        this.distanceFromBottom = distanceFromBottom;
        this.scrollCallback = scrollCallback;
        recalculatePosition();
        resetSliderBounds();
        this.elements = elements;
        scrollFrames.add(this);
        resetVAO();
    }

    public static void updateScroll(float scrollY) {
        scrollFrames.forEach(s -> s.setSliderValue(s.sliderValue + scrollY));
    }

    private void recalculatePosition() {
        this.size = new Vector2i(20, DisplayManager.HEIGHT - distanceFromBottom - distanceFromTop);
        this.position = new Vector2i(DisplayManager.WIDTH - 60, distanceFromTop);
    }

    public ScrollFrameEntry[] getElements() {
        return elements;
    }

    public float getSliderValue() {
        return sliderValue;
    }

    private void setSliderValue(float f) {
        if (isPressed())
            return;
        sliderValue = f;
        if (sliderValue < 0)
            sliderValue = 0;
        if (sliderValue > 1)
            sliderValue = 1;
        Arrays.stream(elements).forEach(e -> ((WorldTab) e).updatePosition(sliderValue, 150 * elements.length));
        backgroundImage.getConstraints().addConstraint(Side.TOP, (int) (-sliderValue * 150 * elements.length) + "px");
    }

    @Override
    public boolean Render() {
        boolean pressed = isPressed();
        if (pressed || wasPressed)
            setSliderValue();
        wasPressed = pressed;
        if (sliderValue != lastSliderValue) {
            resetVAO();
            if (scrollCallback != null)
                scrollCallback.call();
        }
        lastSliderValue = sliderValue;

        backgroundImage.Render();
        Renderer.getUiShader().start();
        Renderer.getUiShader().loadUIColor(new Vector4f(0, 0, 0, 0.75f));
        DrawRect();
        Renderer.getUiShader().stop();


        Renderer.getButtonShader().start();
        Renderer.getButtonShader().loadHovered(isHovered());
        Renderer.getButtonShader().loadDisabled(false);
        DrawScrollHandle();
        Renderer.getButtonShader().stop();

        Arrays.stream(elements).forEach(ScrollFrameEntry::Render);

        return true;
    }

    private void DrawScrollHandle() {
        GL30.glBindVertexArray(vao2);
        GL20.glEnableVertexAttribArray(0);
        GL20.glEnableVertexAttribArray(1);
        glDisable(GL_DEPTH_TEST);
        glDisable(GL_CULL_FACE);
        glActiveTexture(0);
        GL11.glBindTexture(GL11.GL_TEXTURE_2D, TextureLoader.getTexture(8).getTextureID());
        glDrawArrays(GL_TRIANGLE_STRIP, 0, 4);
        glEnable(GL_CULL_FACE);
        glEnable(GL_DEPTH_TEST);
        GL20.glDisableVertexAttribArray(1);
        GL20.glDisableVertexAttribArray(0);
        GL30.glBindVertexArray(0);
    }

    private boolean isPressed() {
        return Mouse.isMousePressed(0) && isHovered();
    }

    private boolean isHovered() {
        Vector2i sliderPos = calculateSliderPosition();
        return (Mouse.isMousePressed(0) && wasPressed) || (Mouse.getMouseX() > sliderPos.x && Mouse.getMouseX() < sliderPos.x + size.x && Mouse.getMouseY() > sliderPos.y && Mouse.getMouseY() < sliderPos.y + sliderHeight);
    }

    private void setSliderValue() {
        sliderValue = (Mouse.getMouseY() - (sliderHeight / 2f) - sliderMin) / (sliderMax - sliderMin);
        if (sliderValue < 0)
            sliderValue = 0;
        if (sliderValue > 1)
            sliderValue = 1;
        Arrays.stream(elements).forEach(e -> ((WorldTab) e).updatePosition(sliderValue, 150 * elements.length));
        backgroundImage.getConstraints().addConstraint(Side.TOP, (int) (-sliderValue * 150 * elements.length) + "px");
    }

    private void resetSliderBounds() {
        this.sliderMin = position.y;
        this.sliderMax = position.y + size.y - sliderHeight;
    }

    private Vector2i calculateSliderPosition() {
        Vector2i vec = position;
        vec.y = (int) (sliderValue * (sliderMax - sliderMin) + sliderMin);
        return vec;
    }

    @Override
    public int getVAO() {
        vao2 = -1;
        if (vao == -1)
            vao = createQuad(position, size);
        if (vao2 == -1)
            vao2 = createButtonQuad(calculateSliderPosition(), new Vector2i(size.x, sliderHeight));
        return vao;
    }

    @Override
    public void resetVAO() {
        vao = -1;
        vao2 = -1;
        recalculatePosition();
        resetSliderBounds();
        Arrays.stream(elements).forEach(ScrollFrameEntry::resetVAO);
        backgroundImage.resetVAO();
    }

    public Vector2i getSize() {
        return size;
    }

    @Override
    public ScreenConstraint getConstraints() {
        return new ScreenConstraint(null);
    }
}
