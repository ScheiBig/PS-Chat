package ScheiBig.PS.UDPChat

import ScheiBig.PS.UDPChat.Messages.BusyNickChatMessage
import ScheiBig.PS.UDPChat.Messages.ChatMessage
import ScheiBig.PS.UDPChat.Messages.JoinRoomNickChatMessage
import ScheiBig.PS.UDPChat.Messages.SetNickChatMessage
import printlnErr
import java.io.IOException
import java.net.DatagramPacket
import java.net.InetAddress
import java.net.MulticastSocket
import java.net.SocketTimeoutException

class MainThread {

    companion object {

        /**
         * [Pair] representing *ip address* and *port number* of multicast group
         */
        @JvmStatic
        val multicastGroup: Pair<InetAddress, Int> = Pair(InetAddress.getByName("230.0.0.0"), 4446)

        /**
         * Size of *buffer* used to receive *messages* from *UDP socket*
         */
        @JvmStatic
        val bufferSize: Int = 4096
        /**
         * Maximum size of message that can be send by user
         */
        @JvmStatic
        val messageSize: Int = 3500
        /**
         * Maximum size of nickname that can be entered by user
         */
        @JvmStatic
        val nickSize: Int = 250
        /**
         * Maximum size of chatroom name that can be entered by user
         */
        @JvmStatic
        val roomSize: Int = 250

        /**
         * Maximum number of communication retry
         */
        @JvmStatic
        val retryNumber: Int = 3
        /**
         * Current number of communication retry
         */
        @JvmStatic
        var retryCount: Int = retryNumber

        /**
         * Error message of [IOException] thrown by socket
         */
        @JvmStatic
        val IOExMsg = "Hardware error while communicating with Multicast Group! Trying $retryCount more times"
        /**
         * Error message of [SecurityException] thrown by socket
         */
        @JvmStatic
        val SecExMsg = "Security error while communicating with Multicast Group! Trying $retryCount more times"

        /**
         * Main function of program
         * @param args Parameter list send to program
         */
        @JvmStatic
        fun main(args: Array<String>) {
            try {
                var nickname: String
                var roomname: String
                var reply: String
                val socket = MulticastSocket(multicastGroup.second)
                socket.joinGroup(multicastGroup.first)
                val messengerThread = ChatMessenger(socket)
                val listenerThread = ChatListener(messengerThread, socket)
                val buffer = ByteArray(bufferSize)

                nickLoop@while (true) {

                    print("Enter your nickname: ")
                    nickname = readLine() ?: continue@nickLoop
                    if (nickname == "") {
                        printlnErr("Nickname cannot be empty!")
                        continue@nickLoop
                    } else if (nickname.length > nickSize) {
                        printlnErr("Nickname length cannot be over $nickSize characters")
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

                        nickReplyLoop@while (true) {
                            socket.receive(responsePacket)
                            reply = String(responsePacket.data, 0, responsePacket.length)
                            try {
                                if (ChatMessage.parseMessage(reply) is BusyNickChatMessage)
                                    throw SocketTimeoutException()
                            } catch (e: IllegalArgumentException) { continue@nickReplyLoop }
                        }
                    } catch (e: IOException) {
                        printlnErr(IOExMsg)
                        --retryCount
                        continue@nickLoop
                    } catch (e: SecurityException) {
                        printlnErr(SecExMsg)
                        --retryCount
                        continue@nickLoop
                    } catch (e: SocketTimeoutException) {
                        socket.soTimeout = 0
                        break@nickLoop
                    }
                }
                retryCount = retryNumber

                chatroomLoop@while (true) {

                    print("Enter chatrom name: ")
                    roomname = readLine() ?: continue@chatroomLoop
                    if (roomname == "") {
                        printlnErr("Chatroom name cannot be empty!")
                        continue@chatroomLoop
                    } else if (roomname.length > roomSize) {
                        printlnErr("Chatroom name length cannot be over $roomSize characters")
                    }

                    val message = JoinRoomNickChatMessage(roomname, nickname)
                    val packet = DatagramPacket(
                        message.toString().toByteArray(), message.length,
                        multicastGroup.first, multicastGroup.second
                    )
                    try {
                        socket.send(packet)
                        break@chatroomLoop
                    } catch (e: IOException) {
                        printlnErr(IOExMsg)
                        --retryCount
                        continue@chatroomLoop
                    } catch (e: SecurityException) {
                        printlnErr(SecExMsg)
                        --retryCount
                        continue@chatroomLoop
                    }
                }
                retryCount = retryNumber

                socket.close()

            } catch (e: IOException) {
                printlnErr(IOExMsg)
            } catch (e: SecurityException) {
                printlnErr(SecExMsg)
            }
        }
    }
}