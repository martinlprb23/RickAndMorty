package com.mlr_apps.rickandmorty.ui.Screens

import android.annotation.SuppressLint
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import coil.compose.rememberImagePainter


@Composable
fun CustomDialog(
    image: String,
    openDialogCustom: MutableState<Boolean>) {
    Dialog(onDismissRequest = { openDialogCustom.value = false}) {
        CustomDialogUI(
            imageUrl = image,
            openDialogCustom = openDialogCustom,
            modifier = Modifier.width(250.dp)
        )
    }
}

//Layout
@Composable
fun CustomDialogUI(imageUrl:String,modifier: Modifier = Modifier, openDialogCustom: MutableState<Boolean>){
    Card(
        shape = RoundedCornerShape(10.dp),
        elevation = 8.dp
    ) {
        Column(
            modifier
                .background(MaterialTheme.colors.background)) {

            //.......................................................................
            Image(
                painter = rememberImagePainter(
                    data = imageUrl,
                    builder = {
                        crossfade(true)
                    }),
                contentDescription = null, // decorative
                contentScale = ContentScale.Fit,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(250.dp)
                )

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



@SuppressLint("UnrememberedMutableState")
@Preview(name="Custom Dialog")
@Composable
fun MyDialogUIPreview(){
    CustomDialogUI("https://www.infobae.com/new-resizer/WecI9uI570A7yEKb6RecdPht23c=/768x512/filters:format(webp):quality(85)/arc-anglerfish-arc2-prod-infobae.s3.amazonaws.com/public/YHAUBUYW75FZVLG4Z4WL5S6LMY.jpg",openDialogCustom = mutableStateOf(false))
}