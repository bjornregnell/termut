# termut

Simple terminal utility for Scala 3 based on jline3.

Example usage:
```scala
import termut.*

@main def ExampleNonBlocking = 
  println(s"start typing like speedy gonzales")
  val nbt = NonBlockingRawTerminal()
  while true do
    nbt.awaitChar(10) // poll at 10 ms rate to not overheat your  cpu
    val i = nbt.lastTyped
    if i != -1 then println(s"you typed: $i ${i.toChar}")
```

Note: This api is still experimental and the api will change.
