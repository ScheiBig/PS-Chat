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
            when (splitMessage.first()) {
                "NICK" -> {
                    return if (splitMessage.last() == "BUSY") {
                        if (splitMessage.size != 3) throw badNumberParametersEx
                        BusyNickChatMessage(splitMessage[1])
                    } else {
                        if (splitMessage.size != 2) throw badNumberParametersEx
                        SetNickChatMessage(splitMessage[1])
                    }
                }
                "JOIN" -> {
                    if (splitMessage.size != 3) throw badNumberParametersEx
                    return JoinRoomNickChatMessage(splitMessage[1], splitMessage[2])
                }
                "LEAVE" -> {
                    if (splitMessage.size != 3) throw badNumberParametersEx
                    return JoinRoomNickChatMessage(splitMessage[1], splitMessage[2])
                }
                "MSG" -> {
                    if (splitMessage.size <= 3) throw badNumberParametersEx
                    return MessageRoomNickChatMessage(
                        splitMessage[1], splitMessage[2],
                        splitMessage.subList(3, splitMessage.lastIndex).joinToString(" ")
                    )
                }
                "WHOIS" -> {
                    if (splitMessage.size != 2) throw badNumberParametersEx
                    return WhoisRoomChatMessage(splitMessage[1])
                }
                else -> throw unknownParametersException
            }
        }
    }
}
