package voxelengine.game;

import org.joml.Vector3i;
import org.joml.Vector4f;
import voxelengine.Camera;
import voxelengine.Renderer;
import voxelengine.gui.ScreenConstraint;
import voxelengine.gui.Side;
import voxelengine.gui.Text;
import voxelengine.gui.UIManager;
import voxelengine.input.Mouse;
import voxelengine.model.ModelCreator;
import voxelengine.model.ModelManager;
import voxelengine.model.SkyboxModel;
import voxelengine.texture.TextureLoader;
import voxelengine.util.Time;
import voxelgame.World;
import voxelgame.block.Block;
import voxelgame.material.Material;

public class GameLogic {
    private static final Text fps = new Text(new ScreenConstraint(null).addConstraint(Side.LEFT, "10px").addConstraint(Side.TOP, "10px"), "60 FPS", 25, new Vector4f(1, 1, 1, 0.5f));
    public static SkyboxModel skyboxModel;

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
        skyboxModel = ModelCreator.createSkyboxModel(new String[]{"panorama_3", "panorama_1", "panorama_4", "panorama_5", "panorama_2", "panorama_0"});
        //World.init();
    }

    public static void update() {
        Renderer.beginFrame();
        Time.update();
        Mouse.update();
        World.update();
        //Game code goes here

        if (Mouse.isMousePressed(0)) {
            World.setBlock(new Vector3i((int) Camera.getPosition().x, (int) Camera.getPosition().y, (int) Camera.getPosition().z), new Block(Material.LOG));
        }

        Renderer.Render(skyboxModel);
        UIManager.render();
        fps.setText(Time.getFPS() + " FPS");
        fps.Render();
        Renderer.endFrame();
    }
}
