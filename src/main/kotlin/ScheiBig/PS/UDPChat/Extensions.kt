@file:JvmName("ExtensionsKt")

package ScheiBig.PS.UDPChat

import java.lang.IllegalArgumentException
import java.net.DatagramPacket
import java.net.InetAddress

/** Prints the given [message] to the standard error stream. */
fun printErr(message: Any?) {
    System.err.print(message)
}

/** Prints the given [message] and the line separator to the standard error stream. */
fun printlnErr(message: Any?) {
    System.err.println(message)
}

class DatagramPacketCreator(val address: InetAddress, val port: Int) {

    constructor(multicastIdentifier: Pair<InetAddress, Int>) :
            this(multicastIdentifier.first, multicastIdentifier.second)

    fun getPacket(message: String): DatagramPacket = DatagramPacket(
        message.toByteArray(),
        message.length,
        address, port)

    fun getPacket(buffer: ByteArray): DatagramPacket = DatagramPacket(
        buffer,
        buffer.size,
        address, port)


    fun getPacket(buffer: ByteArray, size: Int): DatagramPacket = if (size < 0) DatagramPacket(
        buffer,
        size,
        address, port) else throw IllegalArgumentException("Size of buffer cannot be negative!")
}