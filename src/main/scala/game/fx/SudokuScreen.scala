package game.fx

object SudokuScreen {
    private val g = Console.GREEN;      private val G = Console.GREEN_B
    private val r = Console.RED;        private val R = Console.RED_B
    private val m = Console.MAGENTA;    private val M = Console.MAGENTA_B
    private val b = Console.BLUE;       private val B = Console.BLUE_B
    private val w = Console.WHITE;      private val W = Console.WHITE_B
    private val c = Console.CYAN;       private val C = Console.CYAN_B
    private val k = Console.BLACK;      private val K = Console.BLACK_B
    private val o = Console.RESET;      private val O = Console.BOLD

    //Top left corner of the board
    private val (x,y) = (35,8)

    def SudokuBoard: String = {
        var j  = this.y - 1
        val pxy: Unit => String = _ => {j+=1;"\u001B[" + j + ";" + x + "H"}

        val b = this.m
        val W = this.G
        
        s"""
        ${pxy()}$b$W|-----------o-----------o-----------|$o        
        ${pxy()}$b$W|$o           $b$W|$o           $b$W|$o           $b$W|$o
        ${pxy()}$b$W|$o           $b$W|$o           $b$W|$o           $b$W|$o    -> Use your ${c}arrow keys$o to move
        ${pxy()}$b$W|$o           $b$W|$o           $b$W|$o           $b$W|$o
        ${pxy()}$b$W|$o           $b$W|$o           $b$W|$o           $b$W|$o    -> Press ${r}q$o to quit
        ${pxy()}$b$W|$o           $b$W|$o           $b$W|$o           $b$W|$o
        ${pxy()}$b$W|-----------o-----------o-----------|$o
        ${pxy()}$b$W|$o           $b$W|$o           $b$W|$o           $b$W|$o
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

    def main(args: Array[String]): Unit = {
        println("Hello from SudokuScreen.")
        print(SudokuBoard)
    }
}