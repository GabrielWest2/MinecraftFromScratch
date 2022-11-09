package voxelengine.gui.callback;

import voxelengine.Renderer;
import voxelengine.game.GameLogic;
import voxelengine.gui.UIManager;
import voxelengine.model.ModelCreator;
import voxelgame.World;

/**
 * @author gabed
 * @Date 11/6/2022
 */
public class SaveWorldCallback implements Callback {

    @Override
    public void call() {
        World.saveAndExit();
        Renderer.setSkybox(0);
        UIManager.setActiveMenu("titlescreen");
    }
}
