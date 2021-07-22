package game.fx.util

object SCon {
    val esc     = '\u001B'
    val bra     = '['
    val eb      = esc.toString + bra
    val bRed    = Console.RED_B
    val white   = Console.WHITE
    val bold    = Console.BOLD
    val right   = 'C'
    val left    = 'D'
    val down    = 'B'
    val up      = 'A'

    
    def caretTo(x: Int, y: Int):    Unit = print(eb + y + ";" + x + "H")
    def caretSave:                  Unit = print(eb + "s")
    def caretRestore:               Unit = print(eb + "u")
    def delLine:                    Unit = print(eb + 2 + "K")
}