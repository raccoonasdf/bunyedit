package fun.raccoon.bunyedit.util;

import fun.raccoon.bunyedit.data.BlockData;
import fun.raccoon.bunyedit.mixin.BlockSignAccessor;
import net.minecraft.core.block.Block;
import net.minecraft.core.block.BlockBed;
import net.minecraft.core.block.BlockButton;
import net.minecraft.core.block.BlockChest;
import net.minecraft.core.block.BlockFenceGate;
import net.minecraft.core.block.BlockLadder;
import net.minecraft.core.block.BlockLever;
import net.minecraft.core.block.BlockPumpkin;
import net.minecraft.core.block.BlockRail;
import net.minecraft.core.block.BlockRedstoneRepeater;
import net.minecraft.core.block.BlockSign;
import net.minecraft.core.block.BlockSlab;
import net.minecraft.core.block.BlockStairs;
import net.minecraft.core.block.BlockTileEntityRotatable;
import net.minecraft.core.block.BlockTorch;
import net.minecraft.core.block.BlockTrapDoor;
import net.minecraft.core.block.piston.BlockPistonBase;
import net.minecraft.core.block.piston.BlockPistonHead;
import net.minecraft.core.util.helper.Axis;

public class Reorient {
    public static BlockData flipped(BlockData blockData, Axis axis) {
        Block block = Block.getBlock(blockData.id);
        int meta = blockData.meta;

        if (block instanceof BlockStairs) {
            if (axis.equals(Axis.X) && (meta&2)==0)
                meta ^= 1;
            if (axis.equals(Axis.Y))
                meta ^= 8;
            if (axis.equals(Axis.Z) && (meta&2)==2)
                meta ^= 1;
        } else if (block instanceof BlockTrapDoor) {
            if (axis.equals(Axis.X) && (meta&2)==2)
                meta ^= 1;
            if (axis.equals(Axis.Y))
                meta ^= 8;
            if (axis.equals(Axis.Z) && (meta&2)==0)
                meta ^= 1;
        } else if (block instanceof BlockSlab) {
            if (axis.equals(Axis.Y) && (meta&1)==0)
                meta ^= 2;
        } else if (
            block instanceof BlockLadder
            || block instanceof BlockPumpkin
            || block instanceof BlockTileEntityRotatable
        ) {
            if (axis.equals(Axis.X) && (meta&2)==0)
                meta ^= 1;
            if (axis.equals(Axis.Z) && (meta&2)==2)
                meta ^= 1;
        } else if (block instanceof BlockFenceGate
            || block instanceof BlockRedstoneRepeater
            || block instanceof BlockBed
            || block instanceof BlockChest
        ) {
            if (axis.equals(Axis.X) && (meta&1)==1)
                meta ^= 2;
            if (axis.equals(Axis.Z) && (meta&1)==0)
                meta ^= 2;
        } else if (
            block instanceof BlockPistonBase
            || block instanceof BlockPistonHead
        ) {
            if (axis.equals(Axis.X) && (meta&6)==4)
                meta ^= 1;
            if (axis.equals(Axis.Y) && (meta&6)==0)
                meta ^= 1;
            if (axis.equals(Axis.Z) && (meta&6)==2)
                meta ^= 1;
        } else if (
            block instanceof BlockTorch
            || block instanceof BlockButton
            || block instanceof BlockLever
        ) {
            switch (meta&7) {
                case 1:
                case 2:
                    if (axis.equals(Axis.X)) meta ^= 3; break;
                case 3:
                case 4:
                    if (axis.equals(Axis.Z)) meta ^= 7; break;
            }
        } else if (block instanceof BlockRail) {
            if (axis.equals(Axis.X) && (meta&14)==2)
                meta ^= 1;
            if (axis.equals(Axis.Z) && (meta&14)==4)
                meta ^= 1;
            if (axis.equals(Axis.X) && (meta&14)>=6)
                meta ^= 1;
            if (axis.equals(Axis.Z) && (meta&14)>=6) {
                meta ^= 14;
                meta ^= 1;
            }
        } else if (block instanceof BlockSign) {
            // the final boss
            if (((BlockSignAccessor)block).getIsFreestanding()) {
                if (axis.equals(Axis.X))
                    meta = meta&~15 | (16-(meta&15));
                if (axis.equals(Axis.Z))
                    meta = meta&~15 | ((24-(meta&15))&15);
            } else {
                if (axis.equals(Axis.X) && (meta&4)==4)
                    meta ^= 1;
                if (axis.equals(Axis.Z) && (meta&4)==0)
                    meta ^= 1;
            }
        } else if (block instanceof BlockPumpkin) {
        } else {
            return blockData;
        }

        return new BlockData(blockData.id, meta, blockData.nbt);
    }
}
