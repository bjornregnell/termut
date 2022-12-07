# termut

Simple terminal utility for Scala 3 based on jline3.

Example usage:
```scala
import termut.*

@main def ExampleNonBlocking = 
  println(s"start typing like speedy gonzales")
  val nbt = NonBlockingRawTerminal()
  while true do
    nbt.awaitChar(timeoutInMillis = 10) // low poll rate to not overheat cpu
    val i: Int = nbt.lastTyped
    if i >= 0 then println(s"you typed: $i ${i.toChar}")
```

Note: This api is still experimental and the api will change.
