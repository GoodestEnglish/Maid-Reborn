package rip.diamond.maid.chat;

import lombok.Getter;
import lombok.Setter;
import rip.diamond.maid.api.user.chat.ChatRoomType;
import rip.diamond.maid.api.user.chat.IChatRoom;

import java.util.UUID;

@Getter
@Setter
public class ChatRoom implements IChatRoom {

    private ChatRoomType type = ChatRoomType.PUBLIC;
    private UUID messageTo;

}
