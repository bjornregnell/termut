//> using scala "3.3.3"
//> using dep "org.jline:jline:3.26.2"
package termut

/** A terminal with history that can add completions */
object Terminal:
    //https://github.com/jline/jline3/wiki
    //https://search.maven.org/artifact/org.jline/jline
  import org.jline.terminal.TerminalBuilder
  import org.jline.reader.LineReaderBuilder
  import org.jline.reader.impl.completer.{ArgumentCompleter, StringsCompleter}
  import org.jline.reader.impl.LineReaderImpl
  final val CtrlD = "\u0004"  // End Of Transmission

  val terminal = TerminalBuilder.terminal // builder.system(true).build
  val reader = LineReaderBuilder.builder
    .terminal(terminal)
    .build
    .asInstanceOf[LineReaderImpl] //cast hack to expose set/getCompleter

  def get(prompt: String = "", default: String = ""): String =
    util.Try(reader.readLine(prompt, null: Character, default)).getOrElse(CtrlD)

  def getSecret(prompt: String = "Enter secret: ", mask: Char = '*'): String = 
    util.Try(reader.readLine(prompt, mask)).getOrElse(CtrlD)

  def isOk(msg: String = ""): Boolean = get(s"$msg (Y/n): ") == "Y"

  def put(s: String): Unit = 
    terminal.writer.println(s)
    terminal.writer.flush

  def removeCompletions(): Unit = reader.setCompleter(null)

  def setCompletions(first: Seq[String], second: Seq[String]): Boolean =
    removeCompletions()
    val sc1 = new StringsCompleter(first: _*)
    val sc2 = new StringsCompleter(second: _*)
    val ac = new ArgumentCompleter(sc1, sc2)
    reader.setCompleter(ac)
    true  // to be compatible with old readline which used to return if ok

/** A raw terminal that can be polled with timeout for next character. */
class NonBlockingRawTerminal(val bufferCapacity: Int = 1000):
  private val buffer =
    java.util.concurrent.LinkedBlockingQueue[Integer](bufferCapacity)

  @volatile private var _lastTyped: Integer = null

  def lastTyped: Int = if _lastTyped == null then -1 else _lastTyped.intValue()

  def awaitChar(timeoutInMillis: Long = 1): Unit =
    _lastTyped = buffer.poll(timeoutInMillis, java.util.concurrent.TimeUnit.MILLISECONDS)

  val rawTerminal = 
    val t = org.jline.terminal.TerminalBuilder.terminal
    t.enterRawMode()
    t

  private val readerThread = 
    val t = new Thread:
      override def run() = while true do
        val i = rawTerminal.reader().read()
        buffer.offer(i)

    t.start()
    t
