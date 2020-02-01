package ScheiBig.PS.UDPChat

import ScheiBig.PS.UDPChat.Messages.*
import java.io.IOException
import java.net.DatagramPacket
import java.net.MulticastSocket
import java.net.SocketException

class ChatListener(private val messenger: ChatMessenger, private val socket: MulticastSocket): Thread(), Shutdownable {

    private val buf = ByteArray(MainThread.bufferSize)
    private var communicationOpened = false
    fun closeComm() { communicationOpened = false }
    fun openComm() { communicationOpened = true }

    override fun run() {
        loop@ while (isRunning) {
            val packet = DatagramPacket(buf, buf.size)
            try {
                socket.receive(packet)
            } catch (e: SocketException) {
                break
            } catch (e: IOException) {
                printlnErr(MainThread.IOExMsg)
            } catch (e: SecurityException) {
                printlnErr(MainThread.SecExMsg)
            }
            val message = String(packet.data, 0, packet.length)
            val parsedMessage: ChatMessage
            try {
                parsedMessage = ChatMessage.parseMessage(message)
            } catch (e: IllegalArgumentException) {
                continue
            }
            when (parsedMessage) {
                is BusyNickChatMessage -> continue@loop
                is JoinRoomNickChatMessage -> {
                    if (MainThread.checkRoomname(parsedMessage.roomname)) {
                        if (communicationOpened) {
                            println("${parsedMessage.nickname} joined your chatroom")
                            ConsoleReader.printPrompt()
                        }
                    }
                }
                is LeaveRoomNickChatMessage -> {
                    if (MainThread.checkRoomname(parsedMessage.roomname)) {
                        if (communicationOpened) {
                            println("${parsedMessage.nickname} left your chatroom")
                            ConsoleReader.printPrompt()
                        }
                    }
                }
                is MessageRoomNickChatMessage -> {
                    if (MainThread.checkRoomname(parsedMessage.roomname))
                        println("${parsedMessage.nickname}: ${parsedMessage.message}")
                }
                is RoomRoomNickChatMessage -> {
                    if (MainThread.checkRoomname(parsedMessage.roomname))
                        ConsoleReader.printPrompt(parsedMessage.roomname, parsedMessage.nickname)
                }
                is SetNickChatMessage -> {
                    if (MainThread.checkNickname(parsedMessage.nickname))
                        messenger.priorityQueue(BusyNickChatMessage(parsedMessage.nickname))
                }
                is WhoisRoomChatMessage -> {
                    if (MainThread.checkRoomname(parsedMessage.roomname))
                        messenger.queue(RoomRoomNickChatMessage(MainThread.roomname, MainThread.nickname))
                }

            }
        }
    }

    override var isRunning: Boolean = true

    override fun shutdown() {
        isRunning = false
        this.interrupt()
    }
}