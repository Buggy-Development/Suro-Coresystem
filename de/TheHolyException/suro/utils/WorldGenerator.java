package de.TheHolyException.suro.utils;

import java.util.Random;
import java.util.logging.Logger;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.data.BlockData;
import org.bukkit.generator.ChunkGenerator;

public class WorldGenerator extends ChunkGenerator {
    private Logger log = Logger.getLogger("Minecraft");
    private BlockData[] layerBlock;
    private int[] layerHeight;

    public WorldGenerator() {
        this("");
    }

    public WorldGenerator(String id) {
        if (id == null || id.equals("")) {
            id = "64|stone";
        }
        if (id.equals(".")) {
            this.layerBlock = new BlockData[0];
            this.layerHeight = new int[0];
            return;
        }
        try {
            id = id.charAt(0) != '.' ? "1|minecraft:bedrock|" + id : id.substring(1);
            String[] tokens = id.split("[|]");
            if (tokens.length % 2 != 0) {
                throw new Exception();
            }
            int layerCount = tokens.length / 2;
            this.layerBlock = new BlockData[layerCount];
            this.layerHeight = new int[layerCount];
            for (int i = 0; i < layerCount; ++i) {
                BlockData blockData;
                int j = i * 2;
                int height = Integer.parseInt(tokens[j]);
                if (height <= 0) {
                    this.log.warning("[CleanroomGenerator] Invalid height '" + tokens[j] + "'. Using 64 instead.");
                    height = 64;
                }
                try {
                    blockData = Bukkit.createBlockData((String)tokens[j + 1]);
                }
                catch (Exception e) {
                    this.log.warning("[CleanroomGenerator] Failed to lookup block '" + tokens[j + 1] + "'. Using stone instead. Exception: " + e.toString());
                    blockData = Material.STONE.createBlockData();
                }
                this.layerBlock[i] = blockData;
                this.layerHeight[i] = height;
            }
        }
        catch (Exception e) {
            this.log.severe("[CleanroomGenerator] Error parsing CleanroomGenerator ID '" + id + "'. using defaults '64,1': " + e.toString());
            e.printStackTrace();
            this.layerBlock = new BlockData[2];
            this.layerBlock[0] = Material.BEDROCK.createBlockData();
            this.layerBlock[1] = Material.STONE.createBlockData();
            this.layerHeight = new int[2];
            this.layerHeight[0] = 1;
            this.layerHeight[1] = 64;
        }
    }

    public ChunkGenerator.ChunkData generateChunkData(World world, Random random, int chunkX, int chunkZ, ChunkGenerator.BiomeGrid biome) {
        ChunkGenerator.ChunkData chunk = this.createChunkData(world);
        int y = 0;
        for (int i = 0; i < this.layerBlock.length; ++i) {
            chunk.setRegion(0, y, 0, 16, y + this.layerHeight[i], 16, this.layerBlock[i]);
            y += this.layerHeight[i];
        }
        return chunk;
    }

    public Location getFixedSpawnLocation(World world, Random random) {
        int highestBlock;
        if (!world.isChunkLoaded(0, 0)) {
            world.loadChunk(0, 0);
        }
        if ((highestBlock = world.getHighestBlockYAt(0, 0)) <= 0 && world.getBlockAt(0, 0, 0).getType() == Material.AIR) {
            return new Location(world, 0.0, 64.0, 0.0);
        }
        return new Location(world, 0.0, (double)highestBlock, 0.0);
    }
}
