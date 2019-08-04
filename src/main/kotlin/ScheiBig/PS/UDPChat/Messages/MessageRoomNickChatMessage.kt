package ScheiBig.PS.UDPChat.Messages

class MessageRoomNickChatMessage(override val roomname: String,
                                 override val nickname: String,
                                 val message: String) : RoomNickChatMessage() {
    override fun toString(): String = "MSG ${super.toString()} $message"
}