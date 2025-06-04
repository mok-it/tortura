package mok.it.tortura.util

import kotlinx.serialization.json.Json
import mok.it.tortura.model.Competition

val mapJsonFormat = Json{allowStructuredMapKeys = true}

fun Competition.toJson() = mapJsonFormat.encodeToString( this)