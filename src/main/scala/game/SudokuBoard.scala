package game

import com.typesafe.config.ConfigFactory
import game.util.{Pos, Outcome, Editable}
import math.random

class SudokuBoard {
    private val board: Array[Array[SudokuCell[Int]]] = Array.ofDim[SudokuCell[Int]](SudokuBoard.boardH,SudokuBoard.boardW)

    private val posIsNotEditable = SudokuBoard.BoardChecker.boardPositionIsImmutable(this)
    private val posIsEditable    = SudokuBoard.BoardChecker.boardPositionIsEditable(this)
    private val isValid          = SudokuBoard.BoardChecker.assertLacks(this)

    val checkBoard: Unit => Boolean = _ => SudokuBoard.BoardChecker.checkAll(this)

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
    val boardH = 9
    val boardW = 9
    val emptyRep = "_"

    def main(args: Array[String]): Unit = {
        println("Testing SudokuBoard.scala")

        val sb = SudokuBoard()
        val checkTest: Tuple2[Pos,Int] => Boolean = BoardChecker assertLacks sb
        val checkVal = 23
        var expectedResult = false

        sb `=` (new Pos(8,0)) -> SudokuCell(23, true)

        for (r <- 0 until SudokuBoard.boardH) {
            for (c <- 0 until SudokuBoard.boardW)
                print(sb(r)(c) + " ")
            println()
        }
        
        println("Does the lower left corner not contain " + checkVal + ": " + checkTest {(new Pos(7,2)) -> checkVal})
        println(if (expectedResult == checkTest {(new Pos(7,2)) -> checkVal}) s"Corner test: [${Console.GREEN}PASS${Console.RESET}]" else s"Corner test: [${Console.RED}FAIL${Console.RESET}]")

        expectedResult = false
        val result = BoardChecker.isUniqueSequence(sb, Square thatContains {new Pos(0,5)})

        println(s"Checking isUniqueSeq function -> Expected result: $expectedResult -> (actual value): " + result)
        println(if (expectedResult == result) s"Uniqueness test: [${Console.GREEN}PASS${Console.RESET}]" else s"Uniqueness test: [${Console.RED}FAIL${Console.RESET}]")
    }

    def apply(): SudokuBoard = SudokuBoard(ConfigFactory.load().getInt("defaultFill"))

    def apply(cellsToFill: Int): SudokuBoard = {
        val sb = new SudokuBoard
        editableFill(sb)
        randomImmutableFill(sb, cellsToFill)
        sb
    }

    private def randomImmutableFill(sb: SudokuBoard, numberOfCellsToFill: Int): Unit   = {
        if (numberOfCellsToFill > 0) {
            val ecs = getEditableCells apply sb
            val checkValid: Tuple2[Pos,Int] => Boolean = SudokuBoard.BoardChecker.assertLacks(sb)
            val posSelect = generateNumber apply 0 -> (ecs.length - 1)

            while(cellIsNotImmutable(sb -> ecs(posSelect))) {
                val value = generateVal

                if(checkValid apply ecs(posSelect) -> value)
                    sb.board(ecs(posSelect).x)(ecs(posSelect).y) = SudokuCell(value, true)
            }

            randomImmutableFill(sb, numberOfCellsToFill - 1)
        }
    }

    private def editableFill(sb: SudokuBoard): Unit     = {
        for (r <- 0 until SudokuBoard.boardH; c <- 0 until SudokuBoard.boardW)
            sb.board(r)(c) = SudokuCell(SudokuCell.EMPTY)
    }

    private def getEditableCells: SudokuBoard => IndexedSeq[Pos] = sb => {
        for (r <-0 until SudokuBoard.boardH; c <- 0 until SudokuBoard.boardW if SudokuBoard cellIsNotImmutable sb -> (new Pos(r,c)))
            yield new Pos(r,c)
    }

    private def cellIsNotImmutable(bp: Tuple2[SudokuBoard,Pos]): Boolean = bp match {case (sb,p) => !BoardChecker.boardPositionIsImmutable(sb)(p)}

    private def generateNumber: Tuple2[Int,Int] => Int = se => se match {
        case (start, end) =>
            ((random * 10 * (end + 1) - 1).toInt + start * 10) / 10
    }

    private def generateSBNumber: Int => Int  = n => generateNumber apply n -> (SudokuBoard.boardH - 1)
    private def generatePos: Pos            = new Pos(generateSBNumber apply 0, generateSBNumber apply 0)
    private def generateVal: Int            = generateSBNumber apply 1

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

        def checkAll: SudokuBoard => Boolean = sb => SudokuBoard.BoardChecker.isFilledCompletely(sb) && FullBoard.allPositions.forall(ps => isUniqueSequence(sb, ps))
        
        def isFilledCompletely: SudokuBoard => Boolean = sb => sb.board.forall(row => row.forall(cell => cell.value != SudokuCell.EMPTY))

        def isUniqueSequence(sb: SudokuBoard, sq: IndexedSeq[Pos]): Boolean = sq match {
            case IndexedSeq(n)          => true
            case IndexedSeq(n, ns @ _*) => ns.forall(m => sb(n) != sb(m)) && isUniqueSequence(sb, ns.asInstanceOf[IndexedSeq[Pos]])
        }
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