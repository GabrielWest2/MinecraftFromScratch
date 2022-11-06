package voxelengine.engine;

import org.lwjgl.glfw.GLFWErrorCallback;
import org.lwjgl.glfw.GLFWVidMode;
import org.lwjgl.opengl.GL;
import org.lwjgl.system.MemoryStack;
import voxelengine.Framebuffer;
import voxelengine.Renderer;
import voxelengine.gui.UIManager;
import voxelengine.input.Keyboard;
import voxelengine.input.Mouse;

import java.nio.IntBuffer;

import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL11.glViewport;
import static org.lwjgl.system.MemoryStack.stackPush;

/**
 * @author gabed
 * @Date 8/9/2022
 */
public class DisplayManager {
    public static final int FPS_CAP = 120;
    public static final boolean VSYNC = true;
    public static final boolean FULLSCREEN = false;
    public static final float FAR_PLANE = 1000f;
    public static final float NEAR_PLANE = 0.1f;
    public static final float FOV = 90f;
    public static int WIDTH = 1280;
    public static int HEIGHT = 720;
    private static long window = -1;

    public static void createWindow() {
        GLFWErrorCallback.createPrint(System.err).set();

        // Initialize GLFW. Most GLFW functions will not work before doing this.
        if (!glfwInit())
            throw new IllegalStateException("Unable to initialize GLFW");

        // Configure GLFW
        glfwDefaultWindowHints(); // optional, the current window hints are already the default
        glfwWindowHint(GLFW_VISIBLE, GLFW_FALSE); // the window will stay hidden after creation
        glfwWindowHint(GLFW_RESIZABLE, GLFW_TRUE); // the window will be resizable
        glfwWindowHint(GLFW_MAXIMIZED, FULLSCREEN ? GLFW_TRUE : GLFW_FALSE); // the window will be resizable

        // Create the window
        window = glfwCreateWindow(WIDTH, HEIGHT, "Hello World!", 0, 0);
        if (window == -1)
            throw new RuntimeException("Failed to create the GLFW window");

        // Setup a key callback. It will be called every time a key is pressed, repeated or released.
        glfwSetKeyCallback(window, (window, key, scancode, action, mods) -> {
            if (key == GLFW_KEY_ESCAPE && action == GLFW_RELEASE)
                glfwSetWindowShouldClose(window, true); // We will detect this in the rendering loop
        });

        // Get the thread stack and push a new frame
        try (MemoryStack stack = stackPush()) {
            IntBuffer pWidth = stack.mallocInt(1); // int*
            IntBuffer pHeight = stack.mallocInt(1); // int*

            // Get the window size passed to glfwCreateWindow
            glfwGetWindowSize(window, pWidth, pHeight);

            // Get the resolution of the primary monitor
            GLFWVidMode vidmode = glfwGetVideoMode(glfwGetPrimaryMonitor());

            // Center the window
            assert vidmode != null;
            glfwSetWindowPos(
                    window,
                    (vidmode.width() - pWidth.get(0)) / 2,
                    (vidmode.height() - pHeight.get(0)) / 2
            );
        } // the stack frame is popped automatically

        // Make the OpenGL context current
        glfwMakeContextCurrent(window);

        if (VSYNC)// Enable v-sync
            glfwSwapInterval(1);

        // Make the window visible
        glfwShowWindow(window);


        glfwSetWindowSizeCallback(window, (window, width, height) -> {
            if (width != 0 && height != 0) {
                WIDTH = width;
                HEIGHT = height;
                glViewport(0, 0, width, height);
                Framebuffer.setFrameBufferSize(WIDTH, HEIGHT);
                Renderer.UpdateProjection();
                UIManager.recalculateSizes();
            }
        });
        glfwSetKeyCallback(window, Keyboard::KeyPressCallback);
        glfwSetMouseButtonCallback(window, Mouse::MouseButtonCallback);
        glfwSetCursorPosCallback(window, Mouse::MouseMoveCallback);
        glfwSetScrollCallback(window, Mouse::MouseScrollCallback);
        GL.createCapabilities();
    }

    public static long getWindow() {
        return window;
    }
}
