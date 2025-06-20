package mok.it.tortura.ui.components

import HelpIcon
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

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
fun HelpButton(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    IconButton(
        onClick = onClick,
        modifier = modifier
    ) {
        HelpIcon()
    }
}