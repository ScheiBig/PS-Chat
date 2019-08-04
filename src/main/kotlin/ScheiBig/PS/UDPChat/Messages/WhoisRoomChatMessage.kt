package ScheiBig.PS.UDPChat.Messages

class WhoisRoomChatMessage(val roomname: String): ChatMessage {
    override fun toString(): String = "WHOIS $roomname"
}