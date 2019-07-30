package ScheiBig.PS.UDPChat.Messages

open class SetNickChatMessage(override val nickname: String) : NickChatMessage() {
    override fun toString(): String = "NICK ${super.toString()}"
}