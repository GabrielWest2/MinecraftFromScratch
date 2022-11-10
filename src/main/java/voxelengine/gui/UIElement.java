package voxelengine.gui;

import org.joml.Vector2i;
import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL15;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL30;
import voxelengine.engine.DisplayManager;
import voxelengine.texture.TextureLoader;

import java.nio.FloatBuffer;

import static org.lwjgl.opengl.GL15.*;

/**
 * @author gabed
 * @Date 10/17/2022
 */
public interface UIElement {
    boolean Render();

    Vector2i getSize();

    ScreenConstraint getConstraints();

    default int createQuad(Vector2i pos, Vector2i size) {
        final float[] POSITIONS = {pos.x, pos.y + size.y, pos.x, pos.y, pos.x + size.x, pos.y + size.y, pos.x + size.x, pos.y};

        int quad = GL30.glGenVertexArrays();
        GL30.glBindVertexArray(quad);
        int vboID = GL15.glGenBuffers();
        GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, vboID);
        FloatBuffer buffer = BufferUtils.createFloatBuffer(POSITIONS.length);
        buffer.put(POSITIONS);
        buffer.flip();
        GL15.glBufferData(GL15.GL_ARRAY_BUFFER, buffer, GL15.GL_STATIC_DRAW);
        GL20.glVertexAttribPointer(0, 2, GL_FLOAT, false, 0, 0);
        GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, 0);
        GL30.glBindVertexArray(0);
        return quad;
    }

    default int createButtonQuad(Vector2i pos, Vector2i size) {
        final float[] POSITIONS = {pos.x, pos.y + size.y, pos.x, pos.y, pos.x + size.x, pos.y + size.y, pos.x + size.x, pos.y};
        final float[] UVS = {
                0, 0.33333333333f,
                0, 0,
                1, 0.33333333333f,
                1, 0
        };

        int quad = GL30.glGenVertexArrays();
        GL30.glBindVertexArray(quad);

        int vboID = GL15.glGenBuffers();
        GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, vboID);
        FloatBuffer buffer = BufferUtils.createFloatBuffer(POSITIONS.length);
        buffer.put(POSITIONS);
        buffer.flip();
        GL15.glBufferData(GL15.GL_ARRAY_BUFFER, buffer, GL15.GL_STATIC_DRAW);
        GL20.glVertexAttribPointer(0, 2, GL_FLOAT, false, 0, 0);

        vboID = GL15.glGenBuffers();
        glBindBuffer(GL15.GL_ARRAY_BUFFER, vboID);
        buffer = BufferUtils.createFloatBuffer(UVS.length);
        buffer.put(UVS);
        buffer.flip();
        GL15.glBufferData(GL15.GL_ARRAY_BUFFER, buffer, GL15.GL_STATIC_DRAW);
        GL20.glVertexAttribPointer(1, 2, GL11.GL_FLOAT, false, 0, 0);
        glBindBuffer(GL15.GL_ARRAY_BUFFER, 0);

        GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, 0);
        GL30.glBindVertexArray(0);
        return quad;
    }

    default int createUVImageQuad(Vector2i pos, Vector2i size, float[] UVS) {
        final float[] POSITIONS = {pos.x, pos.y + size.y, pos.x, pos.y, pos.x + size.x, pos.y + size.y, pos.x + size.x, pos.y};

        int quad = GL30.glGenVertexArrays();
        GL30.glBindVertexArray(quad);

        int vboID = GL15.glGenBuffers();
        GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, vboID);
        FloatBuffer buffer = BufferUtils.createFloatBuffer(POSITIONS.length);
        buffer.put(POSITIONS);
        buffer.flip();
        GL15.glBufferData(GL15.GL_ARRAY_BUFFER, buffer, GL15.GL_STATIC_DRAW);
        GL20.glVertexAttribPointer(0, 2, GL_FLOAT, false, 0, 0);

        vboID = GL15.glGenBuffers();
        glBindBuffer(GL15.GL_ARRAY_BUFFER, vboID);
        buffer = BufferUtils.createFloatBuffer(UVS.length);
        buffer.put(UVS);
        buffer.flip();
        GL15.glBufferData(GL15.GL_ARRAY_BUFFER, buffer, GL15.GL_STATIC_DRAW);
        GL20.glVertexAttribPointer(1, 2, GL11.GL_FLOAT, false, 0, 0);
        glBindBuffer(GL15.GL_ARRAY_BUFFER, 0);

        GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, 0);
        GL30.glBindVertexArray(0);
        return quad;
    }

    default int createImageQuad(Vector2i pos, Vector2i size, boolean fullX, boolean fullY, boolean tileTexture) {
        if (fullX) {
            pos.x = 0;
            size.x = DisplayManager.WIDTH;
        }
        if (fullY) {
            pos.y = 0;
            size.y = DisplayManager.HEIGHT;
        }

        final float[] POSITIONS = {pos.x, pos.y + size.y, pos.x, pos.y, pos.x + size.x, pos.y + size.y, pos.x + size.x, pos.y};


        float[] UVS = {
                0, 1,
                0, 0,
                1, 1,
                1, 0
        };
        if (tileTexture) {
            UVS = new float[]{
                    0, size.y / 100f,
                    0, 0,
                    size.x / 100f, size.y / 100f,
                    size.x / 100f, 0
            };
        }

        int quad = GL30.glGenVertexArrays();
        GL30.glBindVertexArray(quad);

        int vboID = GL15.glGenBuffers();
        GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, vboID);
        FloatBuffer buffer = BufferUtils.createFloatBuffer(POSITIONS.length);
        buffer.put(POSITIONS);
        buffer.flip();
        GL15.glBufferData(GL15.GL_ARRAY_BUFFER, buffer, GL15.GL_STATIC_DRAW);
        GL20.glVertexAttribPointer(0, 2, GL_FLOAT, false, 0, 0);

        vboID = GL15.glGenBuffers();
        glBindBuffer(GL15.GL_ARRAY_BUFFER, vboID);
        buffer = BufferUtils.createFloatBuffer(UVS.length);
        buffer.put(UVS);
        buffer.flip();
        GL15.glBufferData(GL15.GL_ARRAY_BUFFER, buffer, GL15.GL_STATIC_DRAW);
        GL20.glVertexAttribPointer(1, 2, GL11.GL_FLOAT, false, 0, 0);
        glBindBuffer(GL15.GL_ARRAY_BUFFER, 0);

        GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, 0);
        GL30.glBindVertexArray(0);
        return quad;
    }

    default int createImageQuad(Vector2i pos, Vector2i size) {

        final float[] POSITIONS = {pos.x, pos.y + size.y, pos.x, pos.y, pos.x + size.x, pos.y + size.y, pos.x + size.x, pos.y};


        float[] UVS = {
                0, 1,
                0, 0,
                1, 1,
                1, 0
        };
        int quad = GL30.glGenVertexArrays();
        GL30.glBindVertexArray(quad);

        int vboID = GL15.glGenBuffers();
        GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, vboID);
        FloatBuffer buffer = BufferUtils.createFloatBuffer(POSITIONS.length);
        buffer.put(POSITIONS);
        buffer.flip();
        GL15.glBufferData(GL15.GL_ARRAY_BUFFER, buffer, GL15.GL_STATIC_DRAW);
        GL20.glVertexAttribPointer(0, 2, GL_FLOAT, false, 0, 0);

        vboID = GL15.glGenBuffers();
        glBindBuffer(GL15.GL_ARRAY_BUFFER, vboID);
        buffer = BufferUtils.createFloatBuffer(UVS.length);
        buffer.put(UVS);
        buffer.flip();
        GL15.glBufferData(GL15.GL_ARRAY_BUFFER, buffer, GL15.GL_STATIC_DRAW);
        GL20.glVertexAttribPointer(1, 2, GL11.GL_FLOAT, false, 0, 0);
        glBindBuffer(GL15.GL_ARRAY_BUFFER, 0);

        GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, 0);
        GL30.glBindVertexArray(0);
        return quad;
    }

    default void DrawRect() {

        GL30.glBindVertexArray(getVAO());
        GL20.glEnableVertexAttribArray(0);
        glDisable(GL_DEPTH_TEST);
        glDisable(GL_CULL_FACE);
        glDrawArrays(GL_TRIANGLE_STRIP, 0, 4);
        glEnable(GL_CULL_FACE);
        glEnable(GL_DEPTH_TEST);
        GL20.glDisableVertexAttribArray(0);
        GL30.glBindVertexArray(0);
    }

    default void DrawText(String text) {
        GL30.glBindVertexArray(getVAO());
        GL20.glEnableVertexAttribArray(0);
        GL20.glEnableVertexAttribArray(1);
        glDisable(GL_DEPTH_TEST);
        glDisable(GL_CULL_FACE);
        glActiveTexture(0);
        GL11.glBindTexture(GL11.GL_TEXTURE_2D, TextureLoader.getTexture(1).getTextureID());
        glDrawArrays(GL_TRIANGLES, 0, text.length() * 6);
        glEnable(GL_CULL_FACE);
        glEnable(GL_DEPTH_TEST);
        GL20.glDisableVertexAttribArray(0);
        GL20.glDisableVertexAttribArray(1);
        GL30.glBindVertexArray(0);
    }

    default void DrawTextureButton() {
        GL30.glBindVertexArray(getVAO());
        GL20.glEnableVertexAttribArray(0);
        GL20.glEnableVertexAttribArray(1);
        glDisable(GL_DEPTH_TEST);
        glDisable(GL_CULL_FACE);
        glActiveTexture(0);
        GL11.glBindTexture(GL11.GL_TEXTURE_2D, TextureLoader.getTexture(2).getTextureID());
        glDrawArrays(GL_TRIANGLE_STRIP, 0, 4);
        glEnable(GL_CULL_FACE);
        glEnable(GL_DEPTH_TEST);
        GL20.glDisableVertexAttribArray(1);
        GL20.glDisableVertexAttribArray(0);
        GL30.glBindVertexArray(0);
    }

    default void DrawImage(int id) {
        GL30.glBindVertexArray(getVAO());
        GL20.glEnableVertexAttribArray(0);
        GL20.glEnableVertexAttribArray(1);
        glDisable(GL_DEPTH_TEST);
        glDisable(GL_CULL_FACE);
        glActiveTexture(0);
        GL11.glBindTexture(GL11.GL_TEXTURE_2D, TextureLoader.getTexture(id).getTextureID());
        glDrawArrays(GL_TRIANGLE_STRIP, 0, 4);
        glEnable(GL_CULL_FACE);
        glEnable(GL_DEPTH_TEST);
        GL20.glDisableVertexAttribArray(1);
        GL20.glDisableVertexAttribArray(0);
        GL30.glBindVertexArray(0);
    }

    default void DrawSmallButton() {
        GL30.glBindVertexArray(getVAO());
        GL20.glEnableVertexAttribArray(0);
        GL20.glEnableVertexAttribArray(1);
        glDisable(GL_DEPTH_TEST);
        glDisable(GL_CULL_FACE);
        glActiveTexture(0);
        GL11.glBindTexture(GL11.GL_TEXTURE_2D, TextureLoader.getTexture(3).getTextureID());
        glDrawArrays(GL_TRIANGLE_STRIP, 0, 4);
        glEnable(GL_CULL_FACE);
        glEnable(GL_DEPTH_TEST);
        GL20.glDisableVertexAttribArray(1);
        GL20.glDisableVertexAttribArray(0);
        GL30.glBindVertexArray(0);
    }

    int getVAO();

    void resetVAO();

    default boolean isInBoundingBox(Vector2i position) {
        Vector2i p = getConstraints().calculatePixelPosition(getSize());
        return position.x > p.x && position.x < p.x + getSize().x && position.y > p.y && position.y < p.y + getSize().y;
    }

    default boolean isObstructed(Vector2i position) {
        for (int i = UIManager.getActiveMenu().getUIElements().size() - 1; i >= 0; i--) {
            if (UIManager.getActiveMenu().getUIElements().get(i) == this)
                return false;
            if (UIManager.getActiveMenu().getUIElements().get(i).isInBoundingBox(position))
                return true;
        }
        return false;
    }
}
