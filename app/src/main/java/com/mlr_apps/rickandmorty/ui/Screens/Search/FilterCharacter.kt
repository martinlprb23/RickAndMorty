package com.mlr_apps.rickandmorty.ui.Screens.Search

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog


@Composable
fun FilterDialog(
    openDialogCustom: MutableState<Boolean>
) {
    Dialog(onDismissRequest = { openDialogCustom.value = false}) {
        CustomDialogUI(
            openDialogCustom = openDialogCustom,
            modifier = Modifier.width(250.dp)
        )
    }
}

//Layout
@Composable
fun CustomDialogUI( modifier: Modifier = Modifier, openDialogCustom: MutableState<Boolean>){
    Card(
        shape = RoundedCornerShape(10.dp),
        elevation = 8.dp
    ) {
        Column(
            modifier
                .background(MaterialTheme.colors.background)) {
            //.......................................................................

            Box(
                Modifier
                    .fillMaxWidth()
                    .clickable { openDialogCustom.value = false },
                contentAlignment = Alignment.Center) {

                Text(
                    "Close",
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(top = 16.dp, bottom = 16.dp)
                )

            }
        }
        //.......................................................................
    }
}

