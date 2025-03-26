package mok.it.tortura.model

import kotlinx.serialization.Serializable

@Serializable
data class ProblemSet(val name: String, val blocks: List<Block>, val category: String = "")