package websocket.chat

import io.micronaut.runtime.Micronaut.*
fun main(args: Array<String>) {
	build()
	    .args(*args)
		.packages("websocket.chat")
		.start()
}

