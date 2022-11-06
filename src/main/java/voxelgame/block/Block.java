package voxelgame.block;

import voxelgame.material.Material;

/**
 * @author gabed
 * @Date 10/12/2022
 */
public class Block {
    private final Material material;
    private BlockData customBlockData;

    public Block(Material material) {
        this.material = material;
        customBlockData = new BlockData();
    }

    public BlockData getCustomBlockData() {
        return customBlockData;
    }

    public void setCustomBlockData(BlockData customBlockData) {
        this.customBlockData = customBlockData;
    }

    public Material getMaterial() {
        return material;
    }
}
