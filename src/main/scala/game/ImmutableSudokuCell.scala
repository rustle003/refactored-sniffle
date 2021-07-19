package game

import game.util.Outcome

class ImmutableSudokuCell(v: Int) extends SudokuCell[Int](v) with Immutable{
    override def value: Int = this.v
    override def <<=(va: Int): Outcome = Outcome.Failure
    override def <=<(va: Int): Boolean = false
}