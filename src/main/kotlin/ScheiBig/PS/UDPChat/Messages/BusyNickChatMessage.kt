package ScheiBig.PS.UDPChat.Messages

class BusyNickChatMessage(nickname: String) : SetNickChatMessage(nickname) {
    override fun toString(): String = "${super.toString()} BUSY"
}