package game.util

trait AssignmentLimitation[-T] {
    def <<=(x: T): Outcome = { ??? }
    def <=<(x: T): Boolean = ???
}