package me.saiintbrisson.nms.api.area;

import java.util.Map;

public interface AreaHistory<B> {

    void addChange(Map<B, int[]> change);

    Map<B, int[]> getLastChange();

}
