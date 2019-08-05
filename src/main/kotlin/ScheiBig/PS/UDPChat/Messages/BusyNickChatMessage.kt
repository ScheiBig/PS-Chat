package ScheiBig.PS.UDPChat.Messages

/**
 * Object representing reply message sent when [nickname] is already taken
 * @property nickname Nickname that is already taken
 * @constructor Creates message with specified nickname
 */
internal class BusyNickChatMessage(nickname: String) : SetNickChatMessage(nickname) {
    /**
     * Returns a string representation of the message
     * @return "NICK [nickname] BUSY"
     */
    override fun toString(): String = "${super.toString()} BUSY"
}