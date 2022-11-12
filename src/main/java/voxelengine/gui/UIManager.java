package voxelengine.gui;

import org.joml.Vector2i;
import org.joml.Vector3f;
import org.joml.Vector4f;
import voxelengine.Camera;
import voxelengine.game.GameLogic;
import voxelengine.gui.callback.QuitgameCallback;
import voxelengine.gui.callback.SaveWorldCallback;
import voxelengine.gui.callback.SingleplayerCallback;
import voxelengine.gui.callback.TitlescreenCallback;
import voxelengine.util.Time;
import voxelgame.SaveWorld;
import voxelgame.World;
import voxelgame.material.Material;

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
    private static final Text fps = new Text(new ScreenConstraint(null).addConstraint(Side.LEFT, "10px").addConstraint(Side.TOP, "10px"), "60 FPS", 25, new Vector4f(1, 1, 1, 0.5f));
    private static int slot = 0;
    public static void addMenu(String name, UIMenu menu) {
        menus.put(name, menu);
    }

    public static void render() {
        if (activeMenu == menus.get("titlescreen")) {
            Camera.setRotation(new Vector3f(0, (Camera.getRotation().y - 0.025f) % 360, 0));

        }else if (activeMenu == menus.get("game")) {
            updateInGameHUD();
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
        createInGameHUD();
    }

    public static void incrementHotbar(){
        System.out.println("increase");
        slot++;
        if(slot > 8)
            slot -= 9;
    }
    public static void decrementHotbar(){
        slot--;
        if(slot < 0)
            slot += 9;
    }
    public static void setHotbar(int i){
        slot = i;
    }

    public static void updateInGameHUD(){
        if(!highlight.getConstraints().getConstraint(Side.LEFT).equals((40* slot)+"px")) {
            highlight.getConstraints().addConstraint(Side.LEFT, (40 * slot) + "px");
            highlight.resetVAO();
            switch (slot){
                case 0:
                    GameLogic.selectedMaterial = Material.AIR;
                    break;
                case 1:
                    GameLogic.selectedMaterial = Material.DIRT;
                    break;
                case 2:
                    GameLogic.selectedMaterial = Material.STONE;
                    break;
                case 3:
                    GameLogic.selectedMaterial = Material.GRASS;
                    break;
                case 4:
                    GameLogic.selectedMaterial = Material.LOG;
                    break;
                case 5:
                    GameLogic.selectedMaterial = Material.BEDROCK;
                    break;
                case 6:
                    GameLogic.selectedMaterial = Material.LEAF;
                    break;
                case 7:
                    GameLogic.selectedMaterial = Material.SAND;
                    break;
                case 8:
                    GameLogic.selectedMaterial = Material.PUMPKIN;
                    break;
            }
        }
    }
    static Image highlight;
    private static void createTitlescreen() {
        UIMenu menu = new UIMenu();
        TextureButton button = new TextureButton(new ScreenConstraint(null).addConstraint(Side.CENTER_X, "0px").addConstraint(Side.CENTER_Y, "25px"), "Singleplayer", new Vector2i(450, 50), new SingleplayerCallback());
        TextureButton button2 = new TextureButton(new ScreenConstraint(button).addConstraint(Side.TOP, "55px"), "Multiplayer", new Vector2i(450, 50), false);
        TextureButton button3 = new TextureButton(new ScreenConstraint(button2).addConstraint(Side.TOP, "100px"), "Options", new Vector2i(197 + 25, 50), false);
        TextureButton button4 = new TextureButton(new ScreenConstraint(button3).addConstraint(Side.LEFT, "228px"), "Quit Game", new Vector2i(197 + 25, 50), new QuitgameCallback());
        Text version = new Text(new ScreenConstraint(null).addConstraint(Side.LEFT, "5px").addConstraint(Side.BOTTOM, "5px"), "MCFS 1.0", 20, new Vector4f(1, 1, 1, 1));
        //Text copyright = new Text(new ScreenConstraint(null).addConstraint(Side.RIGHT, "5px").addConstraint(Side.BOTTOM, "5px"), "Copyright 2022 GABE", 20, new Vector4f(1, 1, 1, 1));
        Image title = new Image(new ScreenConstraint(null).addConstraint(Side.CENTER_X, "0px").addConstraint(Side.CENTER_Y, "-30%"), 4, new Vector2i((int) (570 * 1.2), (int) (100 * 1.2)));
        //Image title2 = new Image(new ScreenConstraint(title).addConstraint(Side.LEFT, "75px").addConstraint(Side.TOP, "100px"), 5, new Vector2i((int) (719 * 0.75f), (int) (75 * 0.75f)));
        menu.addElement(button);
        menu.addElement(button2);
        menu.addElement(button3);
        menu.addElement(button4);
        menu.addElement(version);
        //menu.addElement(copyright);
        menu.addElement(title);
        //menu.addElement(title2);
        UIManager.addMenu("titlescreen", menu);
        UIManager.setActiveMenu("titlescreen");
    }

    private static void createInGameHUD(){
        UIMenu menu = new UIMenu();

        Image hotbar = new Image(new ScreenConstraint(null).addConstraint(Side.CENTER_X, "0px").addConstraint(Side.BOTTOM, "20px"), 11, new Vector2i(364, 44));
        menu.addElement(hotbar);
        highlight = new Image(new ScreenConstraint(hotbar).addConstraint(Side.LEFT, (40*4)+"px"), 12, new Vector2i(44, 44));
        menu.addElement(highlight);
        UVImage slot1 = new UVImage(new ScreenConstraint(hotbar).addConstraint(Side.LEFT, "7px").addConstraint(Side.TOP, "7px"), 6, new Vector2i(30, 30), generateUVs(Material.GRASS.getTexture().getFaceTexture(4)));
        UVImage slot2 = new UVImage(new ScreenConstraint(slot1).addConstraint(Side.LEFT, "40px"),0, new Vector2i(30, 30), generateUVs(Material.DIRT.getTexture().getFaceTexture(4)));
        UVImage slot3 = new UVImage(new ScreenConstraint(slot2).addConstraint(Side.LEFT, "40px"),0, new Vector2i(30, 30), generateUVs(Material.STONE.getTexture().getFaceTexture(4)));
        UVImage slot4 = new UVImage(new ScreenConstraint(slot3).addConstraint(Side.LEFT, "40px"),0, new Vector2i(30, 30), generateUVs(Material.GRASS.getTexture().getFaceTexture(4)));
        UVImage slot5 = new UVImage(new ScreenConstraint(slot4).addConstraint(Side.LEFT, "40px"),0, new Vector2i(30, 30), generateUVs(Material.LOG.getTexture().getFaceTexture(4)));
        UVImage slot6 = new UVImage(new ScreenConstraint(slot5).addConstraint(Side.LEFT, "40px"),0, new Vector2i(30, 30), generateUVs(Material.BEDROCK.getTexture().getFaceTexture(4)));
        UVImage slot7 = new UVImage(new ScreenConstraint(slot6).addConstraint(Side.LEFT, "40px"),0, new Vector2i(30, 30), generateUVs(Material.LEAF.getTexture().getFaceTexture(4)));
        UVImage slot8 = new UVImage(new ScreenConstraint(slot7).addConstraint(Side.LEFT, "40px"),0, new Vector2i(30, 30), generateUVs(Material.SAND.getTexture().getFaceTexture(4)));
        UVImage slot9 = new UVImage(new ScreenConstraint(slot8).addConstraint(Side.LEFT, "40px"),0, new Vector2i(30, 30), generateUVs(Material.PUMPKIN.getTexture().getFaceTexture(4)));
        Crosshair crosshair = new Crosshair(13, new Vector2i(20, 20));
        menu.addElement(crosshair);
        menu.addElement(slot1);
        menu.addElement(slot2);
        menu.addElement(slot3);
        menu.addElement(slot4);
        menu.addElement(slot5);
        menu.addElement(slot6);
        menu.addElement(slot7);
        menu.addElement(slot8);
        menu.addElement(slot9);

        menu.addElement(new TextureButton(new ScreenConstraint(null).addConstraint(Side.RIGHT, "10px").addConstraint(Side.TOP, "10px"), "Leave", new Vector2i(150, 50), new SaveWorldCallback()));
        UIManager.addMenu("game", menu);
    }

    private static float[] generateUVs(Vector2i pos) {
        float scale = 1/32f;
        return new float[]{
                pos.x * scale, (pos.y + 1) * scale,
                pos.x * scale, pos.y * scale,
                (pos.x + 1) * scale, (pos.y + 1) * scale,
                (pos.x + 1) * scale, pos.y * scale
        };
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
        TextureButton button2 = new TextureButton(new ScreenConstraint(play).addConstraint(Side.TOP, "55px"), "Create New World", new Vector2i(450, 50), false);
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

    public static void renderDynamicElements() {
        fps.setText(Time.getFPS() + " FPS");
        fps.Render();
    }
}
