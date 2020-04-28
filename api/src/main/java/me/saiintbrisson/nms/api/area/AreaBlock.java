package me.saiintbrisson.nms.api.area;

import org.bukkit.Material;

public interface AreaBlock extends BlockPosition {

    Material getType();
    int getTypeId();
    byte getData();

    void setType(int id);
    void setTypeAndData(int id, byte data);

}
