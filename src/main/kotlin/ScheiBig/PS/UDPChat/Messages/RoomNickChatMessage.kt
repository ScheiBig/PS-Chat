package ScheiBig.PS.UDPChat.Messages

internal abstract class RoomNickChatMessage: NickChatMessage() {
    abstract val roomname: String
    override fun toString(): String = "$roomname ${super.toString()}"
}