package game.fx

import game.SudokuBoard
import game.util.Pos
import game.fx.util.SCon
import game.SudokuCell
import game.util.Outcome

class SudokuScreen(private val sBoard: SudokuBoard) {
    private var caret = (0,0)

    def initialFill: Unit = {
        for(r <- 0 until SudokuBoard.boardH; c <- 0 until SudokuBoard.boardW if sBoard(r)(c) != SudokuCell.EMPTY)
            SudokuScreen.printToScreen(
                (SudokuScreen.coordinateTransform(new Pos(r, c)), sBoard(r)(c)),
                SudokuScreen.r)
        
        for(r <- 0 until SudokuBoard.boardH; c <- 0 until SudokuBoard.boardW if sBoard(r)(c) == SudokuCell.EMPTY)
            SudokuScreen.printToScreen(SudokuScreen.coordinateTransform(new Pos(r,c)) -> SudokuBoard.emptyRep, SudokuScreen.w)
    }
    
    def insertVal(a: Any): Outcome = sBoard.insert(a.toString.toInt, caretToPos)
    def callSudokuBoard: SudokuBoard = sBoard

    def caretToPos: Pos = new Pos(caret._1, caret._2)

    def moveCaretRight: Unit = {
        caret = SudokuScreen.guardedSuccessor(caret._1) -> caret._2
        val Pos(r,z) = SudokuScreen.coordinateTransform(caretToPos)
        SCon.caretTo(r,z); Thread.sleep(25l)
    }

    def moveCaretLeft: Unit = {
        caret = SudokuScreen.guardedPredecessor(caret._1) -> caret._2
        val Pos(r,z) = SudokuScreen.coordinateTransform(caretToPos)
        SCon.caretTo(r,z); Thread.sleep(25l)
    }

    def moveCaretDown: Unit = {
        caret = caret._1 -> SudokuScreen.guardedSuccessor(caret._2)
        val Pos(r,z) = SudokuScreen.coordinateTransform(caretToPos)
        SCon.caretTo(r,z); Thread.sleep(25l)
    }

    def moveCaretUp: Unit = {
        caret = caret._1 -> SudokuScreen.guardedPredecessor(caret._2)
        val Pos(r,z) = SudokuScreen.coordinateTransform(caretToPos)
        SCon.caretTo(r,z); Thread.sleep(25l)
    }
}

object SudokuScreen {
    private val g = Console.GREEN;      private val G = Console.GREEN_B
    private val r = Console.RED;        private val R = Console.RED_B
    private val m = Console.MAGENTA;    private val M = Console.MAGENTA_B
    private val b = Console.BLUE;       private val B = Console.BLUE_B
    private val w = Console.WHITE;      private val W = Console.WHITE_B
    private val c = Console.CYAN;       private val C = Console.CYAN_B
    private val k = Console.BLACK;      private val K = Console.BLACK_B
    private val o = Console.RESET;      private val O = Console.BOLD

    
    private val (x,y) = (35,10)     //Define top left corner of the board
    private val dX = 4
    private val dY = 2
    private val initX_offset = 2
    private val initY_offset = 1

    private val (i,j) = (15, 40)    //Define safe print position

