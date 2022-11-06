package voxelgame.chunk;

import org.joml.Vector2i;
import org.joml.Vector3i;

/**
 * @author gabed
 * @Date 10/16/2022
 */
public class ChunkPoint {
    public final Vector2i chunk;
    public final Vector3i block;

    public ChunkPoint(Vector2i chunk, Vector3i block) {
        this.chunk = chunk;
        this.block = block;
    }
}
