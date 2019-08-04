package ScheiBig.PS.UDPChat

import ScheiBig.PS.UDPChat.Messages.ChatMessage
import java.net.MulticastSocket
import java.util.*

class ChatMessenger(private val socket: MulticastSocket): Thread() {

    private val queue = LinkedList<ChatMessage>()

    override fun run() {

    }
}
