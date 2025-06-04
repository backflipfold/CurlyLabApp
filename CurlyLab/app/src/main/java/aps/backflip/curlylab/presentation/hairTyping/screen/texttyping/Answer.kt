package aps.backflip.curlylab.presentation.hairTyping.screen.texttyping

data class Answer (
    val result: String,
    val text: String,
    var isSelected: Boolean
)

enum class PorosityTypes(val code: String, val result: String) {
    POROUS("p", "пористые"),
    SEMI_POROUS("s", "среднепористые"),
    NON_POROUS("n", "низкопористые")
}

enum class ThicknessTypes(val code: String, val result: String) {
    THIN("t", "тонкие"),
    MEDIUM("m", "средние"),
    BOLD("b", "толстые")
}


enum class ColoredTypes(val code: String, val result: String) {
    COLORED("c", "окрашенные"),
    NOT_COLORED("n", "неокрашенные"),
}