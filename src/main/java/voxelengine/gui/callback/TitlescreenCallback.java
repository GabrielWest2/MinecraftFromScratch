package voxelengine.gui.callback;

import voxelengine.gui.UIManager;

/**
 * @author gabed
 * @Date 10/23/2022
 */
public class TitlescreenCallback implements Callback {
    @Override
    public void call() {
        UIManager.setActiveMenu("titlescreen");
    }
}
