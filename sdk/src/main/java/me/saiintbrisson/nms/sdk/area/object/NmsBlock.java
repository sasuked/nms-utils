package me.saiintbrisson.nms.sdk.area.object;

import me.saiintbrisson.nms.api.area.AreaBlock;
import me.saiintbrisson.nms.api.area.SimpleBlockPosition;
import org.bukkit.Material;

public class NmsBlock extends SimpleBlockPosition implements AreaBlock {

    private NmsWorld world;

    public NmsBlock(NmsWorld world, int x, int y, int z) {
        super(x, y, z);
        this.world = world;
    }

    @Override
    public Material getType() {
        return Material.getMaterial(
            world.getType(getX(), getY(), getZ())
        );
    }

    @Override
    public int getTypeId() {
        return world.getType(getX(), getY(), getZ());
    }

    @Override
    public byte getData() {
        return world.getData(getX(), getY(), getZ());
    }

    @Override
    public void setType(int id) {
        world.setBlock(this, id, (byte) 0);
    }

    @Override
    public void setTypeAndData(int id, byte data) {
        world.setBlock(this, id, data);
    }
}
