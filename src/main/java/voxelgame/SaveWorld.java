package voxelgame;

import org.joml.Vector2i;
import org.joml.Vector3i;
import voxelgame.block.Block;
import voxelgame.chunk.Chunk;
import voxelgame.material.Material;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

/**
 * @author gabed
 * @Date 10/29/2022
 */
public class SaveWorld {
    public int seed;
    public String lastPlayed;
    public String path;
    public String version;
    public String name;
    public int difficulty;

    public SaveWorld(String path) {
        File f = new File(path + "/world.wfdesc");
        try {
            BufferedReader reader = new BufferedReader(new FileReader(f));
            name = reader.readLine();
            version = reader.readLine();
            difficulty = Integer.parseInt(reader.readLine());
            seed = Integer.parseInt(reader.readLine());
            lastPlayed = reader.readLine();

            reader.close();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        this.path = path;
    }

    public Chunk loadChunk(Vector2i position) {
        File f = new File(path + "/chunks/" + position.x + "_" + position.y + ".chk");
        if (!f.exists()) return new Chunk(position);
        Chunk chunk = new Chunk(position);
        try {
            BufferedReader reader = new BufferedReader(new FileReader(f));
            String line = reader.readLine();
            while (line != null) {
                String[] parts = line.split(";");
                Vector3i cp = new Vector3i(Integer.parseInt(parts[0]), Integer.parseInt(parts[1]), Integer.parseInt(parts[2]));
                Block block = new Block(Material.valueOf(parts[3]));
                chunk.setBlock(cp, block);
                line = reader.readLine();
            }
            reader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return chunk;
    }


    public int getSeed() {
        return seed;
    }

    public String getLastPlayed() {
        return lastPlayed;
    }

    public String getPath() {
        return path;
    }

    public String getVersion() {
        return version;
    }

    public String getName() {
        return name;
    }

    public int getDifficulty() {
        return difficulty;
    }
}
