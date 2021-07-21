import game.fx.SudokuScreen

object Sudoku {
    

    def main(args: Array[String]): Unit = {
        intro
    }

    def intro: Unit = {
        val resetScreenAndCursor = "\u001B[2J\u001B[0;0H"
        print(resetScreenAndCursor)
        println("Hello World!");                            Thread.sleep(3500l)
        io.StdIn.readLine("\n\n\u001B[s[Press Enter to Exit]")
        println("\u001B[uWell, this is awkward...So...");   Thread.sleep(2500l)

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

        println(SudokuScreen.SudokuBanner)

        println("\n\n\n")
        println(s"Anyway, it's been a pleasure meeting you ${Console.BOLD}$name${Console.RESET}")
        io.StdIn.readLine("\u001B[s[Press Enter to Exit]")

        println("\u001B[uWho am I kidding? Let's play!")
        blockingPrint apply 7 -> ""
        print(resetScreenAndCursor)

        println(SudokuScreen.SudokuBanner + "\n\n\n\n")
        println(SudokuScreen.SudokuBoard)
    }

    def blockingPrint: Tuple2[Int,String] => Unit = rs => rs match {
        case (reps, str) => (1 to reps).foreach( _ => {Thread.sleep(500l); print(str)} )
    }  
}