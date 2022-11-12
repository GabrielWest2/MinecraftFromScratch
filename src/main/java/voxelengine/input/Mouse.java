package voxelengine.input;

import org.joml.Vector2f;
import org.joml.Vector2i;
import voxelengine.gui.ScrollFrame;
import voxelengine.gui.UIManager;

import static org.lwjgl.glfw.GLFW.*;

public class Mouse {
    private static final boolean[] mouseButtons = new boolean[GLFW_MOUSE_BUTTON_LAST];
    private static float x = 0, y = 0, dx = 0, dy = 0, lastX = 0, lastY = 0, dsx = 0, dsy = 0;

    /**
     * Updates the mouse's position and deltas
     */
    public static void update() {
        dx = x - lastX;
        dy = y - lastY;
        lastX = x;
        lastY = y;
    }

    /**
     * @return Returns true if the given button is pressed
     */
    public static boolean isMousePressed(int i) {
        return mouseButtons[i];
    }

    /**
     * @return Returns the position of the mouse cursor as a Vector2i
     */
    public static Vector2i getMousePosInt() {
        return new Vector2i((int) Mouse.getMouseX(), (int) Mouse.getMouseY());
    }

    /**
     * @return Returns the position of the mouse cursor
     */
    private static Vector2f getMousePos() {
        return new Vector2f(x, y);
    }

    /**
     * @return Returns the change in the mouses x position from last frame
     */
    public static float getMouseDx() {
        return dx;
    }

    /**
     * @return Returns the change in the mouses y position from last frame
     */
    public static float getMouseDy() {
        return dy;
    }


    public static void MouseButtonCallback(long window, int button, int action, int mods) {
        if (action == GLFW_PRESS) {
            mouseButtons[button] = true;
        } else if (action == GLFW_RELEASE) {
            mouseButtons[button] = false;
        }
    }

    public static void MouseScrollCallback(long window, double scrollX, double scrollY) {
        dsx = (float) scrollX;
        dsy = (float) scrollY;
        ScrollFrame.updateScroll(dsy / -55f);
        if(scrollY < 0)
            UIManager.incrementHotbar();
        if(scrollY > 0)
            UIManager.decrementHotbar();
    }

    public static void MouseMoveCallback(long window, double mouseX, double mouseY) {
        x = (float) mouseX;
        y = (float) mouseY;
    }

    public static float getMouseX() {
        return x;
    }

    public static float getMouseY() {
        return y;
    }

    public static float getMouseScrollX() {
        return dsx;
    }

    public static float getMouseScrollY() {
        return dsy;
    }
}
