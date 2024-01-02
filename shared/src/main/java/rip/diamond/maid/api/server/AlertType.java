package rip.diamond.maid.api.server;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum AlertType {

    SERVER("maid.alert.server"),
    ;

    private final String permission;

}
