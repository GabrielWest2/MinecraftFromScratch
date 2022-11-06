package voxelgame.block;

import java.util.HashMap;

/**
 * @author gabed
 * @Date 10/12/2022
 */
public class BlockData {
    private final HashMap<String, String> valueSets = new HashMap<>();


    public String getValue(String key) {
        return valueSets.get(key);
    }

    public int getIntValue(String key) {
        return Integer.parseInt(valueSets.get(key));
    }

    public float getFloatValue(String key) {
        return Float.parseFloat(valueSets.get(key));
    }

    public long getLongValue(String key) {
        return Long.parseLong(valueSets.get(key));
    }


    public void setValue(String key, String value) {
        valueSets.put(key, value);
    }

    public void setValue(String key, int value) {
        valueSets.put(key, String.valueOf(value));
    }

    public void setValue(String key, float value) {
        valueSets.put(key, String.valueOf(value));
    }

    public void setValue(String key, long value) {
        valueSets.put(key, String.valueOf(value));
    }


    public HashMap<String, String> getBlockDataSets() {
        return valueSets;
    }
}
