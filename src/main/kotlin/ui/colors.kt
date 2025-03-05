package ui

import androidx.compose.ui.graphics.Color

enum class CategoryColors(
    val textColor: Color,
    val backgroundColor: Color,
    ){
    BOCS(
        backgroundColor =  Color(171, 32, 17),
        textColor = Color.White
    ),
    KIS(
        backgroundColor = Color(207, 198, 35),
        textColor =  Color.White,
        ),
    NAGY(
        backgroundColor = Color(12,135,47),
        textColor = Color.White
    ),
    JEGES(
        backgroundColor = Color(37, 195, 219),
        textColor = Color.White
    )

}