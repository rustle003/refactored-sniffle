package game.fx

object SudokuScreen {
    private val g = Console.GREEN;      private val G = Console.GREEN_B
    private val r = Console.RED;        private val R = Console.RED_B
    private val m = Console.MAGENTA;    private val M = Console.MAGENTA_B
    private val b = Console.BLUE;       private val B = Console.BLUE_B
    private val w = Console.WHITE;      private val W = Console.WHITE_B
    private val c = Console.CYAN;       private val C = Console.CYAN_B
    private val k = Console.BLACK;      private val K = Console.BLACK_B
    private val x = Console.RESET;      private val X = Console.BOLD

    def SudokuBoard: String = {
        s"""
                               |---------o---------o---------|        
                               |         |         |         |
                               |         |         |         |
                               |         |         |         |
                               |---------o---------o---------|
                               |         |         |         |
                               |         |         |         |
                               |         |         |         |
                               |---------o---------o---------|
                               |         |         |         |
                               |         |         |         |
                               |         |         |         |
                               |---------o---------o---------|
        """

    }

    def SudokuBanner: String = {
        
        s"""
        $X$G$m  ____            _       _                                                                $x
        $X$C$b / ___| _   _  __| | ___ | | ___   _                                                       $x
        $X$R$c \\___ \\| | | |/ _` |/ _ \\| |/ / | | |                                                      $x
        $X$W$r  ___) | |_| | (_| | (_) |   <| |_| |                                                      $x
        $X$M$w |____/ \\__,_|\\__,_|\\___/|_|\\_\\\\__,_|                                                      $x
        $x
        """
    }
}