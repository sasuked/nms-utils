package me.saiintbrisson.nms.api.entities;

import org.bukkit.Location;

public interface NmsEntity {

    int getEntityId();

    Location getLocation();
    void setLocation(Location location);

    void tick();

}
