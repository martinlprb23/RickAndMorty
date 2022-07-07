package com.mlr_apps.rickandmorty.ui.Screens.Episodes


import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.mlr_apps.rickandmorty.Data.Model.EpisodesListEntry
import com.mlr_apps.rickandmorty.R
import com.mlr_apps.rickandmorty.ui.theme.RickAndMortyTheme
import com.mlr_apps.rickandmorty.ui.theme.RobotoCondensed
import com.mlr_apps.rickandmorty.ui.theme.backgroundName

@Composable
fun EpisodesScreen() {
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
                    text = "Episodes",
                    fontFamily = RobotoCondensed,
                    fontWeight = FontWeight.Bold,
                    fontSize = 21.sp,
                )

                EpisodesList()
            }

        }
    }
}


@Composable
fun EpisodesList(viewModel: EpisodesViewModel = hiltViewModel()) {

    val episodesList by remember{viewModel.episodeList}
    val endReached by remember{viewModel.endReached}
    val loadError by remember{viewModel.loadError}
    val isLoading by remember{viewModel.isLoading}

    LazyColumn(
        //contentPadding = PaddingValues(start = 16.dp, end = 16.dp, bottom = 16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp),
        modifier = Modifier.fillMaxWidth()
    ){

        val itemCount = episodesList.size
        items(itemCount){
            if (it>=itemCount-1 && !endReached && !isLoading){
                viewModel.episodesPaginated()
            }
            EpisodesEntry(episodesList[it])
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
                viewModel.episodesPaginated()
            }
        }
    }
}



@Composable
fun EpisodesEntry(
    entry: EpisodesListEntry,
) {


    Box(modifier = Modifier
        .shadow(5.dp, RoundedCornerShape(10.dp))
        .clip(RoundedCornerShape(10.dp))
        .fillMaxSize()
        .height(80.dp)
        .background(
            Brush.horizontalGradient(
                listOf(
                    backgroundName, MaterialTheme.colors.surface)))
        .clickable {

        }
    ){
        Row {

            Image(
                painter = painterResource(id = R.drawable.nave),
                contentDescription = "Image location",
                modifier = Modifier.size(100.dp),
                contentScale = ContentScale.Crop
            )

            Column(modifier = Modifier
                .padding(8.dp)
                .fillMaxSize(),
                Arrangement.SpaceEvenly) {

                Text(
                    text = entry.episodeName,
                    fontFamily = RobotoCondensed,
                    fontWeight = FontWeight.Bold,
                    fontSize = 16.sp
                )

                Text(
                    text = "${entry.episode} - ${entry.airDate}",
                    fontSize = 12.sp,
                )
            }
        }
    }
}