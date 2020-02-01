package ScheiBig.PS.UDPChat.Messages

/**
 * Object representing message sent when room listing
 * @property nickname Nickname of person in listed chatroom
 * @property roomname Name of chatroom that is being listed
 * @constructor Creates message with specified [roomname] and [nickname]
 */
internal class RoomRoomNickChatMessage(roomname: String,
                                       nickname: String) : RoomNickChatMessage(roomname, nickname) {
    override fun toString(): String {
        return "ROOM ${super.toString()}"
    }
}