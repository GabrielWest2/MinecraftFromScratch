package voxelengine.gui.callback;

import org.joml.Vector3f;
import voxelengine.Camera;
import voxelengine.gui.UIManager;

/**
 * @author gabed
 * @Date 10/23/2022
 */
public class TitlescreenCallback implements Callback {
    @Override
    public void call() {
        Camera.setPosition(new Vector3f(0, 0, 0));
        UIManager.setActiveMenu("titlescreen");
    }
}
