package ScheiBig.PS.UDPChat.Messages

internal abstract class NickChatMessage(val nickname: String): ChatMessage() {

    override fun toString(): String = nickname
}
