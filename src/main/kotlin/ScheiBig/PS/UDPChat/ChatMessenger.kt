package ScheiBig.PS.UDPChat

import ScheiBig.PS.UDPChat.Messages.ChatMessage
import java.io.IOException
import java.net.MulticastSocket
import java.util.*
import java.util.concurrent.locks.ReentrantLock
import kotlin.concurrent.withLock

class ChatMessenger(private val socket: MulticastSocket): Thread(), Shutdownable {


    private val lock = ReentrantLock()
    private val condition = lock.newCondition()

    private val msgQueue = LinkedList<ChatMessage>()
    fun queue(cmsg: ChatMessage) {
        msgQueue.add(cmsg)
        lock.withLock { condition.signal() } // like synchronized(lock) lock.notify()
    }
    fun priorityQueue(cmsg: ChatMessage) {
        msgQueue.addFirst(cmsg)
        lock.withLock { condition.signal() }
    }
    fun dequeue() = msgQueue.pollFirst()

    override fun run() {
        lock.withLock {           // like synchronized(lock)
            while (isRunning) {

                try {
                    if (msgQueue.isEmpty()) condition.await() // like lock.wait()
                } catch (e: InterruptedException) {
                    break
                }
                val message = dequeue()!!
                val packet = MainThread.packetCreator.getPacket(message.toString())
                try {
                    socket.send(packet)
                    println("Sent: $message")
                } catch (e: IOException) {
                    printlnErr(MainThread.IOExMsg)
                    break
                } catch (e: SecurityException) {
                    printlnErr(MainThread.SecExMsg)
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
