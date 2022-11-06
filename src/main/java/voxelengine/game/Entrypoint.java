package voxelengine.game;

import voxelengine.engine.DisplayManager;

import java.util.Objects;

import static org.lwjgl.glfw.Callbacks.glfwFreeCallbacks;
import static org.lwjgl.glfw.GLFW.*;

/**
 * @author gabed
 * @Date 8/9/2022
 */
public class Entrypoint {
    public static void main(String[] args) {
        DisplayManager.createWindow();
        GameLogic.start();
        while (!glfwWindowShouldClose(DisplayManager.getWindow())) {
            GameLogic.update();
            glfwSwapBuffers(DisplayManager.getWindow());
            glfwPollEvents();
        }
        GameLogic.shutdown();
        // Free the window callbacks and destroy the window
        glfwFreeCallbacks(DisplayManager.getWindow());
        glfwDestroyWindow(DisplayManager.getWindow());

        // Terminate GLFW and free the error callback
        glfwTerminate();
        Objects.requireNonNull(glfwSetErrorCallback(null)).free();
    }
}
