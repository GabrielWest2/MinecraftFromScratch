package voxelengine.model;

import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL15;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL30;
import voxelengine.texture.Texture;
import voxelengine.texture.TextureLoader;

import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.util.ArrayList;
import java.util.List;

import static org.lwjgl.opengl.GL30.*;


public class ModelCreator {

    private static final float SIZE = 600f;

    private static final float[] SKYBOX_VERTICES = {
            -SIZE, SIZE, -SIZE,
            -SIZE, -SIZE, -SIZE,
            SIZE, -SIZE, -SIZE,
            SIZE, -SIZE, -SIZE,
            SIZE, SIZE, -SIZE,
            -SIZE, SIZE, -SIZE,

            -SIZE, -SIZE, SIZE,
            -SIZE, -SIZE, -SIZE,
            -SIZE, SIZE, -SIZE,
            -SIZE, SIZE, -SIZE,
            -SIZE, SIZE, SIZE,
            -SIZE, -SIZE, SIZE,

            SIZE, -SIZE, -SIZE,
            SIZE, -SIZE, SIZE,
            SIZE, SIZE, SIZE,
            SIZE, SIZE, SIZE,
            SIZE, SIZE, -SIZE,
            SIZE, -SIZE, -SIZE,

            -SIZE, -SIZE, SIZE,
            -SIZE, SIZE, SIZE,
            SIZE, SIZE, SIZE,
            SIZE, SIZE, SIZE,
            SIZE, -SIZE, SIZE,
            -SIZE, -SIZE, SIZE,

            -SIZE, SIZE, -SIZE,
            SIZE, SIZE, -SIZE,
            SIZE, SIZE, SIZE,
            SIZE, SIZE, SIZE,
            -SIZE, SIZE, SIZE,
            -SIZE, SIZE, -SIZE,

            -SIZE, -SIZE, -SIZE,
            -SIZE, -SIZE, SIZE,
            SIZE, -SIZE, -SIZE,
            SIZE, -SIZE, -SIZE,
            -SIZE, -SIZE, SIZE,
            SIZE, -SIZE, SIZE
    };


    private static final List<Integer> vaos = new ArrayList<>();
    private static final List<Integer> vbos = new ArrayList<>();
    private static int quad;


    public static Model loadToVAO(float[] positions, int[] indices) {
        int vaoID = createVAO();
        bindIndicesBuffer(indices);
        storeDataInAttributeList(0, 3, positions);
        unbindVAO();
        return new Model(vaoID, positions.length);
    }

    public static Model loadToVAO(float[] positions, int dimensions) {
        int vaoID = createVAO();
        storeDataInAttributeList(0, dimensions, positions);
        unbindVAO();
        return new Model(vaoID, positions.length / dimensions);
    }

    public static Model loadToVAO(FloatBuffer positions, IntBuffer indices) {
        int vaoID = createVAO();
        bindIndicesBuffer(indices);
        storeDataInAttributeList(0, 3, positions);
        unbindVAO();
        return new Model(vaoID, positions.capacity());
    }

    /*
     * Generates a skybox from given cube map textures
     */
    public static SkyboxModel createSkyboxModel(String[] TEXTURE_FILES) {
        Model m = ModelCreator.loadToVAO(SKYBOX_VERTICES, 3);
        return new SkyboxModel(m.getVaoID(), m.getVertexCount(), TextureLoader.loadCubeMap(TEXTURE_FILES));
    }

    public static TexturedModel loadToTexturedVAO(float[] positions, int[] indices, float[] textureCoords, float[] normals, Texture texture) {
        int vaoID = createVAO();
        bindIndicesBuffer(indices);
        storeDataInAttributeList(0, 3, positions);
        storeDataInAttributeList(1, 2, textureCoords);
        storeDataInAttributeList(2, 1, normals);
        unbindVAO();
        return new TexturedModel(vaoID, positions.length * 2, texture);
    }


