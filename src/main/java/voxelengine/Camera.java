package voxelengine;

import org.joml.Math;
import org.joml.Vector3f;
import voxelengine.input.Keyboard;
import voxelengine.input.Mouse;
import voxelengine.util.Time;

import static org.lwjgl.glfw.GLFW.*;

public class Camera {
    private static final float MOVEMENT_SPEED = 10f;
    private static final float FAST_MOVEMENT_SPEED = 30f;
    private static Vector3f position = new Vector3f(0, 50, 0);
    private static Vector3f rotation = new Vector3f(0, 0, 0);

    public static void update() {
        float vx = 0, vy = 0, vz = 0;
        boolean ctrl = Keyboard.isKeyPressed(GLFW_KEY_LEFT_CONTROL);
        if (Keyboard.isKeyPressed(GLFW_KEY_W)) {
            vx = -Math.sin(Math.toRadians(rotation.y)) * (ctrl ? FAST_MOVEMENT_SPEED : MOVEMENT_SPEED);
            vz = -Math.cos(Math.toRadians(rotation.y)) * (ctrl ? FAST_MOVEMENT_SPEED : MOVEMENT_SPEED);
        }
        if (Keyboard.isKeyPressed(GLFW_KEY_S)) {
            vx = Math.sin(Math.toRadians(rotation.y)) * (ctrl ? FAST_MOVEMENT_SPEED : MOVEMENT_SPEED);
            vz = Math.cos(Math.toRadians(rotation.y)) * (ctrl ? FAST_MOVEMENT_SPEED : MOVEMENT_SPEED);
        }
        if (Keyboard.isKeyPressed(GLFW_KEY_A)) {
            vx = -Math.sin(Math.toRadians(rotation.y + 90)) * (ctrl ? FAST_MOVEMENT_SPEED : MOVEMENT_SPEED);
            vz = -Math.cos(Math.toRadians(rotation.y + 90)) * (ctrl ? FAST_MOVEMENT_SPEED : MOVEMENT_SPEED);
        }
        if (Keyboard.isKeyPressed(GLFW_KEY_D)) {
            vx = -Math.sin(Math.toRadians(rotation.y - 90)) * (ctrl ? FAST_MOVEMENT_SPEED : MOVEMENT_SPEED);
            vz = -Math.cos(Math.toRadians(rotation.y - 90)) * (ctrl ? FAST_MOVEMENT_SPEED : MOVEMENT_SPEED);
        }
        if (Keyboard.isKeyPressed(GLFW_KEY_SPACE)) {
            vy += (ctrl ? FAST_MOVEMENT_SPEED : MOVEMENT_SPEED);
        }
        if (Keyboard.isKeyPressed(GLFW_KEY_LEFT_SHIFT)) {
            vy -= (ctrl ? FAST_MOVEMENT_SPEED : MOVEMENT_SPEED);
        }
        Vector3f move = new Vector3f(Time.getDeltaTime() * vx, Time.getDeltaTime() * vy, Time.getDeltaTime() * vz);
        moveBy(move);
        if (Mouse.isMousePressed(1))
            rotateBy(new Vector3f(Mouse.getMouseDy() / -20f, Mouse.getMouseDx() / -20f, 0));
    }

    public static void moveBy(Vector3f vec) {
        position = new Vector3f(position.x + vec.x, position.y + vec.y, position.z + vec.z);
    }

    public static void rotateBy(Vector3f vec) {
        rotation = new Vector3f(rotation.x + vec.x, rotation.y + vec.y, 0);
    }

    public static Vector3f getPosition() {
        return position;
    }

    public static void setPosition(Vector3f position) {
        Camera.position = position;
    }

    public static Vector3f getRotation() {
        return rotation;
    }

    public static void setRotation(Vector3f rotation) {
        Camera.rotation = rotation;
    }
}
