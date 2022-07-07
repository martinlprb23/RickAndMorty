package com.mlr_apps.rickandmorty.ui.Screens.Characters

import android.util.Log
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.ImageSearch
import androidx.compose.material.icons.outlined.Search
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Alignment.Companion.BottomCenter
import androidx.compose.ui.Alignment.Companion.Center
import androidx.compose.ui.Alignment.Companion.CenterEnd
import androidx.compose.ui.Alignment.Companion.CenterStart
import androidx.compose.ui.Alignment.Companion.CenterVertically
import androidx.compose.ui.Alignment.Companion.End
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.layout.VerticalAlignmentLine
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import coil.annotation.ExperimentalCoilApi
import coil.compose.ImagePainter
import coil.compose.rememberImagePainter
import com.mlr_apps.rickandmorty.Data.Model.RickAndMortyListEntry
import com.mlr_apps.rickandmorty.R
import com.mlr_apps.rickandmorty.ui.theme.RickAndMortyTheme
import com.mlr_apps.rickandmorty.ui.theme.RobotoCondensed
import kotlinx.coroutines.launch


@Composable
fun MainCharacters(
    navController: NavController) {
    RickAndMortyTheme {
        Surface(
            modifier = Modifier.fillMaxSize(),
            color = MaterialTheme.colors.background
        ) {
            Column() {
                TopSectionRickAndMorty(navController = navController)

                RickAndMortyList(navController = navController)
               
            }
        }
    }
}

@Composable
fun TopSectionRickAndMorty(navController: NavController) {

    Column {
        Row(
            modifier = Modifier
                .padding(start = 16.dp, top = 50.dp, end = 16.dp, bottom = 24.dp)
                .fillMaxWidth(),
            Arrangement.SpaceBetween
        ) {

            Text(
                text = "Characters",
                fontFamily = RobotoCondensed,
                fontWeight = FontWeight.Bold,
                fontSize = 21.sp,
            )

            IconButton(
                onClick = {
                    navController.navigate(
                        route = "search_character"
                    )
                },
                modifier = Modifier.size(24.dp)
            ){
                Icon(
                    imageVector = Icons.Outlined.Search,
                    contentDescription = "Icon back screen",
                    tint = MaterialTheme.colors.onSurface,
                )
            }
        }

        /*Box(
            modifier = Modifier
                .padding(top = 16.dp, start = 16.dp, end = 16.dp, bottom = 24.dp)
                .fillMaxWidth(),
            contentAlignment = Center
        ) {

            Image(
                painter = painterResource(id = R.drawable.rickandmortylogo),
                contentDescription = "Rick and Morty Logo", alignment = Center,
                modifier = Modifier.size(height = 100.dp, width = 200.dp))
        }*/
    }
}





@Composable
fun RickAndMortyList(
    navController: NavController,
    viewModel: RickAndMortyListViewModel = hiltViewModel()
) {

    val charactersList by remember{viewModel.rickAndMortyList}
    val endReached by remember{viewModel.endReached}
    val loadError by remember{viewModel.loadError}
    val isLoading by remember{viewModel.isLoading}


    LazyColumn(
        //contentPadding = PaddingValues(start = 16.dp, end = 16.dp, bottom = 16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp),
        modifier = Modifier.fillMaxWidth().padding(start = 16.dp, end = 16.dp),
        contentPadding = PaddingValues(bottom = 16.dp)
    ){


        val itemCount = charactersList.size
        Log.d("Items","$itemCount List: ${charactersList.size}")
        items(itemCount){
            if (it >=itemCount - 1 && !endReached && !isLoading){
                //Log.d("CountSize:", itemCount.toString())
                viewModel.loadRickAndMortyPaginated()
            }
            CharactersEntry(navController = navController,entry = charactersList[it])
        }
    }

    Box(
        contentAlignment = Center,
        modifier = Modifier.fillMaxSize()
    ) {
        if (isLoading){
            CircularProgressIndicator(color = MaterialTheme.colors.primary)
        }

        if (loadError.isNotEmpty()){
            RetrySection(error = loadError) {
                viewModel.loadRickAndMortyPaginated()
            }
        }

    }

}


@OptIn(ExperimentalCoilApi::class)
@Composable
fun CharactersEntry(
    navController: NavController,
    entry: RickAndMortyListEntry,
    viewModel: RickAndMortyListViewModel = hiltViewModel()
) {

    val defaultDominantColor = MaterialTheme.colors.surface

    var dominantColor by remember{
        mutableStateOf(defaultDominantColor)
    }

    val painter = rememberImagePainter(data = entry.imageUrl)
    val painterState = painter.state

    Box(
        modifier = Modifier
            .shadow(5.dp, RoundedCornerShape(10.dp))
            .clip(RoundedCornerShape(10.dp))
            .fillMaxSize()
            .height(125.dp)
            //.aspectRatio(1f)
            .background(
                Brush.horizontalGradient(
                    listOf(
                        dominantColor, defaultDominantColor
                    )
                )
            )
            .clickable {

                navController.navigate(
                    route = "Rick_And_Morty_detail_screen/${dominantColor.toArgb()}/${entry.number}"
                )
            }
    ) {
        Row {
            Box {
                Image(
                    painter = painter,
                    contentDescription = entry.characterName,
                    modifier = Modifier
                        .size(125.dp))

                if (painterState is ImagePainter.State.Loading) {
                    CircularProgressIndicator(
                        color = MaterialTheme.colors.primary,
                        modifier = Modifier.align(Center)
                    )

                }

                else if (painterState is ImagePainter.State.Success) {
                    LaunchedEffect(key1 = painter) {
                        launch {
                            val image = painter.imageLoader.execute(painter.request).drawable
                            viewModel.calcDominantColor(image!!) {
                                dominantColor = it
                            }
                        }
                    }
                }
            }


            Column(modifier = Modifier
                .padding(8.dp)
                .fillMaxSize(),
            Arrangement.SpaceEvenly) {


                Text(
                    text = entry.characterName,
                    fontFamily = RobotoCondensed,
                    fontWeight = FontWeight.Bold,
                    fontSize = 16.sp
                )


                Column {
                    Text(text = "Origin",fontSize=(10.sp))
                    Text(
                        text = entry.origin,
                        fontSize = 12.sp,
                    )
                }

                Column {
                    Text(text = "Status",fontSize=(10.sp))
                    Row {
                        Box(
                            modifier = Modifier
                                .size(10.dp)
                                .clip(CircleShape)
                                .background(
                                    when (entry.status) {
                                        "Dead" -> {
                                            Color.Red
                                        }
                                        "unknown" -> {
                                            Color.Blue
                                        }
                                        else -> {
                                            Color.Green
                                        }
                                    }
                                )
                                .align(CenterVertically)
                        )

                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            text = "${entry.status} - ${entry.gender}",
                            fontSize = 12.sp,
                        )

                    }
                }

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
        contentAlignment = BottomCenter){
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