    public static TexturedModel loadToTexturedVAO(FloatBuffer positions, IntBuffer indices, FloatBuffer textureCoords, FloatBuffer normals, Texture texture) {
        int vaoID = createVAO();
        bindIndicesBuffer(indices);
        storeDataInAttributeList(0, 3, positions);
        storeDataInAttributeList(1, 2, textureCoords);
        storeDataInAttributeList(2, 1, normals);
        unbindVAO();
        return new TexturedModel(vaoID, positions.capacity(), texture);
    }

    public static int createVAO() {
        int vaoID = glGenVertexArrays();
        vaos.add(vaoID);
        glBindVertexArray(vaoID);
        return vaoID;
    }

    private static void storeDataInAttributeList(int attributeNumber, int coordinateSize, float[] data) {
        int vboID = GL15.glGenBuffers();
        vbos.add(vboID);
        glBindBuffer(GL15.GL_ARRAY_BUFFER, vboID);
        FloatBuffer buffer = storeDataInFloatBuffer(data);
        GL15.glBufferData(GL15.GL_ARRAY_BUFFER, buffer, GL15.GL_STATIC_DRAW);
        GL20.glVertexAttribPointer(attributeNumber, coordinateSize, GL11.GL_FLOAT, false, 0, 0);
        glBindBuffer(GL15.GL_ARRAY_BUFFER, 0);
    }

    private static void storeDataInAttributeList(int attributeNumber, int coordinateSize, int[] data) {
        int vboID = GL15.glGenBuffers();
        vbos.add(vboID);
        glBindBuffer(GL15.GL_ARRAY_BUFFER, vboID);
        IntBuffer buffer = storeDataInIntBuffer(data);
        GL15.glBufferData(GL15.GL_ARRAY_BUFFER, buffer, GL15.GL_STATIC_DRAW);
        GL20.glVertexAttribPointer(attributeNumber, coordinateSize, GL11.GL_INT, false, 0, 0);
        glBindBuffer(GL15.GL_ARRAY_BUFFER, 0);
    }

    private static void storeDataInAttributeList(int attributeNumber, int coordinateSize, FloatBuffer data) {
        int vboID = GL15.glGenBuffers();
        vbos.add(vboID);
        glBindBuffer(GL15.GL_ARRAY_BUFFER, vboID);
        GL15.glBufferData(GL15.GL_ARRAY_BUFFER, data, GL15.GL_STATIC_DRAW);
        GL20.glVertexAttribPointer(attributeNumber, coordinateSize, GL11.GL_FLOAT, false, 0, 0);
        glBindBuffer(GL15.GL_ARRAY_BUFFER, 0);
    }


    public static void cleanUp() {
        for (Integer vao : vaos) {
            GL30.glDeleteVertexArrays(vao);
        }
        for (Integer vbo : vbos) {
            GL15.glDeleteBuffers(vbo);
        }
    }

    private static void unbindVAO() {
        glBindVertexArray(0);
    }

    private static void bindIndicesBuffer(int[] indices) {
        int vboID = GL15.glGenBuffers();
        vbos.add(vboID);
        glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, vboID);
        IntBuffer buffer = storeDataInIntBuffer(indices);
        GL15.glBufferData(GL15.GL_ELEMENT_ARRAY_BUFFER, buffer, GL15.GL_STATIC_DRAW);
    }

    private static void bindIndicesBuffer(IntBuffer buffer) {
        int vboID = GL15.glGenBuffers();
        vbos.add(vboID);
        glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, vboID);
        GL15.glBufferData(GL15.GL_ELEMENT_ARRAY_BUFFER, buffer, GL15.GL_STATIC_DRAW);
    }


    public static IntBuffer storeDataInIntBuffer(int[] data) {
        IntBuffer buffer = BufferUtils.createIntBuffer(data.length);
        buffer.put(data);
        buffer.flip();
        return buffer;
    }

    public static FloatBuffer storeDataInFloatBuffer(float[] data) {
        FloatBuffer buffer = BufferUtils.createFloatBuffer(data.length);
        buffer.put(data);
        buffer.flip();
        return buffer;
    }
}
