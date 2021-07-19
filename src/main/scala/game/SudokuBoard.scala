package game

import game.util.{Pos, Outcome, Editable}
import math.random
import scala.collection.script.Index

class SudokuBoard {
    private val board: Array[Array[SudokuCell[Int]]] = Array.ofDim[SudokuCell[Int]](SudokuBoard.boardH,SudokuBoard.boardW)

    private val posIsNotEditable = SudokuBoard.BoardChecker.boardPositionIsImmutable(this)
    private val posIsEditable    = SudokuBoard.BoardChecker.boardPositionIsEditable(this)
    private val isValid          = SudokuBoard.BoardChecker.assertLacks(this)

    def apply(p: Pos): Int = (this getCell p).value

    def apply(x: Int)(y: Int): Int = board(x)(y).value

    def ->[T](a: T): Tuple2[SudokuBoard,T] = (this,a)

    private def getCell(p: Pos): SudokuCell[Int] = p match {case Pos(x,y) => board(x)(y)}

    private def $eq(pv: Tuple2[Pos,SudokuCell[Int]]): Unit = pv match {
            case (Pos(x,y), v) => board(x)(y) = v
    }

    def insert(n: Int, p: Pos): Outcome = {
        if (posIsNotEditable apply p)
            Outcome.Failure
        else if (posIsEditable apply p)
            this getCell p <<= n
        else {
            this `=` (p -> SudokuCell(n))
            Outcome.Successful
        }

    }

}

object SudokuBoard {
    private val boardH = 9
    private val boardW = 9

    def main(args: Array[String]): Unit = {
        println("Testing SudokuBoard.scala")

        val sb = new SudokuBoard
        val checkTest: Tuple2[Pos,Int] => Boolean = BoardChecker assertLacks sb
        val checkVal = 23

        sb `=` (new Pos(8,0)) -> SudokuCell(23, true)

        for(r <- 0 until 9; c <- 0 until 9)
            sb.insert(c, new Pos(r,c))
        
        for(r <- 0 until 9) {
            for( c <- 0 until 9)
                print(sb(r)(c) + " ")
            println()
        }
        
        println("Does the lower left corner not contain " + checkVal + ": " + checkTest {(new Pos(7,2)) -> checkVal})
    }

    private object BoardChecker {
        def boardPositionIsImmutable:
            SudokuBoard => Pos => Boolean = b => p => (b getCell p).isInstanceOf[Immutable]
        def boardPositionIsEditable:
            SudokuBoard => Pos => Boolean = b => p => (b getCell p).isInstanceOf[Editable]

        def assertLacks: SudokuBoard => Tuple2[Pos,Int] => Boolean = (sBoard: SudokuBoard) => (pv: Tuple2[Pos,Int]) => pv match {
                case (pos, forValueIn) =>
                    check (sBoard -> forValueIn -> {Row    thatContains pos}) &&
                    check (sBoard -> forValueIn -> {Column thatContains pos}) &&
                    check (sBoard -> forValueIn -> {Square thatContains pos})
        }

        def check: Tuple2[Tuple2[SudokuBoard,Int],IndexedSeq[Pos]] => Boolean = pv => pv match {
            case ((sb, v),ps) => ps forall {(pos: Pos) => sb(pos) != v} 
        }

        // def checkAll: SudokuBoard => Boolean = sb => {
        //     for(ps <- FullBoard.allPositions)
                
        // }

        // def isUniqueSequence(sq: IndexedSeq[Pos]): Boolean = sq match {
        //     case (n, ns*) => 

        // }
    }

    private trait SudokuBoardVirtualPartition {
        def apply(n: Int): IndexedSeq[Pos] = ???
        def thatContains: Pos => IndexedSeq[Pos] = ???
    }


    private object Row extends SudokuBoardVirtualPartition {
        override def apply(n: Int): IndexedSeq[Pos] = thatContains(new Pos(n,0))

        override def thatContains: Pos => IndexedSeq[Pos] = p => p match {
            case Pos(x, _) => 
                for (c <- 0 until SudokuBoard.boardW)
                    yield new Pos(x,c)
        }
    }

    private object Column extends SudokuBoardVirtualPartition {
        override def apply(n: Int): IndexedSeq[Pos] = thatContains(new Pos(0,n))

        override def thatContains: Pos => IndexedSeq[Pos] = p => p match {
            case Pos(_, y) =>
                for (r <- 0 until SudokuBoard.boardH)
                    yield new Pos(r,y)
        }
    }

    private object Square extends SudokuBoardVirtualPartition {
        val squareDim: Int = math.sqrt(boardH).toInt

        override def apply(n: Int): IndexedSeq[Pos]         = _apply(flatTransform(n))
        def  apply(x: Int, y: Int): IndexedSeq[Pos]         = thatContains(new Pos(revert apply x, revert apply y))
        def _apply(p: Pos): IndexedSeq[Pos]                 = apply(p.x, p.y)

        override def thatContains: Pos => IndexedSeq[Pos] = p => p match { 
            case Pos(x,y) => {
                val rowTransform: Int => Int = transform apply x
                val colTransform: Int => Int = transform apply y

                for (r <- 0 until squareDim; c <- 0 until squareDim)
                     yield new Pos(rowTransform apply r, colTransform apply c)

            }
        }

        def transform:      Int => Int => Int = m => n => m / squareDim * squareDim + n
        def revert:         Int => Int        = n => n * squareDim
        def flatTransform:  Int => Pos        = n => new Pos(n / squareDim, n % squareDim)
    }

    private object FullBoard extends SudokuBoardVirtualPartition {
        val squareDim = boardH

        def apply(): IndexedSeq[Pos]                = containsAll
        override def apply(n: Int): IndexedSeq[Pos] = apply()

        def allPositions: Array[IndexedSeq[Pos]] = {
            for(part <- Array(Row,Column,Square); idx <- 0 until squareDim)
                yield part(idx)
        }

        override def thatContains: Pos => IndexedSeq[Pos] = _ => {
            allPositions.flatten[Pos]
        }

        def containsAll: IndexedSeq[Pos] = allPositions.flatten[Pos]
    }
}

