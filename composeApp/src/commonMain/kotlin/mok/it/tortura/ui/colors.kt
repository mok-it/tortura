package mok.it.tortura.ui

import androidx.compose.ui.graphics.Color

data class CategoryColors(
    val textColor: Color,
    val backgroundColor: Color,
    ){

    companion object {
        val BOCS = CategoryColors(
            backgroundColor =  Color(171, 32, 17),
            textColor = Color.White
        )
        val KIS = CategoryColors(
            backgroundColor = Color(207, 198, 35),
            textColor =  Color.White,
        )
        val NAGY = CategoryColors(
            backgroundColor = Color(12,135,47),
            textColor = Color.White
        )
        val JEGES = CategoryColors(
            backgroundColor = Color(37, 195, 219),
            textColor = Color.White
        )
        val UNDIFINED = CategoryColors(
            backgroundColor = Color.Unspecified,
            textColor = Color.Unspecified
        )
    }

}