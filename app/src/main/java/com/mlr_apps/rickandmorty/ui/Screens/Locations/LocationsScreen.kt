package com.mlr_apps.rickandmorty.ui.Screens.Locations

import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.mlr_apps.rickandmorty.Data.Model.LocationsListEntry
import com.mlr_apps.rickandmorty.R
import com.mlr_apps.rickandmorty.ui.theme.RickAndMortyTheme
import com.mlr_apps.rickandmorty.ui.theme.RobotoCondensed


@Composable
fun LocationsScreen() {
    RickAndMortyTheme {
        Surface(
            modifier = Modifier.fillMaxSize(),
            color = MaterialTheme.colors.background
        ) {

            Column(
                modifier = Modifier.padding(top = 50.dp, start = 16.dp, end = 16.dp),
                Arrangement.spacedBy(24.dp)
            ) {

                Text(
                    text = "Locations",
                    fontFamily = RobotoCondensed,
                    fontWeight = FontWeight.Bold,
                    fontSize = 21.sp,
                )

                LocationList()
            }
        }
    }
}

@Composable
fun LocationList(viewModel: LocationsViewModel = hiltViewModel()) {

    val locationsList by remember{viewModel.locationsList}
    val endReached by remember{viewModel.endReached}
    val loadError by remember{viewModel.loadError}
    val isLoading by remember{viewModel.isLoading}

    LazyColumn(
        //contentPadding = PaddingValues(start = 16.dp, end = 16.dp, bottom = 16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp),
        modifier = Modifier.fillMaxWidth()
    ){

        val itemCount = locationsList.size
        items(itemCount){
            if (it>=itemCount-1 && !endReached && !isLoading){
                viewModel.loadLocationsPaginated()
            }
            LocationsEntry(locationsList[it])
        }
    }

    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier.fillMaxSize()
    ) {
        if (isLoading){
            CircularProgressIndicator(color = MaterialTheme.colors.primary)
        }

        if (loadError.isNotEmpty()){
            com.mlr_apps.rickandmorty.ui.Screens.Characters.RetrySection(error = loadError) {
                viewModel.loadLocationsPaginated()
            }
        }
    }

}

@Composable
fun LocationsEntry(
    entry: LocationsListEntry,
) {
    val uri = entry.locationName

    Box(modifier = Modifier
        .shadow(5.dp, RoundedCornerShape(10.dp))
        .clip(RoundedCornerShape(10.dp))
        .fillMaxSize()
        .height(80.dp)
        .background(
            Brush.horizontalGradient(
                listOf(
                    Color.Green, MaterialTheme.colors.surface)))
        .clickable {
            //startActivity(context, intent, null)
        }
    ){
        Row {

            Image(
                painter = painterResource(id = R.drawable.portal),
                contentDescription = "Image location",
                modifier = Modifier.size(100.dp),
                contentScale = ContentScale.Crop
            )

            Column(modifier = Modifier
                .padding(8.dp)
                .fillMaxSize(),
                Arrangement.SpaceEvenly) {

                Text(
                    text = entry.locationName,
                    fontFamily = RobotoCondensed,
                    fontWeight = FontWeight.Bold,
                    fontSize = 16.sp
                )

                Text(
                    text = "${entry.type} - ${entry.locationName}",
                    fontSize = 12.sp,
                )
            }
        }
    }
}



@Composable
fun RetrySection(
    error: String,
    onRetry: () -> Unit
){
    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(bottom = 100.dp),
        contentAlignment = Alignment.BottomCenter
    ){
        Column {
            Text(text = error, color = Color.Red, fontSize = 18.sp)
            Spacer(modifier = Modifier.height(8.dp))
            Button(
                onClick = { onRetry() },
                modifier = Modifier
                    .align(Alignment.CenterHorizontally),
                shape = RoundedCornerShape(50.dp)
            ) {
                Text(text = "Retry")
            }
        }
    }

}



