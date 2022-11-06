package voxelengine.gui.callback;

import voxelengine.game.GameLogic;

/**
 * @author gabed
 * @Date 10/18/2022
 */
public class QuitgameCallback implements Callback {
    @Override
    public void call() {
        GameLogic.shutdown();
    }
}
