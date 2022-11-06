package voxelengine.input;

import java.util.ArrayList;
import java.util.List;

import static org.lwjgl.glfw.GLFW.GLFW_PRESS;
import static org.lwjgl.glfw.GLFW.GLFW_RELEASE;

public class Keyboard {
    private static final List<Integer> keys = new ArrayList<>();

    /**
     * Returns true if the given key is pressed
     */
    public static boolean isKeyPressed(int glfwKey) {
        return keys.contains(glfwKey);
    }

    public static void KeyPressCallback(long window, int key, int scancode, int action, int mods) {
        if (action == GLFW_PRESS) {
            keys.add(key);
        } else if (action == GLFW_RELEASE) {
            keys.remove((Integer) key);
        }
    }
}
