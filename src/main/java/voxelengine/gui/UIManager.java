package voxelengine.gui;

import org.joml.Vector2i;
import org.joml.Vector3f;
import org.joml.Vector4f;
import voxelengine.Camera;
import voxelengine.gui.callback.QuitgameCallback;
import voxelengine.gui.callback.SingleplayerCallback;
import voxelengine.gui.callback.TitlescreenCallback;
import voxelgame.SaveWorld;
import voxelgame.World;

import java.io.File;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Objects;

/**
 * @author gabed
 * @Date 10/18/2022
 */
public class UIManager {
    private static final HashMap<String, UIMenu> menus = new HashMap<>();
    private static UIMenu activeMenu;
    private static int worldTab = 0;
    private static TextureButton play;

    public static void addMenu(String name, UIMenu menu) {
        menus.put(name, menu);
    }

    public static void render() {
        if (activeMenu == menus.get("titlescreen")) {
            Camera.rotateBy(new Vector3f(0, -0.025f, 0));
            Camera.setRotation(new Vector3f(Camera.getRotation().x, Camera.getRotation().y % 360, Camera.getRotation().z));
        } else {
            Camera.update();
        }
        if (activeMenu == null)
            return;
        activeMenu.render();
    }

    public static void recalculateSizes() {
        menus.values().forEach(UIMenu::resize);
    }

    public static void init() {
        createTitlescreen();
        createSingleplayerScreen();
    }

    private static void createTitlescreen() {
        UIMenu menu = new UIMenu();
        TextureButton button = new TextureButton(new ScreenConstraint(null).addConstraint(Side.CENTER_X, "0px").addConstraint(Side.CENTER_Y, "25px"), "Singleplayer", new Vector2i(450, 50), new SingleplayerCallback());
        TextureButton button2 = new TextureButton(new ScreenConstraint(button).addConstraint(Side.TOP, "55px"), "Multiplayer", new Vector2i(450, 50));
        TextureButton button3 = new TextureButton(new ScreenConstraint(button2).addConstraint(Side.TOP, "100px"), "Options", new Vector2i(197 + 25, 50));
        TextureButton button4 = new TextureButton(new ScreenConstraint(button3).addConstraint(Side.LEFT, "228px"), "Quit Game", new Vector2i(197 + 25, 50), new QuitgameCallback());
        Text version = new Text(new ScreenConstraint(null).addConstraint(Side.LEFT, "5px").addConstraint(Side.BOTTOM, "5px"), "Minecraft 1.0", 20, new Vector4f(1, 1, 1, 1));
        Text copyright = new Text(new ScreenConstraint(null).addConstraint(Side.RIGHT, "5px").addConstraint(Side.BOTTOM, "5px"), "Copyright 2022 GABE", 20, new Vector4f(1, 1, 1, 1));
        Image title = new Image(new ScreenConstraint(null).addConstraint(Side.CENTER_X, "0px").addConstraint(Side.CENTER_Y, "-30%"), 4, new Vector2i((int) (570 * 1.2), (int) (100 * 1.2)));
        Image title2 = new Image(new ScreenConstraint(title).addConstraint(Side.LEFT, "75px").addConstraint(Side.TOP, "100px"), 5, new Vector2i((int) (719 * 0.75f), (int) (75 * 0.75f)));
        menu.addElement(button);
        menu.addElement(button2);
        menu.addElement(button3);
        menu.addElement(button4);
        menu.addElement(version);
        menu.addElement(copyright);
        menu.addElement(title);
        menu.addElement(title2);
        UIManager.addMenu("titlescreen", menu);
        UIManager.setActiveMenu("titlescreen");
    }

    private static ScrollFrame getCurrentScrollFrame() {
        for (UIElement element : activeMenu.getUIElements()) {
            if (element.getClass().isAssignableFrom(ScrollFrame.class))
                return (ScrollFrame) element;
        }
        return null;
    }

    public static void setSelectedTab(int i) {
        System.out.println("Set tab: " + i);
        if (activeMenu == menus.get("singleplayer")) {
            play.setDisabled(false);
            worldTab = 0;
        }
    }

    private static void createSingleplayerScreen() {
        UIMenu menu = new UIMenu();


        File path = new File("worlds");

        File[] files = Arrays.stream(Objects.requireNonNull(path.listFiles())).filter(File::isDirectory).toArray(File[]::new);
        WorldTab[] savedWorlds = new WorldTab[files.length];
        for (int i = 0; i < files.length; i++) {
            savedWorlds[i] = (new WorldTab(i, new SaveWorld(files[i].getPath())));
        }

        ScrollFrame frame = new ScrollFrame(100, 200, savedWorlds);

        menu.addElement(frame);

        menu.addElement(new Rect(new ScreenConstraint(null).addConstraint(Side.BOTTOM, "200px"), new Vector2i(10000, 15), new Vector4f(0, 0, 0, 0.25f)));
        menu.addElement(new Rect(new ScreenConstraint(null).addConstraint(Side.TOP, "100px"), new Vector2i(10000, 15), new Vector4f(0, 0, 0, 0.25f)));

        menu.addElement(new Image(new ScreenConstraint(null), 7, new Vector2i(100, 100), true, false, true));//Foreground
        menu.addElement(new Image(new ScreenConstraint(null).addConstraint(Side.BOTTOM, "0px"), 7, new Vector2i(100, 200), true, false, true));
        menu.addElement(new Text(new ScreenConstraint(null).addConstraint(Side.TOP, "40px").addConstraint(Side.CENTER_X, "0px"), "Select World", 30, new Vector4f(1, 1, 1, 1)));

        play = new TextureButton(new ScreenConstraint(null).addConstraint(Side.CENTER_X, "0px").addConstraint(Side.BOTTOM, "130px"), "Play Selected World", new Vector2i(450, 50), true, () -> World.setWorld(((WorldTab) frame.getElements()[worldTab]).getWorld()));
        TextureButton button2 = new TextureButton(new ScreenConstraint(play).addConstraint(Side.TOP, "55px"), "Create New World", new Vector2i(450, 50));
        TextureButton button3 = new TextureButton(new ScreenConstraint(button2).addConstraint(Side.TOP, "55px"), "Back", new Vector2i(197 + 25, 50), () -> {
            play.setDisabled(true);
            new TitlescreenCallback().call();
        });
        TextureButton delete = new TextureButton(new ScreenConstraint(button3).addConstraint(Side.LEFT, "228px"), "Delete", new Vector2i(197 + 25, 50), true);

        menu.addElement(play);
        menu.addElement(button2);
        menu.addElement(button3);
        menu.addElement(delete);
        UIManager.addMenu("singleplayer", menu);
    }

    public static HashMap<String, UIMenu> getMenus() {
        return menus;
    }

    public static UIMenu getActiveMenu() {
        return activeMenu;
    }

    public static void setActiveMenu(UIMenu menu) {
        activeMenu = menu;
    }

    public static void setActiveMenu(String name) {
        activeMenu = menus.get(name);
    }
}
