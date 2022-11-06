package voxelgame.chunk;

import org.joml.Vector2f;
import org.joml.Vector2i;
import org.joml.Vector3f;
import org.joml.Vector3i;
import voxelengine.model.ModelCreator;
import voxelengine.model.TexturedModel;
import voxelengine.texture.TextureLoader;
import voxelengine.util.FastNoiseLite;
import voxelgame.World;
import voxelgame.block.Block;
import voxelgame.material.Material;

import java.io.IOException;
import java.io.PrintWriter;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * @author gabed
 * @Date 10/12/2022
 */
public class Chunk {
    private static final float noiseScale = 2f;
    private static final float caveScale = 10f;
    private static final float noiseAmplitude = 20f;
    private static int seed;
    private final Block[][][] blocks = new Block[16][128][16];
    private final int chunkX, chunkY;
    private final HashMap<Vector3i, Block> changedBlocks = new HashMap<>();
    private TexturedModel chunkModel = null;
    private List<Vector3f> vertices = new ArrayList<>();
    private List<Vector2f> textureCoordinates = new ArrayList<>();
    private List<Integer> indices = new ArrayList<>();
    private List<Integer> normalsList = new ArrayList<>();
    private float[] verticesArray;
    private float[] uvArray;
    private int[] indicesArray;
    private float[] normals;
    private boolean hasSerialized = false;

    public Chunk(int chunkX, int chunkY) {
        this.chunkX = chunkX;
        this.chunkY = chunkY;
        generateChunk();
    }

    public Chunk(Vector2i coords) {
        this.chunkX = coords.x;
        this.chunkY = coords.y;
        generateChunk();
    }

    public static void setSeed(int _seed) {
        seed = _seed;
    }

    public void generateModel() {
        textureCoordinates = new ArrayList<>();
        indices = new ArrayList<>();
        normalsList = new ArrayList<>();
        vertices = new ArrayList<>();

        for (int x = 0; x < 16; x++) {
            for (int y = 0; y < 128; y++) {
                for (int z = 0; z < 16; z++) {
                    if (blocks[x][y][z].getMaterial() != Material.AIR) {
                        addBlock(new Vector3i(x, y, z), blocks[x][y][z].getMaterial());
                    }
                }
            }
        }

        updateArrays();

        chunkModel = ModelCreator.loadToTexturedVAO(verticesArray, indicesArray, uvArray, normals, TextureLoader.getTexture(0));
    }

    public TexturedModel getChunkModel() {
        if (chunkModel == null)
            generateModel();
        return chunkModel;
    }

    public Block[][][] getBlocks() {
        return blocks;
    }

    public void setBlock(Vector3i position, Block block) {
        if (blocks[position.x][position.y][position.z].getMaterial() == block.getMaterial())
            return;
        blocks[position.x][position.y][position.z] = block;
        changedBlocks.put(position, block);
        hasSerialized = false;
    }

