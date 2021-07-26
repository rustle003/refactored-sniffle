import game.fx.SudokuScreen
import game.SudokuBoard
import game.fx.util.SCon
import game.util.Outcome

object Sudoku {
    var userQuit         = false
    var gameover         = false

    def main(args: Array[String]): Unit = {
        intro

        val ss = SudokuScreen()

        do
        {
            val cs = safeInput("Enter an action or an input: ")

            if (cs != null && cs.length > 0)    
                cs.foreach(n => reactToInput(n, ss))
        }while(!userQuit && !gameover)

        endMessage
    }
    
    def reactToInput(c: Char,ss: SudokuScreen): Unit = c match {
        case SCon.right                 => ss.moveCaretRight
        case SCon.left                  => ss.moveCaretLeft
        case SCon.down                  => ss.moveCaretDown
        case SCon.up                    => ss.moveCaretUp
        case 'q'                        => userQuit = true
        case 's'                        => gameover = if(ss.callSudokuBoard.checkBoard()) true else {errMessage; false}
        case e if e == '0' || e == '_'  => if (ss.insertVal(0) == Outcome.Successful) SudokuScreen.printAtCaretPosition(ss, '_') 
        case n if('1' to '9').contains(n)   => if(ss.insertVal(n) == Outcome.Successful) SudokuScreen.printAtCaretPosition(ss, n)
        case _                          => ()
    }

    def safeInput(message: String): String = {
        SudokuScreen.safeInput(message, SCon.bold)
    }

    def errMessage: Unit = {
        SudokuScreen.safePrint("You're sudoku is wrong. Please consider quitting.",
                                SCon.bRed+SCon.white)
    }

    def endMessage: Unit = {
        if (gameover)
            SudokuScreen.safePrint("That is correct! Great job!", SCon.bold)
        else
            SudokuScreen.safePrint("Thank you for playing!", SCon.bold)
    }

    def intro: Unit = {
        val resetScreenAndCursor = "\u001B[2J\u001B[0;0H"
        print(resetScreenAndCursor)
        println("Hello World!");                            Thread.sleep(3500l)
        io.StdIn.readLine("\n\n\u001B[s[Press Enter to Exit]")
        println("\u001B[uWell, this is awkward...")
        println("Program execution didn't end...So...");   Thread.sleep(2500l)

        val name = io.StdIn.readLine("\nTell me, what is your name?\n")

        println(s"\nNice to meet you ${Console.BOLD}$name${Console.RESET}.")
        blockingPrint apply 3 -> ""
        print("\n\nMy name is")

        blockingPrint apply 7 -> "."
        print("wait for it")
        blockingPrint apply 7 -> "."
        println()
        print(s"\u001B[s${Console.CYAN}loading${Console.RESET} .")
        blockingPrint apply 10 -> "."
        print("\u001B[uWait for it again")
        blockingPrint apply 10 -> "."
        println("\n\n\n")

        SudokuScreen.SudokuBanner.foreach(c => specialPrint apply c)

        println("\n\n\n")
        println("I'm sure you'd like to play, but I've already solved it for you.");    Thread.sleep(2500l)
        println("Just trust me. I did it.");                                            Thread.sleep(2000l)
        println(s"Anyway, it's been a pleasure meeting you ${Console.BOLD}$name${Console.RESET}")
        io.StdIn.readLine("\u001B[s[Press Enter to Exit]")

        println("\u001B[uWho am I kidding? Let's play!");                               Thread.sleep(2000l)
        print(resetScreenAndCursor)

        println(SudokuScreen.SudokuBanner + "\n\n\n\n")
        println(SudokuScreen.SudokuGrid)
    }

    def blockingPrint: Tuple2[Int,String] => Unit = rs => rs match {
        case (reps, str) => (1 to reps).foreach( _ => {Thread.sleep(500l); print(str)} )
    }
    
    def specialPrint: Char => Unit = c => {Thread.sleep(10l); print(c)}
}