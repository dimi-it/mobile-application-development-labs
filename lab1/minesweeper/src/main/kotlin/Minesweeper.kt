data class MinesweeperBoard(val rows: List<String>) {

    val matrix: List<MutableList<Char>> = rows.map { row -> row.toMutableList() }

    fun countAdj(row: Int, col:Int): Char{
        var count: Int = 0
        val lx = if(col > 0) col-1 else col
        val rx = if(col < matrix[0].size - 1) col+1 else col
        val up = if(row > 0) row-1 else row
        val dw = if(row < matrix.size - 1) row+1 else row
        for(i in up..dw){
            for(j in lx..rx){
                if(matrix[i][j] == '*'){
                    count += 1
                }
            }
        }
        return if(count == 0){
            ' '
        } else{
            count.digitToChar()
        }
    }

    fun withNumbers(): List<String> {
        for(i in matrix.indices){
            for(j in matrix[0].indices){
                if(matrix[i][j] != '*'){
                    matrix[i][j] = countAdj(i, j)
                }
            }
        }
        return matrix.map { row -> row.joinToString(separator = "") }
    }
}
