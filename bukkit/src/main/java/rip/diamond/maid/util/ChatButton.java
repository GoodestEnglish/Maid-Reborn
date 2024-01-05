package rip.diamond.maid.util;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class ChatButton {

    private final String name;
    private final String hoverValue;
    private final ClickAction clickAction;
    private final String clickValue;

    public enum ClickAction {
        CHANGE_PAGE,
        COPY_TO_CLIPBOARD,
        OPEN_FILE,
        OPEN_URL,
        RUN_COMMAND,
        SUGGEST_COMMAND
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder(name);
        if (hoverValue != null) {
            builder.insert(0, "<hover:show_text:'" + hoverValue + "'>");
            builder.append("</hover>");
        }
        if (clickAction != null && clickValue != null) {
            builder.insert(0, "<click:" + clickAction.name().toLowerCase() + ":" + clickValue + ">");
            builder.append("</click>");
        }
        return builder.toString();
    }

}
