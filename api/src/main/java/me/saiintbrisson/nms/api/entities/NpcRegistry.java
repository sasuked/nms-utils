package me.saiintbrisson.nms.api.entities;

import com.google.common.collect.HashBasedTable;
import com.google.common.collect.Table;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import me.saiintbrisson.nms.api.entities.npc.Npc;

import java.util.Collection;
import java.util.Iterator;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class NpcRegistry {

    private static final NpcRegistry INSTANCE = new NpcRegistry();

    public static NpcRegistry getInstance() {
        return INSTANCE;
    }

    private Table<Integer, Integer, Npc> npcTable = HashBasedTable.create();

    private final ScheduledExecutorService service = Executors.newSingleThreadScheduledExecutor();
    private ScheduledFuture<?> scheduledFuture;

    @Getter
    private long lastTickDuration;

    public void startTick() {
        if(scheduledFuture != null && !scheduledFuture.cancel(true)) {
            return;
        }

        scheduledFuture = null;

        scheduledFuture = service.scheduleWithFixedDelay(() -> {
            long tickStart = System.currentTimeMillis();
            for(Npc npc : getAll()) {
                npc.tick();
            }
            lastTickDuration = System.currentTimeMillis() - tickStart;
        }, 1000, 50, TimeUnit.MILLISECONDS);
    }

    public int nextId() {
        int current = 0;

        for(Integer integer : npcTable.rowKeySet()) {
            if(integer != null && integer > current) current = integer + 1;
        }

        return current;
    }

    public void register(Npc npc) {
        npcTable.put(npc.getNpcId(), npc.getEntityId(), npc);
    }

    public Collection<Npc> getAll() {
        return npcTable.values();
    }

    public Npc getByNpcId(Integer id) {
        Iterator<Npc> iterator = npcTable.row(id).values().iterator();
        return iterator.hasNext() ? iterator.next() : null;
    }

    public Npc getByEntityId(Integer id) {
        Iterator<Npc> iterator = npcTable.column(id).values().iterator();
        return iterator.hasNext() ? iterator.next() : null;
    }

    public void unregister(Npc npc) {
        npcTable.remove(npc.getNpcId(), npc.getEntityId());
    }

}
