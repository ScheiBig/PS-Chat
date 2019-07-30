package ScheiBig.PS.UDPChat.Messages

abstract class RoomNickChatMessage: NickChatMessage() {
    abstract val roomname: String
    override fun toString(): String = "$roomname ${super.toString()}"
}