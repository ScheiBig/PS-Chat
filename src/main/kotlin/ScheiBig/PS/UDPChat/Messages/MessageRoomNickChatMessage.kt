package ScheiBig.PS.UDPChat.Messages

class MessageRoomNickChatMessage(val message: String,
                                 override val roomname: String,
                                 override val nickname: String) : RoomNickChatMessage() {
    override fun toString(): String = "MSG ${super.toString()} $message"
}