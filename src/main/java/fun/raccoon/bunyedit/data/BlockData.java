package fun.raccoon.bunyedit.data;

import java.util.Arrays;

import com.mojang.nbt.CompoundTag;

import net.minecraft.core.block.Block;
import net.minecraft.core.block.entity.TileEntity;
import net.minecraft.core.world.World;
import net.minecraft.core.world.chunk.ChunkPosition;

/**
 * Worldless representation of a block for storing and placing later.
 */
public class BlockData {
    /**
     * Block ID.
     */
    public int id;

    /**
     * Block metadata.
     */
    public int meta;

    /**
     * NBT data for a tile entity. null for regular blocks.
     */
    public CompoundTag nbt;

    public BlockData(int id, int meta, CompoundTag nbt) {
        this.id = id;
        this.meta = meta;
        this.nbt = nbt;
    }

    private BlockData(World world, int x, int y, int z) {
        this.id = world.getBlockId(x, y, z);
        this.meta = world.getBlockMetadata(x, y, z);

        TileEntity tileEntity = world.getBlockTileEntity(x, y, z);
        CompoundTag compoundTag;
        if (tileEntity == null) {
            compoundTag = null;
        } else {
            compoundTag = new CompoundTag();
            tileEntity.writeToNBT(compoundTag);
        }
        this.nbt = compoundTag;
    }

    public BlockData(World world, ChunkPosition pos) {
        this(world, pos.x, pos.y, pos.z);
    }

    /**
     * From string of the form "<key>:<meta>"
     */
    public static BlockData fromString(String string) {
        String[] parts = string.split(":");
        String key = parts[0];
        int meta = 0;

        if (key.equals("air"))
            return new BlockData(0, 0, null);

        if (parts.length == 2) {
            try {
                meta = Integer.parseInt(parts[1]);
            } catch (NumberFormatException e) {
                return null;
            }
        } else if (parts.length > 2) {
            return null;
        }
        Block block = Arrays.stream(Block.blocksList)
            .filter(b -> b != null && b.getKey().equalsIgnoreCase("tile." + key))
            .findAny()
            .orElse(null);
        
        return new BlockData(block.id, meta, null);
    }

    /**
     * Does the block at this location match this BlockData in ID and metadata?
     */
    public boolean idMetaMatches(World world, ChunkPosition pos) {
        int id = world.getBlockId(pos.x, pos.y, pos.z);
        int meta = world.getBlockMetadata(pos.x, pos.y, pos.z);

        return this.id == id && this.meta == meta;
    }

    /**
     * Places a block matching this data into the world.
     */
    public void place(World world, ChunkPosition pos) {
        world.setBlockAndMetadata(pos.x, pos.y, pos.z, this.id, this.meta);
        if (this.nbt != null)
            world.setBlockTileEntity(
                pos.x, pos.y, pos.z,
                TileEntity.createAndLoadEntity(this.nbt));

        world.notifyBlockChange(pos.x, pos.y, pos.z, this.id);
    }
}
