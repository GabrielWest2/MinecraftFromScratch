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

import java.util.HashMap;

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
        UIManager.setActiveMenu((UIMenu) null);
        GameLogic.skyboxModel = ModelCreator.createSkyboxModel(new String[]{"right", "left", "top", "bottom", "back", "front"});
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

        int i = 0;
        for (int x = lastChunk.x - RENDER_DISTANCE - 1; x <= lastChunk.x + RENDER_DISTANCE; x++) {
            for (int y = lastChunk.y - RENDER_DISTANCE - 1; y <= lastChunk.y + RENDER_DISTANCE; y++) {

                if (!(x > lastChunk.x - RENDER_DISTANCE && x < lastChunk.x + RENDER_DISTANCE)) {
                    if (!(y > lastChunk.y - RENDER_DISTANCE && y < lastChunk.y + RENDER_DISTANCE)) {
                        i++;
                        if (chunks.get(new Vector2i(x, y)) != null) {
                            chunks.get(new Vector2i(x, y)).serializeChunk();

                        }
                    }
                }
            }
        }
        System.out.println(i);
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
}
