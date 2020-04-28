package me.saiintbrisson.nms.api.area.cuboid;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
@EqualsAndHashCode
@AllArgsConstructor
public class ChunkPosition {

    private short x, z;

    public ChunkPosition(int x, int z) {
        this.x = (short) x;
        this.z = (short) z;
    }

    public ChunkPosition(short[] position) {
        x = position[0];
        z = position[1];
    }

}
