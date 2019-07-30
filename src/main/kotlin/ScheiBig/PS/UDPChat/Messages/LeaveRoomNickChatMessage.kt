package ScheiBig.PS.UDPChat.Messages

class LeaveRoomNickChatMessage(override val roomname: String,
                               override val nickname: String) : RoomNickChatMessage() {
    override fun toString(): String = "LEAVE ${super.toString()}"
}