package ScheiBig.PS.UDPChat.Messages

internal abstract class RoomNickChatMessage(val roomname: String, nickname: String): NickChatMessage(nickname) {
    override fun toString(): String = "$roomname ${super.toString()}"
}