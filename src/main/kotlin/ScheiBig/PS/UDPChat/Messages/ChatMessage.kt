package ScheiBig.PS.UDPChat.Messages

abstract class ChatMessage {

    val length: Int
        get() = toString().length

    abstract override fun toString(): String

    companion object {
        @JvmStatic
        @Throws(java.lang.IllegalArgumentException::class)
        fun parseMessage(message: String): ChatMessage {

            val badNumberParametersEx = IllegalArgumentException("Wrong number of parameters!")
            val unknownParametersException = java.lang.IllegalArgumentException("Unknown message type")

            val splitMessage = message.split(Regex(" "))
            return when (splitMessage.first()) {
                "NICK" -> {
                    if (splitMessage.last() == "BUSY") {
                        if (splitMessage.size != 3) throw badNumberParametersEx
                        BusyNickChatMessage(splitMessage[1])
                    } else {
                        if (splitMessage.size != 2) throw badNumberParametersEx
                        SetNickChatMessage(splitMessage[1])
                    }
                }
                "JOIN" -> {
                    if (splitMessage.size != 3) throw badNumberParametersEx
                    JoinRoomNickChatMessage(splitMessage[1], splitMessage[2])
                }
                "LEAVE" -> {
                    if (splitMessage.size != 3) throw badNumberParametersEx
                    LeaveRoomNickChatMessage(splitMessage[1], splitMessage[2])
                }
                "MSG" -> {
                    if (splitMessage.size <= 3) throw badNumberParametersEx
                    MessageRoomNickChatMessage(
                        splitMessage[1], splitMessage[2],
                        splitMessage.subList(3, splitMessage.size).joinToString(" ")
                    )
                }
                "ROOM" -> {
                    if (splitMessage.size != 3) throw badNumberParametersEx
                    RoomRoomNickChatMessage(splitMessage[1], splitMessage[2])
                }
                "WHOIS" -> {
                    if (splitMessage.size != 2) throw badNumberParametersEx
                    WhoisRoomChatMessage(splitMessage[1])
                }
                else -> throw unknownParametersException
            }
        }
    }
}
