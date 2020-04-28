package me.saiintbrisson.nms.deftool.action;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import me.saiintbrisson.nms.api.area.BlockPosition;

import java.util.UUID;

@Getter
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@RequiredArgsConstructor
public class Action {

    @EqualsAndHashCode.Include
    private final UUID id;

    @Setter
    private BlockPosition lower, upper;

    public boolean isComplete() {
        return lower != null && upper != null;
    }

}
