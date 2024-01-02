package rip.diamond.maid.api.server;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum AlertType {

    SERVER("maid.alert.server"),
    UPDATE("maid.alert.update"),
    ;

    private final String permission;

}
