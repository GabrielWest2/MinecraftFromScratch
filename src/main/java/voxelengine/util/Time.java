package voxelengine.util;

public class Time {
    private static final int[] lastFps;
    private static long lastFrame;
    private static float dt;

    static {
        lastFps = new int[100];
        for(int i = 0 ; i < 100; i ++)
            lastFps[i] = 60;
        lastFrame = System.currentTimeMillis();
    }

    public static void update() {
        dt = (System.currentTimeMillis() - lastFrame) / 1000f;
        lastFrame = System.currentTimeMillis();
        System.arraycopy(lastFps, 0, lastFps, 1, lastFps.length - 1);
        lastFps[0] = (int) (1f / dt);
    }

    public static int getFPS() {
        int t = 0;
        for (int i = 0; i < 100; i++) {
            t += lastFps[i];
        }
        return t / 100;
    }

    public static float getDeltaTime() {
        return dt;
    }
}
