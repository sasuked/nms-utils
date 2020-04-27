package me.saiintbrisson.nms.sdk.area;

import com.google.common.collect.Queues;
import me.saiintbrisson.nms.api.area.AreaHistory;
import net.minecraft.server.v1_8_R3.BlockPosition;

import java.util.Deque;
import java.util.Map;

public class NmsHistory implements AreaHistory<BlockPosition> {

    private Deque<Map<BlockPosition, int[]>> history = Queues.newLinkedBlockingDeque();

    @Override
    public void addChange(Map<BlockPosition, int[]> change) {
        history.add(change);
    }

    @Override
    public Map<BlockPosition, int[]> getLastChange() {
        return history.pollLast();
    }

}
