package voxelgame;

import org.joml.Vector2i;
import org.joml.Vector3i;
import voxelengine.Camera;
import voxelengine.Renderer;
import voxelengine.game.GameLogic;
import voxelengine.gui.UIManager;
import voxelengine.gui.UIMenu;
import voxelengine.model.ModelCreator;
import voxelgame.block.Block;
import voxelgame.chunk.Chunk;
import voxelgame.chunk.ChunkPoint;

import java.io.IOException;
import java.io.PrintWriter;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;

/**
 * @author gabed
 * @Date 10/16/2022
 */
public class World {
    private static final int RENDER_DISTANCE = 10;
    private static HashMap<Vector2i, Chunk> chunks;
    private static Vector2i lastChunk = new Vector2i(0, 0);
    private static SaveWorld world;

    public static void init() {
        chunks = new HashMap<>();
        lastChunk = new Vector2i((int) (Camera.getPosition().x / 16), (int) (Camera.getPosition().z / 16));
        updateChunks();
    }

    public static SaveWorld getWorld() {
        return world;
    }

    public static void setWorld(SaveWorld world) {
        World.world = world;
        chunks = new HashMap<>();
        updateChunks();
        UIManager.setActiveMenu("game");
        Renderer.setSkybox(1);
    }

    public static void update() {
        if (world == null)
            return;
        Vector2i currentChunk = new Vector2i((int) (Camera.getPosition().x / 16), (int) (Camera.getPosition().z / 16));
        if (lastChunk != currentChunk) {
            lastChunk = currentChunk;
            updateChunks();
        } else {
            Camera.update();
        }
    }

    public static void render() {
        if(world==null)
            return;
        for (int x = lastChunk.x - RENDER_DISTANCE; x < lastChunk.x + RENDER_DISTANCE; x++) {
            for (int y = lastChunk.y - RENDER_DISTANCE; y < lastChunk.y + RENDER_DISTANCE; y++) {
                Renderer.Render(chunks.get(new Vector2i(x, y)).getChunkModel());
            }
        }
    }

    public static void setBlock(Vector3i position, Block block) {
        ChunkPoint point = convertWorldPointToChunkPoint(position);
        if (chunks == null) return;
        Chunk c = chunks.get(point.chunk);
        if (c == null) return;
        c.setBlock(point.block, block);
        c.generateModel();
    }

    public static Block getBlock(Vector3i position) {
        ChunkPoint point = convertWorldPointToChunkPoint(position);
        Chunk c = chunks.get(point.chunk);
        return c.getBlock(position);
    }

    private static ChunkPoint convertWorldPointToChunkPoint(Vector3i position) {

        Vector2i chunk = new Vector2i(position.x / 16, position.z / 16);
        Vector3i sub = new Vector3i(position.x - chunk.x * 16, position.y, position.z - chunk.y * 16);
        if (position.x < 1) {
            chunk.x -= 1;
            sub.x = position.x - 1 - (chunk.x) * 16;
        }
        if (position.z < 1) {
            chunk.y -= 1;
            sub.z = position.z - 1 - (chunk.y) * 16;
        }

        /*Vector2i chunk = new Vector2i(position.x/16, position.z/16);
        Vector3i sub = new Vector3i(position.x % 16, position.y, position.z % 16);
        if(position.x < 0){
            sub.x = 16 + position.x % (-16);
            chunk.x -= 1;
        }
        if(position.z < 0){
            sub.z = 16 + position.z % (-16);
            chunk.y -= 1;
        }*/
        return new ChunkPoint(chunk, sub);
    }


    private static void updateChunks() {
        for (int x = lastChunk.x - RENDER_DISTANCE - 1; x < lastChunk.x + RENDER_DISTANCE + 2; x++) {
            for (int y = lastChunk.y - RENDER_DISTANCE - 1; y < lastChunk.y + RENDER_DISTANCE + 2; y++) {
                if (!chunks.containsKey(new Vector2i(x, y))) {
                    chunks.put(new Vector2i(x, y), world.loadChunk(new Vector2i(x, y)));
                }
            }
        }
    }

    public static HashMap<Vector2i, Chunk> getChunks() {
        return chunks;
    }

    public static void saveAndExit() {
        try {
            Locale loc = new Locale.Builder().setLanguage("en").setRegion("US").build();
            PrintWriter writer = new PrintWriter(World.getWorld().getPath() + "/world.wfdesc", StandardCharsets.UTF_8);
            writer.println(world.getName());
            writer.println(world.getVersion());
            writer.println(world.getDifficulty());
            writer.println(world.getSeed());
            String pattern = "dd/M/yy hh:mm a";
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern);
            writer.println(simpleDateFormat.format(new Date()));
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        chunks.values().forEach(Chunk::serializeChunk);
        chunks.clear();
        world = null;
    }
}
