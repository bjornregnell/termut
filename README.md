# termut

Simple terminal utility for Scala 3 based on jline3.

Example usage with [scala-cli](https://scala-cli.virtuslab.org) - save in some `.scala` file and `scala-cli run .`
```scala
//> using dep "termut:termut:0.1.0,url=https://github.com/bjornregnell/termut/releases/download/v0.1.0/termut_3-0.1.0.jar"
//> using dep "org.jline:jline:3.26.2"
//> using scala 3

import termut.{NonBlockingRawTerminal, Terminal}

@main def ExampleNiceTerminal =
  import Terminal.*
  setCompletions(Seq("quit", "goodbye"), Seq())
  put("You have history and complete on 'quit' and 'goodbye'")
  put("You can use normal linux shortcuts such as Ctrl+A, Ctrl+K")
  val input = get("prompt> ", default = "hello terminal")
  put(s"You wrote: $input")
  val secret = getSecret()
  put(s"Your secret: $secret") 

@main def ExampleNonBlocking = 
  println(s"start typing like speedy gonzales")
  val nbt = NonBlockingRawTerminal()
  while true do
    nbt.awaitChar(timeoutInMillis = 10) // low poll rate: avoid hot cpu
    val i: Int = nbt.lastTyped
    if i >= 0 then println(s"you typed: $i ${i.toChar}")
```

Note: This api is still experimental and the api will change.

## How to build

```
scala-cli package . --library -o termut_3-0.1.0.jar
scala-cli package . --assembly -o termut_3-assembly-0.1.0.jar --preamble=false
```
