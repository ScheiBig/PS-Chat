package ScheiBig.PS.UDPChat

interface Shutdownable {

    val isRunning: Boolean
    fun shutdown()
}