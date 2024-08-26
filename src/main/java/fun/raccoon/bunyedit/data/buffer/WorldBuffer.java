package fun.raccoon.bunyedit.data.buffer;
public class WorldBuffer {
    public BlockBuffer blocks;
    public EntityBuffer entities;

    public WorldBuffer(BlockBuffer blocks, EntityBuffer entities) {
        this.blocks = blocks;
        this.entities = entities;
    }

    public WorldBuffer() {
        this.blocks = new BlockBuffer();
        this.entities = new EntityBuffer();
    }
}
