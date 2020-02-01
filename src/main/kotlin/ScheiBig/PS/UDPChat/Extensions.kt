@file:JvmName("Extensions")

package ScheiBig.PS.UDPChat

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

/** Prints the given debug-[message] to the standard output stream. */
@Deprecated("Remember to cleanup debug messages from final code")
fun debugPrint(message: Any?) {
    print("$##   $message   ###$")
    if (!RemainingDebugPrinterException.CleanUpDebug)
        throw RemainingDebugPrinterException()
}

/** Prints the given debug-[message] and the line separator to the standard output stream. */
@Deprecated("Remember to cleanup debug messages from final code")
fun debugPrintln(message: Any?) {
    println("$##   $message   ###$")
    if (!RemainingDebugPrinterException.CleanUpDebug)
        throw RemainingDebugPrinterException()
}

class RemainingDebugPrinterException(): RuntimeException() {

    companion object {
        val CleanUpDebug = true
    }
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