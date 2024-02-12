package rip.diamond.maid.api.user.chat;

import java.util.UUID;

public interface IChatRoom {

    /**
     * Get the current chat room type.
     * Chat room type indicates who should receive the message.
     *
     * @return The chat room type
     */
    ChatRoomType getType();

    /**
     * Set the chat room type.
     *
     * @param type The new chat room type
     */
    void setType(ChatRoomType type);

    /**
     * Get who should receive the message when reply to someone.
     *
     * @return The user UUID
     */
    UUID getMessageTo();

    /**
     * Set the receiver's UUID.
     *
     * @param uuid The user UUID
     */
    void setMessageTo(UUID uuid);

}
