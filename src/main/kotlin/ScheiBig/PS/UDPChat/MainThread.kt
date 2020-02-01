package ScheiBig.PS.UDPChat

import ScheiBig.PS.UDPChat.Messages.*
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

        @JvmStatic
        val packetCreator = DatagramPacketCreator(multicastGroup)

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
         * Maximum size of _nickname that can be entered by user
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
         * Timeout of waiting for nickname-already-used message
         */
        @JvmStatic
        val nicknameTimeout = 5000

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

        private lateinit var _nickname: String
        @JvmStatic
        val nickname: String
            get() = _nickname
        @JvmStatic
        fun checkNickname(nickname: String): Boolean = _nickname == nickname

        private lateinit var _roomname: String
        @JvmStatic
        val roomname: String
            get() = _roomname
        @JvmStatic
        fun checkRoomname(roomname: String): Boolean = _roomname == roomname
        @JvmStatic
        fun changeRoomname(roomname: String) { _roomname = roomname }

        private lateinit var messengerThread: ChatMessenger
        private lateinit var listenerThread: ChatListener
        private lateinit var readerThread: ConsoleReader

        private lateinit var socket: MulticastSocket

        /**
         * Main function of program
         * @param args Parameter list send to program
         */
        @JvmStatic
        fun main(args: Array<String>) {
            try {
                socket = MulticastSocket(multicastGroup.second)
                socket.joinGroup(multicastGroup.first)
                messengerThread = ChatMessenger(socket)
                listenerThread = ChatListener(messengerThread, socket)


                _nickname = requestNickname(socket)
                _roomname = requestRoomName(socket, _nickname)
                messengerThread.start()
                listenerThread.start()

                readerThread = ConsoleReader(_roomname, _nickname, messengerThread)

                Runtime.getRuntime().addShutdownHook(Thread((MainThread)::shutdown))

                listenerThread.openComm()

                readerThread.start()
                readerThread.join()

                socket.close()

            } catch (e: IOException) {
                printlnErr(IOExMsg)
            } catch (e: SecurityException) {
                printlnErr(SecExMsg)
            }
        }

        private fun requestRoomName(socket: MulticastSocket, nickname: String): String {
            var roomname: String

            while (true) {

                print("Enter chatrom name: ")
                roomname = readLine() ?: continue
                if (roomname == "") {
                    printlnErr("Chatroom name cannot be empty!")
                    continue
                } else if (roomname.length > roomSize) {
                    printlnErr("Chatroom name length cannot be over $roomSize characters")
                }

                val message = JoinRoomNickChatMessage(roomname, nickname)
                val packet = packetCreator.getPacket(message.toString())
                try {
                    socket.send(packet)
                    retryCount = retryNumber
                    return roomname
                } catch (e: IOException) {
                    printlnErr(IOExMsg)
                    --retryCount
                    continue
                } catch (e: SecurityException) {
                    printlnErr(SecExMsg)
                    --retryCount
                    continue
                }
            }
        }

        private fun requestNickname(socket: MulticastSocket): String {
            val buffer = ByteArray(bufferSize)
            var nickname: String
            var reply: String

            loop@ while (true) {

                print("Enter your nickname: ")
                nickname = readLine() ?: continue
                if (nickname == "") {
                    printlnErr("Nickname cannot be empty!")
                    continue
                } else if (nickname.length > nickSize) {
                    printlnErr("Nickname length cannot be over $nickSize characters")
                    continue
                }
                val message = SetNickChatMessage(nickname)
                val packet = packetCreator.getPacket(message.toString())
                try {
                    socket.send(packet)
                    socket.soTimeout = nicknameTimeout
                    val responsePacket = DatagramPacket(buffer, bufferSize)

                     while (true) {
                        socket.receive(responsePacket)
                        reply = String(responsePacket.data, 0, responsePacket.length)
                        try {
                            if (ChatMessage.parseMessage(reply) !is BusyNickChatMessage) {
                                continue
                            } else {
                                printlnErr("Nickname $nickname is already taken")
                                continue@loop
                            }
                        } catch (e: IllegalArgumentException) {
                            continue
                        }
                    }
                } catch (e: SocketTimeoutException) {
                    socket.soTimeout = 0
                    retryCount = retryNumber
                    return nickname
                } catch (e: IOException) {
                    printlnErr(IOExMsg)
                    --retryCount
                    continue
                } catch (e: SecurityException) {
                    printlnErr(SecExMsg)
                    --retryCount
                    continue
                }
            }
        }

        @JvmStatic
        fun shutdown() {
            listenerThread.closeComm()
            try {
                socket.send(packetCreator.getPacket(LeaveRoomNickChatMessage(roomname, nickname).toString()))
            } catch (e: Exception) {}
            messengerThread.shutdown()
            listenerThread.shutdown()
            readerThread.shutdown()
        }
    }
}