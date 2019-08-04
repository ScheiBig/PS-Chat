package ScheiBig.PS.UDPChat

import printlnErr
import java.lang.Exception
import java.net.DatagramPacket
import java.net.MulticastSocket

class ChatListener(private val messenger: ChatMessenger, private val socket: MulticastSocket): Thread() {

    val buf = ByteArray(4096)

    override fun run() {
        while (true) {
            val packet = DatagramPacket(buf, buf.size)
            try {
                socket.receive(packet)
            } catch (e: Exception) {
                printlnErr("Connection error while communicating with Multicast Group!")
            }
            val message = String(packet.data, 0, packet.length).split(Regex(" "), 2)
            when (message[0]) {
                "NICK" -> {}
                "JOIN" -> {}
                "LEAVE" -> {}
                "MSG" -> {}
                "WHOIS" -> {}
            }
        }
    }
}