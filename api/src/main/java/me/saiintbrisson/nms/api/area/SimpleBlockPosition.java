package me.saiintbrisson.nms.api.area;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;

@Getter
@EqualsAndHashCode
@AllArgsConstructor
public class SimpleBlockPosition implements BlockPosition {

    private int x, y, z;

}
