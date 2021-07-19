package game

import game.util.{Outcome,Editable,AssignmentLimitation}

class EditableSudokuCell(private var v: Int) extends SudokuCell[Int](v) with Editable with AssignmentLimitation[Int] {

    override def value: Int = this.v

    def $eq(va: Int): Outcome = this <<= va

    override def <<=(va: Int): Outcome = {
        if (this <=< va) {
            v = va
            Outcome.Successful
        }
        else
            Outcome.Failure
    }

    override def <=<(va: Int): Boolean = 0 < va && va < 10
}