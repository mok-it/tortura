package mok.it.tortura.ui.components

import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable

@Composable
fun HelpDialog(
    onDismiss: () -> Unit,
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        text = {
            Text( "Ha idáig eljutottál inkább kérj segítséget!" )
        },
        confirmButton = {
            TextButton(onClick = onDismiss) {
                Text("Megyek segítséget kérni")
            }
        }
    )
}

@Composable
fun ParseErrorPopup(onDismiss: () -> Unit) {
    GenericErrorPopup(
        "Formátumhiba",
        "A fájl formátuma helytelen, biztosan jót választottál?" +
                "Excel fájlok beolvasása még nem lehetséges, a beosztást az appban hozd létre!",
        onDismiss
    )
}

@Composable
fun TypeErrorPopup(onDismiss: () -> Unit) {
    GenericErrorPopup(
        "Tartalomhiba",
        "A fájl formátuma helyes ,de tartalma nem értelmezhető. Biztos jót választottál?" +
                "Baloldat csapatbeosztást (.csp), jobboldalt feladatsort (.fes) válassz ki!",
        onDismiss
    )
}

@Composable
fun SaveErrorPopup(onDismiss: () -> Unit) {
    GenericErrorPopup(
        "Mentési hiba",
        "A fájl mentése nem sikerült, mert egy vagy több helyen" +
                "nincs csapat vagy feladatsor kiválasztva.",
        onDismiss
    )
}

@Composable
fun GenericErrorPopup(
    title: String,
    text: String,
    onDismiss: () -> Unit
) {
    AlertDialog(
        title = { Text(title) },
        text = { Text(text) },
        onDismissRequest = onDismiss,
        confirmButton = {
            Button(
                onClick = onDismiss,
            ) { Text("Rendben") }
        }
    )
}

@Composable
fun ExcelErrorPopup(
    onDismiss: () -> Unit,
) {
    GenericErrorPopup(
        "Excel hiba",
        "Az Excel fájl beolvasása nem sikerült, mert a fájl formátuma nem megfelelő." +
                "Kérlek ellenőrizd a fájlt és próbáld újra.",
        onDismiss
    )
}