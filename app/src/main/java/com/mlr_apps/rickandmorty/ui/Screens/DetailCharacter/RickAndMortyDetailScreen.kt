package com.mlr_apps.rickandmorty.ui.Screens.DetailCharacter

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.ArrowBackIos
import androidx.compose.material.icons.outlined.ZoomOutMap
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.produceState
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Alignment.Companion.Center
import androidx.compose.ui.Alignment.Companion.CenterHorizontally
import androidx.compose.ui.Alignment.Companion.TopCenter
import androidx.compose.ui.Alignment.Companion.TopEnd
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import coil.compose.rememberImagePainter
import com.mlr_apps.rickandmorty.Data.API.Response.CharacterInfo.CharacterInfo
import com.mlr_apps.rickandmorty.Utils.Resource
import com.mlr_apps.rickandmorty.ui.Screens.CustomDialog
import com.mlr_apps.rickandmorty.ui.theme.RickAndMortyTheme
import com.mlr_apps.rickandmorty.ui.theme.RobotoCondensed

@Composable
fun CharacterInfoScreen(
    dominantColor: Color,
    characterId: Int,
    navController: NavController,
    viewModel: RickAndMortyDetailViewModel = hiltViewModel()
) {

    val characterInfo = produceState<Resource<CharacterInfo>>(
        initialValue = Resource.Loading()){
        value = viewModel.getCharacterInfo(characterId = characterId)
    }.value


    RickAndMortyTheme {
        Surface(
            modifier = Modifier.fillMaxSize(),
            color = MaterialTheme.colors.background
        ) {
            if (characterInfo is Resource.Success){
                LazyColumn(Modifier.fillMaxSize()) {
                    item {
                        LoadDetailTopSection(
                            dominantColor = dominantColor,
                            navController = navController,
                            characterInfo = characterInfo
                        )
                    }
                    item {
                        LoadInfoCharacter(characterInfo)
                    }
                    item{
                        LoadEpisodes(
                            characterInfo = characterInfo,
                            modifier = Modifier.padding(16.dp)
                        )
                    }
                }
            }


            if (characterInfo is Resource.Loading){
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .fillMaxHeight(),
                    contentAlignment = Alignment.Center
                ){
                    CircularProgressIndicator(color = MaterialTheme.colors.primary)
                }
            }

            if (characterInfo is Resource.Error){
                RetrySection(error = characterInfo.message!!){
                    navController.popBackStack()
                }
            }
        }

    }

}


@Composable
fun LoadDetailTopSection(
    dominantColor: Color,
    navController: NavController,
    characterInfo: Resource.Success<CharacterInfo>) {

    characterInfo.data!!

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(180.dp)
            .background(
                Brush.verticalGradient(
                    listOf(
                        MaterialTheme.colors.surface, dominantColor
                    )
                )
            )
    ){
        IconButton(
            onClick = { navController.popBackStack() },
            modifier = Modifier
                .size(24.dp)
                .offset(16.dp, 50.dp)) {
            Icon(
                imageVector = Icons.Outlined.ArrowBackIos,
                contentDescription = "Icon back screen",
                tint = MaterialTheme.colors.onSurface)
        }


        Image(
            painter = rememberImagePainter(
                data = characterInfo.data.image,
                builder = {
                    crossfade(true)
                }),
            contentScale = ContentScale.Crop,
            contentDescription = characterInfo.data.name,
            modifier = Modifier
                .size(125.dp)
                .offset(y = 90.dp)
                .clip(CircleShape)
                .border(4.dp, MaterialTheme.colors.background, CircleShape)
                .shadow(elevation = 5.dp)
                .background(MaterialTheme.colors.surface)
                .align(Center),

        )


        Text(
            text = "#${characterInfo.data.id}",
            fontSize = 21.sp,
            modifier = Modifier
                .align(TopCenter)
                .offset(y = 50.dp)
        )

        val dialog = remember { mutableStateOf(false) }

        IconButton(
            onClick = { dialog.value = true},
            modifier = Modifier
                .align(TopEnd)
                .size(24.dp)
                .offset(x = (-16).dp, y = 50.dp)
        ){
            Icon(
                imageVector = Icons.Outlined.ZoomOutMap,
                contentDescription = "Icon back screen",
                tint = MaterialTheme.colors.onSurface,
            )
        }

        if (dialog.value){
            CustomDialog(
                image = characterInfo.data.image,
                openDialogCustom = dialog
            )

        }

    }

}