    public void serializeChunk() {
        if (hasSerialized || changedBlocks.keySet().size() == 0)
            return;
        System.out.println("Seralizing chunk with " + changedBlocks.size() + " differences");

        try {
            PrintWriter writer = new PrintWriter(World.getWorld().getPath() + "/chunks/" + chunkX + "_" + chunkY + ".chk", StandardCharsets.UTF_8);
            for (Vector3i pos : changedBlocks.keySet()) {
                writer.println(pos.x + ";" + pos.y + ";" + pos.z + ";" + changedBlocks.get(pos).getMaterial());
            }
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        hasSerialized = true;
    }

    public Block getBlock(Vector3i position) {
        return blocks[position.x][position.y][position.z];
    }

    private void addBlock(Vector3i position, Material material) {
        boolean top = true, bottom = true, left = true, right = true, front = true, back = true;
        //Cull left and right faces
        if (position.x == 0 || position.x == 15) {
            if (position.x == 0) {
                right = blocks[position.x + 1][position.y][position.z].getMaterial().isTransparent();
                if (World.getChunks().containsKey(new Vector2i(chunkX - 1, chunkY))) {
                    Chunk nextChunk = World.getChunks().get(new Vector2i(chunkX - 1, chunkY));
                    left = nextChunk.getBlocks()[15][position.y][position.z].getMaterial().isTransparent();
                }

            } else {
                left = blocks[position.x - 1][position.y][position.z].getMaterial().isTransparent();
                if (World.getChunks().containsKey(new Vector2i(chunkX + 1, chunkY))) {
                    Chunk nextChunk = World.getChunks().get(new Vector2i(chunkX + 1, chunkY));
                    right = nextChunk.getBlocks()[0][position.y][position.z].getMaterial().isTransparent();
                }
            }
        } else {
            left = blocks[position.x][position.y][position.z].getMaterial().isTransparent() || blocks[position.x - 1][position.y][position.z].getMaterial().isTransparent();
            right = blocks[position.x][position.y][position.z].getMaterial().isTransparent() || blocks[position.x + 1][position.y][position.z].getMaterial().isTransparent();
        }
        //Cull front and back faces
        if (position.z == 0 || position.z == 15) {
            if (position.z == 0) {
                front = blocks[position.x][position.y][position.z + 1].getMaterial().isTransparent();
                if (World.getChunks().containsKey(new Vector2i(chunkX, chunkY - 1))) {
                    Chunk nextChunk = World.getChunks().get(new Vector2i(chunkX, chunkY - 1));
                    back = nextChunk.getBlocks()[position.x][position.y][15].getMaterial().isTransparent();
                }
            } else {
                back = blocks[position.x][position.y][position.z - 1].getMaterial().isTransparent();
                if (World.getChunks().containsKey(new Vector2i(chunkX, chunkY + 1))) {
                    Chunk nextChunk = World.getChunks().get(new Vector2i(chunkX, chunkY + 1));
                    front = nextChunk.getBlocks()[position.x][position.y][0].getMaterial().isTransparent();
                }
            }
        } else {
            back = blocks[position.x][position.y][position.z].getMaterial().isTransparent() || blocks[position.x][position.y][position.z - 1].getMaterial().isTransparent();
            front = blocks[position.x][position.y][position.z].getMaterial().isTransparent() || blocks[position.x][position.y][position.z + 1].getMaterial().isTransparent();
        }
        //Cull top and bottom faces
        if (position.y == 0 || position.y == 255) {
            if (position.y == 0) {
                top = blocks[position.x][position.y + 1][position.z].getMaterial().isTransparent();
            } else {
                bottom = blocks[position.x][position.y - 1][position.z].getMaterial().isTransparent();
            }
        } else {
            bottom = blocks[position.x][position.y][position.z].getMaterial().isTransparent() || blocks[position.x][position.y - 1][position.z].getMaterial().isTransparent();
            top = blocks[position.x][position.y][position.z].getMaterial().isTransparent() || blocks[position.x][position.y + 1][position.z].getMaterial().isTransparent();
        }

        if (top)
            addFace(position, 0, material);
        if (bottom)
            addFace(position, 1, material);
        if (left)
            addFace(position, 2, material);
        if (right)
            addFace(position, 3, material);
        if (front)
            addFace(position, 4, material);
        if (back)
            addFace(position, 5, material);
    }

    private void addFace(Vector3i position, int direction, Material material) {
        position.add(chunkX * 16, 0, chunkY * 16);

        float x = position.x;
        float y = position.y;
        float z = position.z;

        int offset = vertices.size();


        if (direction == 0) {
            vertices.add(new Vector3f(x - 0.5f, y + 0.5f, z + 0.5f));
            vertices.add(new Vector3f(x + 0.5f, y + 0.5f, z + 0.5f));
            vertices.add(new Vector3f(x + 0.5f, y + 0.5f, z - 0.5f));//Top
            vertices.add(new Vector3f(x - 0.5f, y + 0.5f, z - 0.5f));
            normalsList.add(1);
            normalsList.add(1);
            normalsList.add(1);
            normalsList.add(1);
        } else if (direction == 1) {
            vertices.add(new Vector3f(x - 0.5f, y - 0.5f, z - 0.5f));
            vertices.add(new Vector3f(x + 0.5f, y - 0.5f, z - 0.5f));
            vertices.add(new Vector3f(x + 0.5f, y - 0.5f, z + 0.5f));//Bottom
            vertices.add(new Vector3f(x - 0.5f, y - 0.5f, z + 0.5f));
            normalsList.add(2);
            normalsList.add(2);
            normalsList.add(2);
            normalsList.add(2);
        } else if (direction == 2) {
            vertices.add(new Vector3f(x - 0.5f, y + 0.5f, z + 0.5f));
            vertices.add(new Vector3f(x - 0.5f, y + 0.5f, z - 0.5f));//Left
            vertices.add(new Vector3f(x - 0.5f, y - 0.5f, z - 0.5f));
            vertices.add(new Vector3f(x - 0.5f, y - 0.5f, z + 0.5f));
            normalsList.add(3);
            normalsList.add(3);
            normalsList.add(3);
            normalsList.add(3);
        } else if (direction == 3) {
            vertices.add(new Vector3f(x + 0.5f, y + 0.5f, z - 0.5f));
            vertices.add(new Vector3f(x + 0.5f, y + 0.5f, z + 0.5f));//Right
            vertices.add(new Vector3f(x + 0.5f, y - 0.5f, z + 0.5f));
            vertices.add(new Vector3f(x + 0.5f, y - 0.5f, z - 0.5f));
            normalsList.add(4);
            normalsList.add(4);
            normalsList.add(4);
            normalsList.add(4);
        } else if (direction == 4) {
            vertices.add(new Vector3f(x + 0.5f, y + 0.5f, z + 0.5f));//Front
            vertices.add(new Vector3f(x - 0.5f, y + 0.5f, z + 0.5f));
            vertices.add(new Vector3f(x - 0.5f, y - 0.5f, z + 0.5f));
            vertices.add(new Vector3f(x + 0.5f, y - 0.5f, z + 0.5f));
            normalsList.add(5);
            normalsList.add(5);
            normalsList.add(5);
            normalsList.add(5);
        } else if (direction == 5) {
            vertices.add(new Vector3f(x - 0.5f, y + 0.5f, z - 0.5f));
            vertices.add(new Vector3f(x + 0.5f, y + 0.5f, z - 0.5f)); //Back
            vertices.add(new Vector3f(x + 0.5f, y - 0.5f, z - 0.5f));
            vertices.add(new Vector3f(x - 0.5f, y - 0.5f, z - 0.5f));
            normalsList.add(6);
            normalsList.add(6);
            normalsList.add(6);
            normalsList.add(6);
        }
        position.sub(chunkX * 16, 0, chunkY * 16);

        indices.add(offset);
        indices.add(offset + 1);
        indices.add(offset + 3);

        indices.add(offset + 1);
        indices.add(offset + 2);
        indices.add(offset + 3);


        int tileX = material.getTexture().getFaceTexture(direction).x;
        int tileY = material.getTexture().getFaceTexture(direction).y;

        float tileSize = 1 / 32f;

        textureCoordinates.add(new Vector2f((tileX + 1) * tileSize, tileY * tileSize));
        textureCoordinates.add(new Vector2f(tileX * tileSize, tileY * tileSize));
        textureCoordinates.add(new Vector2f(tileX * tileSize, (tileY + 1) * tileSize));
        textureCoordinates.add(new Vector2f((tileX + 1) * tileSize, (tileY + 1) * tileSize));
    }

    public void updateArrays() {
        verticesArray = new float[vertices.size() * 3];
        indicesArray = new int[vertices.size() * 3];
        uvArray = new float[vertices.size() * 2];
        normals = new float[vertices.size() * 3];

        int i = 0;
        for (Vector3f vert : vertices) {
            verticesArray[i * 3] = vert.x;
            verticesArray[i * 3 + 1] = vert.y;
            verticesArray[i * 3 + 2] = vert.z;
            i++;
        }
        i = 0;
        for (Integer ind : indices) {
            indicesArray[i] = ind;
            i++;
        }
        i = 0;
        for (Vector2f uv : textureCoordinates) {
            uvArray[i] = uv.x;
            uvArray[i + 1] = uv.y;
            i += 2;
        }
        i = 0;
        for (Integer normal : normalsList) {
            normals[i] = normal;
            i++;
        }
    }

    private void generateChunk() {
        FastNoiseLite noise = new FastNoiseLite(seed);
        noise.SetNoiseType(FastNoiseLite.NoiseType.Perlin);
        Block air = new Block(Material.AIR);
        Block stone = new Block(Material.STONE);
        Block grass = new Block(Material.GRASS);
        Block dirt = new Block(Material.DIRT);
        Block bedrock = new Block(Material.BEDROCK);
        for (int x = 0; x < 16; x++) {
            for (int y = 0; y < 128; y++) {
                for (int z = 0; z < 16; z++) {

                    float value = 40 + noiseAmplitude * noise.GetNoise(chunkX * 16f * noiseScale + x * noiseScale, chunkY * 16f * noiseScale + z * noiseScale);

                    if (y == (int) value) {
                        blocks[x][y][z] = grass;
                    } else if (y < value && y > value - 4) {
                        blocks[x][y][z] = dirt;
                    } else if (y == 0) {
                        blocks[x][y][z] = bedrock;
                    } else if (y < value && noise.GetNoise(chunkX * 16f * caveScale + x * caveScale, y * caveScale, chunkY * 16f * caveScale + z * caveScale) > -0.35f) {
                        blocks[x][y][z] = stone;
                    } else {
                        blocks[x][y][z] = air;
                    }

                }
            }
        }
    }
}
