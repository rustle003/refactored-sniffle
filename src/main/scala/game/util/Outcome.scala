package game.util

case class Outcome(val state: Boolean) extends AnyVal

object Outcome {
    val Successful = new Outcome(true)
    val Failure = new Outcome(false)
}