@Composable
fun LoadInfoCharacter(characterInfo: Resource.Success<CharacterInfo>) {
    Column(
        Modifier
            .fillMaxWidth()
            .padding(top = 70.dp, bottom = 24.dp)) {

        characterInfo.data!!
        Column(
            modifier = Modifier.fillMaxWidth(),
            horizontalAlignment = CenterHorizontally
        ){

            Text(
                text = characterInfo.data.name,
                fontFamily = RobotoCondensed,
                fontWeight = FontWeight.Bold,
                fontSize = 21.sp,
            )

            Spacer(modifier = Modifier.height(8.dp))

            Row {
                Box(
                    modifier = Modifier
                        .size(12.dp)
                        .clip(CircleShape)
                        .background(
                            when (characterInfo.data.status) {
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
                        .align(Alignment.CenterVertically)
                )

                Spacer(modifier = Modifier.width(4.dp))
                Text(
                    text = characterInfo.data.status,
                    fontSize = 14.sp,
                )

            }

            Row(
                horizontalArrangement = Arrangement.SpaceEvenly,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 16.dp)
            ) {
                Column(horizontalAlignment = CenterHorizontally) {
                    Text(
                        text = characterInfo.data.species,
                        fontWeight = FontWeight.Bold,
                        fontSize = 16.sp,
                    )

                    Text(
                        text = "Species",
                        fontSize = 12.sp,
                    )
                }

                Column(horizontalAlignment = CenterHorizontally) {
                    Text(
                        text = characterInfo.data.gender,
                        fontWeight = FontWeight.Bold,
                        fontSize = 16.sp)

                    Text(
                        text = "Gender",
                        fontSize = 12.sp)
                }
            }

        }

        //
        Column(Modifier.padding(start = 16.dp, end = 16.dp, top = 24.dp)) {

            Text(
                text = characterInfo.data.origin.name,
                fontWeight = FontWeight.Bold,
                fontSize = 14.sp,
            )

            Text(
                text = "Origin",
                fontSize = 12.sp,
            )
        }

        Column(Modifier.padding(start = 16.dp, end = 16.dp, top = 16.dp)) {

            Text(
                text = characterInfo.data.location.name,
                fontWeight = FontWeight.Bold,
                fontSize = 14.sp,
            )

            Text(
                text = "Location",
                fontSize = 12.sp,
            )
        }

        //LoadEpisodes(characterInfo.data)

    }

}

@Composable
fun LoadEpisodes(
    modifier: Modifier = Modifier,
    characterInfo: Resource.Success<CharacterInfo>,
    viewModel: RickAndMortyDetailViewModel = hiltViewModel()
) {
    characterInfo.data!!



    Divider(color = MaterialTheme.colors.surface, thickness = 1.dp)

    Column(modifier = modifier) {
        Text(
            text = "Episodes Apparent in (${characterInfo.data.episode.size})",
            fontWeight = FontWeight.Bold,
            fontSize = 14.sp)
    }


}





@Composable
fun RetrySection(
    error: String,
    onRetry: () -> Unit
){
    Box(
        modifier = Modifier
            .fillMaxSize(),
        contentAlignment = Alignment.Center
    ){
        Column(horizontalAlignment = Alignment.CenterHorizontally)
        {
            Text(text = error, color = Color.Red, fontSize = 18.sp)
            Spacer(modifier = Modifier.height(50.dp))
            Button(
                onClick = { onRetry() },
                modifier = Modifier
                    .align(Alignment.CenterHorizontally),
                shape = RoundedCornerShape(50.dp)
            ) {
                Text(text = "Go back")
            }
        }
    }

}

