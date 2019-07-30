package ScheiBig.PS.UDPChat.Messages

class JoinRoomNickChatMessage(override val roomname: String,
                              override val nickname: String) : RoomNickChatMessage() {
    override fun toString(): String = "JOIN ${super.toString()}"
}