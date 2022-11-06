package voxelengine.gui;

import org.joml.Vector2i;
import org.joml.Vector4f;
import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL15;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL30;
import voxelengine.Renderer;

import java.nio.FloatBuffer;

import static org.lwjgl.opengl.GL11.GL_FLOAT;
import static org.lwjgl.opengl.GL15.glBindBuffer;


/**
 * @author gabed
 * @Date 10/16/2022
 */

public class Text implements UIElement {
    private final ScreenConstraint constraints;
    private final int size;
    int vao = -1;
    private String text;
    private Vector4f color;
    private Vector2i calcSize;

    public Text(ScreenConstraint constraints, String text, int size, Vector4f color) {
        this.constraints = constraints;
        this.text = text;
        this.size = size;
        this.color = color;
    }

    public static int getStringSize(String string, int fontSize) {
        int total = 0;
        for (int i = 0; i < string.length(); i++) {
            total += getSpacing(string.charAt(i), fontSize);
        }
        return total;
    }

    private static int getSpacing(char charAt, int fontSize) {
        if (charAt == 'i' || charAt == 'l' || charAt == '.') {

            return (int) (fontSize * 0.25);
        } else if (charAt == 't') {
            return (int) (fontSize * 0.5);
        } else {
            return (int) (fontSize * 0.75);
        }
    }

    public static Vector2i getCharacterCoords(char character) {
        int x = 0;
        int y = 0;
        for (int i = 0; i < (int) character; i++) {
            x++;
            if (x > 15) {
                y++;
                x = 0;
            }
        }
        return new Vector2i(x, y);
    }

    public void setColor(Vector4f color) {
        this.color = color;
    }

    @Override
    public int createQuad(Vector2i textPos, Vector2i size) {
        float[] vertices = new float[text.length() * 12];
        float[] uvCoords = new float[text.length() * 12];
        for (int i = 0; i < text.length(); i++) {
            vertices[i * 12] = textPos.x;
            vertices[i * 12 + 1] = textPos.y + size.y;
            vertices[i * 12 + 2] = textPos.x;
            vertices[i * 12 + 3] = textPos.y;
            vertices[i * 12 + 4] = textPos.x + size.y;
            vertices[i * 12 + 5] = textPos.y + size.y;

            vertices[i * 12 + 6] = textPos.x + size.y;
            vertices[i * 12 + 7] = textPos.y + size.y;
            vertices[i * 12 + 8] = textPos.x;
            vertices[i * 12 + 9] = textPos.y;
            vertices[i * 12 + 10] = textPos.x + size.y;
            vertices[i * 12 + 11] = textPos.y;
            /*
            vertices = new float[]{
                    textPos.x, textPos.y + size.y,
                    textPos.x, textPos.y,
                    textPos.x + size.x, textPos.y + size.y,

                    textPos.x + size.x, textPos.y + size.y,
                    textPos.x, textPos.y,
                    textPos.x  + size.x, textPos.y,
            };
             */
            Vector2i charCoord = getCharacterCoords(text.charAt(i));
            float x_spacing = (8 / 128f);
            float y_spacing = (8 / 128f);
            float x = charCoord.x * x_spacing;
            float y = charCoord.y * y_spacing;

            uvCoords[i * 12] = x + 0;
            uvCoords[i * 12 + 1] = y + y_spacing;
            uvCoords[i * 12 + 2] = x + 0;
            uvCoords[i * 12 + 3] = y + 0;
            uvCoords[i * 12 + 4] = x + x_spacing;
            uvCoords[i * 12 + 5] = y + y_spacing;

            uvCoords[i * 12 + 6] = x + x_spacing;
            uvCoords[i * 12 + 7] = y + y_spacing;
            uvCoords[i * 12 + 8] = x + 0;
            uvCoords[i * 12 + 9] = y + 0;
            uvCoords[i * 12 + 10] = x + x_spacing;
            uvCoords[i * 12 + 11] = y + 0;

            /*uvCoords = new float[]{
                    x + 0, y + 1/3f,
                    x + 0, y + 0,
                    x + 1/28f, y + 1/3f,

                    x + 1/28f, y + 1/3f,
                    x + 0, y + 0,
                    x + 1/28f, y + 0,

            };*/
            textPos.x += getSpacing(text.charAt(i));
        }

        int quad = GL30.glGenVertexArrays();
        GL30.glBindVertexArray(quad);

        int vboID = GL15.glGenBuffers();
        GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, vboID);
        FloatBuffer buffer = BufferUtils.createFloatBuffer(vertices.length);
        buffer.put(vertices);
        buffer.flip();
        GL15.glBufferData(GL15.GL_ARRAY_BUFFER, buffer, GL15.GL_STATIC_DRAW);
        GL20.glVertexAttribPointer(0, 2, GL_FLOAT, false, 0, 0);

        vboID = GL15.glGenBuffers();
        glBindBuffer(GL15.GL_ARRAY_BUFFER, vboID);
        buffer = BufferUtils.createFloatBuffer(uvCoords.length);
        buffer.put(uvCoords);
        buffer.flip();
        GL15.glBufferData(GL15.GL_ARRAY_BUFFER, buffer, GL15.GL_STATIC_DRAW);
        GL20.glVertexAttribPointer(1, 2, GL11.GL_FLOAT, false, 0, 0);
        glBindBuffer(GL15.GL_ARRAY_BUFFER, 0);

        GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, 0);
        GL30.glBindVertexArray(0);
        return quad;
    }

    @Override
    public boolean Render() {
        Renderer.getFontShader().start();
        Renderer.getFontShader().loadUIColor(color);

        /*
        Vector2i pos = constraints.calculatePixelPosition(getSize());
        Vector2f center = new Vector2f(pos.x / DisplayManager.WIDTH, pos.y / DisplayManager.HEIGHT);
        center.y = (1.0f - center.y);
        center.x = center.x * 2f - 1.0f;
        center.y = center.y * 2f - 1.0f;

        Matrix4f rotationMatrix = new Matrix4f().identity();
        float cosTheta = Math.cos(Math.toRadians(45));
        float sinTheta = Math.sin(Math.toRadians(45));
        rotationMatrix.m00(cosTheta);
        rotationMatrix.m10(-sinTheta);
        rotationMatrix.m11(cosTheta);
        rotationMatrix.m01(sinTheta);

        Matrix4f nP = new Matrix4f().identity();
        nP.m30(-center.x + 0.5f);
        nP.m31(-center.y + 0.5f);

        Matrix4f P = new Matrix4f().identity();
        P.m30(center.x);
        P.m31(center.y);
        Renderer.getFontShader().loadRotationMatrix(new Matrix4f().identity());
        */


        DrawText(text);
        Renderer.getFontShader().stop();
        return true;
    }

    @Override
    public int getVAO() {
        if (vao == -1) {
            vao = createQuad(constraints.calculatePixelPosition(getSize()), getSize());
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

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
        resetVAO();
    }

    public Vector2i getSize() {
        if (calcSize == null) {
            int length = 0;
            for (int i = 0; i < text.length(); i++) {
                length += getSpacing(text.charAt(i));
            }
            calcSize = new Vector2i(length, size);
        }
        return calcSize;
    }

    private int getSpacing(char charAt) {
        if (charAt == 'i' || charAt == 'l' || charAt == '.' || charAt == '!' || charAt == ':') {
            return (int) (size * 0.25);
        } else if (charAt == 't') {
            return (int) (size * 0.5);
        } else {
            return (int) (size * 0.75);
        }
    }

}
