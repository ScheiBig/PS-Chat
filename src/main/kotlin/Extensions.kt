@file:JvmName("ExtensionsKt")

/** Prints the given [message] to the standard error stream. */
fun printErr(message: Any?) {
    System.err.print(message)
}

/** Prints the given [message] and the line separator to the standard error stream. */
fun printlnErr(message: Any?) {
    System.err.println(message)
}