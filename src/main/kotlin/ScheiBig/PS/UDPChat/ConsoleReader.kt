package ScheiBig.PS.UDPChat

import ScheiBig.PS.UDPChat.Messages.ChatMessage
import ScheiBig.PS.UDPChat.Messages.LeaveRoomNickChatMessage
import ScheiBig.PS.UDPChat.Messages.MessageRoomNickChatMessage
import ScheiBig.PS.UDPChat.Messages.WhoisRoomChatMessage
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async
import kotlinx.coroutines.runBlocking

class ConsoleReader(var roomname: String, val nickname: String, val messenger: ChatMessenger): Thread(), Shutdownable {

    private val HELP: String = """
        say %message - send %message to chat
        roomleave - leave current chatroom (will prompt for new chatroom name)
        roomlist - counts and lists users being in current chatroom
        help - prints command list
        exit - closes application
    """.trimIndent()

    private lateinit var msgAsync: Deferred<String?>

    override fun run() {
        loop@ while (true) {
            printPrompt()
            var message: String? = null
            msgAsync = GlobalScope.async { readLine() }
            runBlocking { message = msgAsync.await() }
            if (message == null) continue@loop
            val parsedMessage: ChatMessage
            val splitMessage = message!!.split(Regex(" "))
            parsedMessage = when (splitMessage.first()) {
                "say" -> {
                    MessageRoomNickChatMessage(roomname, nickname,
                        splitMessage.subList(1, splitMessage.size).joinToString(" "))
                }
                "roomleave" -> {
                    LeaveRoomNickChatMessage(roomname, nickname)
                }
                "roomlist" -> {
                    WhoisRoomChatMessage(roomname)
                }
                "help" -> {
                    println(HELP)
                    continue@loop
                }
                "exit" -> {
                    println("Goodbye!")
                    MainThread.shutdown()
                    break@loop
                }
                else -> {
                    printlnErr("Unknown command! Consider typing 'help'")
                    continue@loop
                }
            }
            messenger.queue(parsedMessage)
        }
    }

    companion object {
        @JvmStatic
        fun printPrompt() {
            // horribly buggy
            // print("${MainThread.roomname} \u2906 ${MainThread.nickname}: ")
        }

        @JvmStatic
        fun printPrompt(roomname: String, nickname: String) {
             println("$roomname \u2906 $nickname")
        }
    }

    override var isRunning: Boolean = true

    override fun shutdown() {
        isRunning = false
        msgAsync.cancel()
        this.interrupt()
    }
}