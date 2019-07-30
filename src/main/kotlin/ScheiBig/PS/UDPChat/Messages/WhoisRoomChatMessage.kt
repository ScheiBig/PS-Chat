package ScheiBig.PS.UDPChat.Messages

class WhoisRoomChatMessage(val roomname: String) {
    override fun toString(): String = "WHOIS $roomname"
}