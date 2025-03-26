package mok.it.tortura.model

enum class SolutionState {
    CORRECT,
    INCORRECT,
    EMPTY;

    fun toBoolean(): Boolean {
        return this == CORRECT
    }
}