    def apply(): SudokuScreen = {
        val sb = new SudokuScreen(SudokuBoard())
        sb.initialFill
        SudokuScreen.resetCaretPosition
        SudokuScreen.resetConsoleFormatting
        sb
    }
    def SudokuGrid: String = {
        var j  = this.y - 1
        val pxy: Unit => String = _ => {j+=1;"\u001B[" + j + ";" + x + "H"}

        val b = this.g
        val W = this.G
        
        s"""
        ${pxy()}$b$W|-----------o-----------o-----------|$o        
        ${pxy()}$b$W|$o           $b$W|$o           $b$W|$o           $b$W|$o
        ${pxy()}$b$W|$o           $b$W|$o           $b$W|$o           $b$W|$o    -> Use your ${c}arrow keys$o to move
        ${pxy()}$b$W|$o           $b$W|$o           $b$W|$o           $b$W|$o
        ${pxy()}$b$W|$o           $b$W|$o           $b$W|$o           $b$W|$o    -> Press ${g}s$o to submit your sudoku
        ${pxy()}$b$W|$o           $b$W|$o           $b$W|$o           $b$W|$o
        ${pxy()}$b$W|-----------o-----------o-----------|$o
        ${pxy()}$b$W|$o           $b$W|$o           $b$W|$o           $b$W|$o    -> Press ${r}q$o to quit
        ${pxy()}$b$W|$o           $b$W|$o           $b$W|$o           $b$W|$o
        ${pxy()}$b$W|$o           $b$W|$o           $b$W|$o           $b$W|$o
        ${pxy()}$b$W|$o           $b$W|$o           $b$W|$o           $b$W|$o
        ${pxy()}$b$W|$o           $b$W|$o           $b$W|$o           $b$W|$o
        ${pxy()}$b$W|-----------o-----------o-----------|$o
        ${pxy()}$b$W|$o           $b$W|$o           $b$W|$o           $b$W|$o
        ${pxy()}$b$W|$o           $b$W|$o           $b$W|$o           $b$W|$o
        ${pxy()}$b$W|$o           $b$W|$o           $b$W|$o           $b$W|$o
        ${pxy()}$b$W|$o           $b$W|$o           $b$W|$o           $b$W|$o
        ${pxy()}$b$W|$o           $b$W|$o           $b$W|$o           $b$W|$o
        ${pxy()}$b$W|-----------o-----------o-----------|$o
        """

    }

    def SudokuBanner: String = {
        
        s"""
        $O$G$m  ____            _       _                                                                $o
        $O$C$b / ___| _   _  __| | ___ | | ___   _                                                       $o
        $O$R$c \\___ \\| | | |/ _` |/ _ \\| |/ / | | |                                                      $o
        $O$W$r  ___) | |_| | (_| | (_) |   <| |_| |                                                      $o
        $O$M$w |____/ \\__,_|\\__,_|\\___/|_|\\_\\\\__,_|                                                      $o
        $o
        """
    }

    def coordinateTransform: Pos => Pos = p =>
            new Pos(p.x * dX + initX_offset + x, p.y * dY + initY_offset + y)
    
    def printToScreen(pv: Tuple2[Pos,Any], fmt: String = ""): Unit = pv match {
        case (Pos(r,z),a) => {SCon.caretTo(r,z); print(fmt + a)}
    }

    def printAtCaretPosition(ss: SudokuScreen, a: Any, fmt: String = ""): Unit = {
        val Pos(tx, ty) = coordinateTransform(ss.caretToPos)
        SCon.caretTo(tx, ty)
        print(fmt + a)
    }

    def safePrint(message: String, fmt: String = ""): Unit = {
        SCon.caretTo(i,j)
        print(fmt + message);           Thread.sleep(1500l)
        resetConsoleFormatting
        SCon.caretTo(i,j)
        SCon.delLine
    }

    def safeInput(message: String, fmt: String = ""): String = {
        SCon.caretTo(i,j)
        val res = io.StdIn.readLine(fmt+message+Console.RESET)
        SCon.caretTo(i,j)
        SCon.delLine
        res
    }

    def resetCaretPosition: Unit = coordinateTransform(new Pos(0,0)) match {case Pos(i,j) => SCon.caretTo(i,j)}
    def resetConsoleFormatting: Unit = print(o)

    private def guardBounds: Tuple2[Int,Boolean] => Boolean = nb => nb match {
        case (n,b) => (0 < n && n < predecessor(SudokuBoard.boardH)) || b
    }

    private def guardedSuccessor: Int => Int = n => if (guardBounds(n -> (n == 0))) successor(n) else n
    private def guardedPredecessor: Int => Int = n => if (guardBounds(n -> (n == predecessor(SudokuBoard.boardH)))) predecessor(n) else n

    private def successor: Int => Int = n => n + 1
    private def predecessor: Int => Int = n => n - 1

    def main(args: Array[String]): Unit = {
        println("Hello from SudokuScreen\u00B2.")
        print(SudokuBoard)
    }
}