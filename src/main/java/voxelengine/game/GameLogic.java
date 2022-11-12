package voxelengine.game;

import org.joml.Vector3i;
import org.joml.Vector4f;
import voxelengine.Camera;
import voxelengine.Framebuffer;
import voxelengine.Renderer;
import voxelengine.engine.DisplayManager;
import voxelengine.gui.ScreenConstraint;
import voxelengine.gui.Side;
import voxelengine.gui.Text;
import voxelengine.gui.UIManager;
import voxelengine.input.Keyboard;
import voxelengine.input.Mouse;
import voxelengine.model.ModelCreator;
import voxelengine.model.ModelManager;
import voxelengine.model.SkyboxModel;
import voxelengine.texture.TextureLoader;
import voxelengine.util.Time;
import voxelgame.World;
import voxelgame.block.Block;
import voxelgame.material.Material;

import static org.lwjgl.glfw.GLFW.*;

public class GameLogic {
    public static Material selectedMaterial = Material.LOG;

    public static void shutdown() {
        System.out.println("Shutting down...");
        Renderer.cleanUp();
        TextureLoader.cleanUp();
        ModelCreator.cleanUp();
        System.exit(-1);
    }

    public static void start() {
        ModelManager.LoadTextures();
        ModelManager.LoadModels();
        Renderer.Prepare();
        UIManager.init();
    }

    public static void update() {
        Renderer.beginFrame();
        Time.update();
        Mouse.update();
        World.update();
        World.render();
        //Game code goes here
        if (Mouse.isMousePressed(0)) {
            World.setBlock(new Vector3i((int) Camera.getPosition().x, (int) Camera.getPosition().y, (int) Camera.getPosition().z), new Block(selectedMaterial));
        }
        for(int i = 0; i < 9; i++){
            if(Keyboard.isKeyPressed(GLFW_KEY_0 + i + 1))
                UIManager.setHotbar(i);
        }
        Renderer.endFrame();
    }
}
