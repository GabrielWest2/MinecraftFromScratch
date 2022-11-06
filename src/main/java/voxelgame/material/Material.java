package voxelgame.material;

import org.joml.Vector2i;
import voxelgame.block.BlockTexture;

/**
 * @author gabed
 * @Date 10/12/2022
 */
public enum Material {
    AIR(0, "Air", null, true),
    DIRT(1, "Dirt", new BlockTexture(new Vector2i(7, 2)), false),
    STONE(2, "Stone", new BlockTexture(new Vector2i(31, 15)), false),
    PUMPKIN(3, "Pumpkin", new BlockTexture(new Vector2i(26, 28), new Vector2i(26, 25), new Vector2i(26, 26), new Vector2i(26, 26), new Vector2i(26, 26), new Vector2i(26, 26)), true),
    GRASS(4, "Grass Block", new BlockTexture(new Vector2i(15, 23), new Vector2i(7, 2), new Vector2i(15, 14), new Vector2i(15, 14), new Vector2i(15, 14), new Vector2i(15, 14)), false),
    SAND(5, "Sand", new BlockTexture(new Vector2i(29, 10)), false),
    BEDROCK(6, "Bedrock", new BlockTexture(new Vector2i(1, 17)), false),
    LEAF(7, "Leaf Block", new BlockTexture(new Vector2i(1, 0)), true),
    LOG(8, "Leaf Block", new BlockTexture(new Vector2i(31, 2), new Vector2i(31, 0)), false),
    IRON_ORE(9, "Iron Ore", new BlockTexture(new Vector2i(7, 1)), false);

    final int id;
    final String displayName;
    final BlockTexture texture;

    //Whether it culls neighboring block faces.
    //Transparent does NOT cull faces.
    final boolean isTransparent;

    Material(int id, String displayName, BlockTexture texture, boolean isTransparent) {
        this.id = id;
        this.displayName = displayName;
        this.texture = texture;
        this.isTransparent = isTransparent;
    }

    public Material getMaterialFromId(int id) {
        for (Material m : Material.values()) {
            if (m.id == id)
                return m;
        }
        return null;
    }

    public int getId() {
        return id;
    }

    public String getDisplayName() {
        return displayName;
    }

    public BlockTexture getTexture() {
        return texture;
    }

    public boolean isTransparent() {
        return isTransparent;
    }
}
