package game

import game.util.AssignmentLimitation

abstract class SudokuCell[T](v: T) extends AnyRef with AssignmentLimitation[T] {
    def value: T = ???
}

object SudokuCell {
    var EMPTY = 0

    def apply(value: Int): SudokuCell[Int] = new EditableSudokuCell(value)

    def apply(value: Int, b: Boolean): SudokuCell[Int] = new ImmutableSudokuCell(value)

    def main(args: Array[String]): Unit = {
        println("Hello from SudokuCell")

        val test = SudokuCell(5, true)
        
        println(test.value)

        test <<= 3

        println(test.value)
    }
}