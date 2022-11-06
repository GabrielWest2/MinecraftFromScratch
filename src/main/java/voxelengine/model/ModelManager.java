package voxelengine.model;

import com.bulletphysics.collision.shapes.BvhTriangleMeshShape;
import de.javagl.obj.Obj;
import de.javagl.obj.ObjData;
import de.javagl.obj.ObjReader;
import de.javagl.obj.ObjUtils;
import voxelengine.texture.Texture;
import voxelengine.texture.TextureLoader;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


public class ModelManager {
    private static final List<Texture> textures = new ArrayList<>();
    private static final HashMap<Long, TexturedModel> loadedModels = new HashMap<>();
    private static final HashMap<TexturedModel, BvhTriangleMeshShape> modelMeshes = new HashMap<>();

    public static void LoadTextures() {
        File dir = new File("res/images");
        File[] directoryListing = dir.listFiles();
        if (directoryListing != null) {
            int id = 0;
            for (File child : directoryListing) {
                System.out.println(child.getPath());
                textures.add(TextureLoader.getTexture(child.getPath()));
                id++;
            }
        }
    }

    public static void LoadModels() {
        File dir = new File("res/models");
        File[] directoryListing = dir.listFiles();
        if (directoryListing != null) {
            int id = 0;
            for (File child : directoryListing) {
                System.out.println(child.getPath());
                TexturedModel model = loadTexturedOBJ(child.getPath(), textures.get(id));
                loadedModels.put((long) id, model);
                id++;
            }
        }
    }

    private static TexturedModel loadTexturedOBJ(String path, Texture texture) {
        try {
            InputStream inputStream = new FileInputStream(path);
            Obj obj = ObjUtils.convertToRenderable(ObjReader.read(inputStream));
            /*
            Create a mesh collider for the Bullet Physics Engine
            float[] vert = new float[ObjData.getVertices(obj).limit()];
            ObjData.getVertices(obj).get(vert);
            ObjectArrayList<javax.vecmath.Vector3f> vertices = new ObjectArrayList<>();
            for (int i = 0; i < vert.length; i += 3) {
                vertices.add(new javax.vecmath.Vector3f(vert[i], vert[i + 1], vert[i + 2]));
            }


            TriangleIndexVertexArray indexVertexArray = new TriangleIndexVertexArray(ObjData.getFaceVertexIndices(obj).limit() / 3,
                    copyIntBufferAsByteBuffer(ObjData.getFaceVertexIndices(obj)),
                    3 * 4,
                    ObjData.getVertices(obj).limit() / 3,
                    copyFloatBufferAsByteBuffer(ObjData.getVertices(obj)),
                    3 * 8);

            modelMeshes.put(model, new BvhTriangleMeshShape(indexVertexArray, true));*/
            return ModelCreator.loadToTexturedVAO(ObjData.getVertices(obj), ObjData.getFaceVertexIndices(obj), ObjData.getTexCoords(obj, 2), ObjData.getNormals(obj), texture);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static HashMap<Long, TexturedModel> getLoadedModels() {
        return loadedModels;
    }

    public static List<Texture> getTextures() {
        return textures;
    }

    public static HashMap<TexturedModel, BvhTriangleMeshShape> getModelMeshes() {
        return modelMeshes;
    }

    public static ByteBuffer copyIntBufferAsByteBuffer(
            IntBuffer paramIntBuffer) {
        ByteBuffer localByteBuffer = newByteBuffer(paramIntBuffer
                .remaining() * 4);/*from   ww w  . j a  v a 2s . c o m*/
        paramIntBuffer.mark();
        localByteBuffer.asIntBuffer().put(paramIntBuffer);
        paramIntBuffer.reset();
        localByteBuffer.rewind();
        return localByteBuffer;
    }

    public static ByteBuffer newByteBuffer(int paramInt) {
        ByteBuffer localByteBuffer = ByteBuffer.allocateDirect(paramInt);
        localByteBuffer.order(ByteOrder.nativeOrder());
        return localByteBuffer;
    }

    public static ByteBuffer copyFloatBufferAsByteBuffer(
            FloatBuffer paramFloatBuffer) {
        ByteBuffer localByteBuffer = newByteBuffer(paramFloatBuffer
                .remaining() * 8);/*from   ww w  . j a  v a 2s . c o m*/
        paramFloatBuffer.mark();
        localByteBuffer.asFloatBuffer().put(paramFloatBuffer);
        paramFloatBuffer.reset();
        localByteBuffer.rewind();
        return localByteBuffer;
    }

}
