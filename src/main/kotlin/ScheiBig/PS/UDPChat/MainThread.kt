package ScheiBig.PS.UDPChat

import ScheiBig.PS.UDPChat.Messages.SetNickChatMessage
import printErr
import printlnErr
import java.io.IOException
import java.net.DatagramPacket
import java.net.InetAddress
import java.net.MulticastSocket

class MainThread {

    companion object {

        @JvmStatic
        val multicastGroup: Pair<InetAddress, Int> = Pair(InetAddress.getByName("230.0.0.0"), 4446)

        @JvmStatic
        val bufferSize: Int = 4096
        @JvmStatic
        val messageSize: Int = 3500
        @JvmStatic
        val nickSize: Int = 250
        @JvmStatic
        val roomSize: Int = 250

        @JvmStatic
        fun main(args: Array<String>) {
            try {
                var nickname: String
                var reply: String
                val socket = MulticastSocket(multicastGroup.second)
                socket.joinGroup(multicastGroup.first)
                val messengerThread = ChatMessenger(socket)
                val listenerThread = ChatListener(messengerThread, socket)
                val buffer = ByteArray(bufferSize)

                nickLoop@ while (true) {

                    print("Enter your nickname: ")
                    nickname = readLine() ?: continue
                    if (nickname == "") {
                        printlnErr("Nickname cannot be empty!")
                        continue@nickLoop
                    }
                    val message = SetNickChatMessage(nickname)
                    val packet = DatagramPacket(
                        message.toString().toByteArray(), message.length,
                        multicastGroup.first, multicastGroup.second
                    )
                    try {
                        socket.send(packet)
                        socket.soTimeout = 5000
                        val responsePacket = DatagramPacket(buffer, bufferSize)
                        socket.receive(responsePacket)
                        reply = String(responsePacket.data, 0, responsePacket.length)


                    } catch (e: Exception) {
                        printlnErr("Connection error while communicating with Multicast Group!")
                        continue@nickLoop
                    }
                }

                socket.close()

            } catch (e: IOException) {
                printlnErr("Error while communicating with hardware!")
            } catch (e: SecurityException) {
                printlnErr("Error while communicating caused by security restrictions!")
            }
        }
    }
}