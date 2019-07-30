import java.net.DatagramPacket
import java.net.InetAddress
import java.net.MulticastSocket
import java.util.concurrent.TimeUnit
import java.util.concurrent.locks.ReentrantLock
import kotlin.concurrent.withLock

    val multicastGroup: Pair<InetAddress, Int> = Pair(InetAddress.getByName("230.0.0.0"), 4446)

    fun main() {

        val socket = MulticastSocket(multicastGroup.second)
        socket.joinGroup(multicastGroup.first)
        val msg = "Hello World!"
        val packet = DatagramPacket(msg.toByteArray(), msg.length, multicastGroup.first, multicastGroup.second)
        socket.send(packet)
        val thread = Thread(Runnable {
            val buf = ByteArray(4096)
            val packet1 = DatagramPacket(buf, buf.size)
            socket.receive(packet1)
            socket.close()
            println(String(packet1.data, 0, packet1.length))
        })
        thread.start()
        val lock = ReentrantLock()
        val condition = lock.newCondition()
        lock.withLock {
            condition.await(5, TimeUnit.SECONDS)
        }
        thread.interrupt()
    }
