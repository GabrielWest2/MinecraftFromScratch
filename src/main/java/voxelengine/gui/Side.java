package voxelengine.gui;

public enum Side {
    TOP(1), BOTTOM(0), LEFT(3), RIGHT(2), CENTER_X(5), CENTER_Y(4);
    final int opposite;

    Side(int _opposite) {
        opposite = _opposite;
    }
}
