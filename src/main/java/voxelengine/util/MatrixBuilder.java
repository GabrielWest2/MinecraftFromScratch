package voxelengine.util;

import org.joml.Matrix3f;
import org.joml.Matrix4f;
import org.joml.Quaternionf;
import org.joml.Vector3f;
import voxelengine.Camera;
import voxelengine.engine.DisplayManager;

public class MatrixBuilder {

    public static final float FOV = 80;
    public static final float NEAR_PLANE = 0.1f;
    public static final float FAR_PLANE = 1500f;

    public static Matrix4f createTransformationMatrix(Vector3f offset, Vector3f rotation, Vector3f scale) {
        Matrix4f worldMatrix = new Matrix4f();
        worldMatrix.identity().translate(offset).
                rotateX((float) Math.toRadians(rotation.x)).
                rotateY((float) Math.toRadians(rotation.y)).
                rotateZ((float) Math.toRadians(rotation.z)).
                scale(scale.x, scale.y, scale.z);
        return worldMatrix;
    }

    public static Matrix4f createTransformationMatrix(Vector3f offset, Quaternionf rotation, Vector3f scale) {
        Matrix4f worldMatrix = new Matrix4f();
        worldMatrix.identity().translate(offset).
                rotate(rotation).
                scale(scale.x, scale.y, scale.z);
        return worldMatrix;
    }


    public static Matrix4f createProjectionMatrix() {
        float aspectRatio = (float) DisplayManager.WIDTH / (float) DisplayManager.HEIGHT;
        float y_scale = (float) ((1f / Math.tan(Math.toRadians(FOV / 2f))));
        float x_scale = y_scale / aspectRatio;
        float frustum_length = FAR_PLANE - NEAR_PLANE;

        Matrix4f matrix = new Matrix4f();
        matrix.m00(x_scale);
        matrix.m11(y_scale);
        matrix.m22(-((FAR_PLANE + NEAR_PLANE) / frustum_length));
        matrix.m23(-1);
        matrix.m32(-((2 * NEAR_PLANE * FAR_PLANE) / frustum_length));
        matrix.m33(0);
        return matrix;
    }

    public static Matrix4f createViewMatrix() {
        Matrix4f matrix = new Matrix4f();
        Vector3f forward = new Vector3f();
        Matrix3f normal = new Matrix3f();
        //matrix.identity().translate(camera.getPosition()).setRotationXYZ((float)Math.toRadians(camera.getRotation().x), (float)Math.toRadians(camera.getRotation().y), (float)Math.toRadians(camera.getRotation().z));
        matrix.identity()
                .rotateX((float) Math.toRadians(-Camera.getRotation().x))
                .rotateY((float) Math.toRadians(-Camera.getRotation().y))
                .rotateZ((float) Math.toRadians(-Camera.getRotation().z))
                .translate(-Camera.getPosition().x, -Camera.getPosition().y, -Camera.getPosition().z)
                .normal(normal) // <- returns the 'normal' matrix
                .invert()
                .transform(forward.set(0, 0, -1)); // <- what is forward?
        return matrix;
    }

    public static Matrix4f createOrthoViewMatrix(Vector3f pos, Vector3f dir) {
        Matrix4f matrix = new Matrix4f();
        Vector3f forward = new Vector3f();
        Matrix3f normal = new Matrix3f();
        //matrix.identity().translate(camera.getPosition()).setRotationXYZ((float)Math.toRadians(camera.getRotation().x), (float)Math.toRadians(camera.getRotation().y), (float)Math.toRadians(camera.getRotation().z));
        matrix.identity()
                .rotateX((float) Math.toRadians(-dir.x))
                .rotateY((float) Math.toRadians(-dir.y))
                .rotateZ((float) Math.toRadians(-dir.z))
                .translate(-pos.x, -pos.y, -pos.z)
                .normal(normal) // <- returns the 'normal' matrix
                .invert()
                .transform(forward.set(0, 0, -1)); // <- what is forward?
        return matrix;
    }


    public static Matrix4f createStationaryViewMatrix() {
        Matrix4f matrix = new Matrix4f();
        Vector3f forward = new Vector3f();
        Matrix3f normal = new Matrix3f();
        matrix.identity()
                .rotateX((float) Math.toRadians(-Camera.getRotation().x))
                .rotateY((float) Math.toRadians(-Camera.getRotation().y))
                .rotateZ((float) Math.toRadians(-Camera.getRotation().z))
                .normal(normal) // <- returns the 'normal' matrix
                .invert()
                .transform(forward.set(0, 0, -1)); // <- what is forward?
        return matrix;
    }
}
