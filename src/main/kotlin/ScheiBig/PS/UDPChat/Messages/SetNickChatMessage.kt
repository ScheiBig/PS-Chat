package ScheiBig.PS.UDPChat.Messages

/**
 * Object representing message sent when [nickname] is registered
 * @property nickname Nickname that is being registered
 * @constructor Creates message with specified nickname
 */
internal open class SetNickChatMessage(override val nickname: String) : NickChatMessage() {
    /**
     * Returns a string representation of the message
     * @return "NICK [nickname]"
     */
    override fun toString(): String = "NICK ${super.toString()}"
}