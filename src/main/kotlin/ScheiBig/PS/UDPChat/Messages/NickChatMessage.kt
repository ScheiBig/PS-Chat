package ScheiBig.PS.UDPChat.Messages

abstract class NickChatMessage: ChatMessage {
    abstract val nickname: String
    override fun toString(): String = nickname
}
