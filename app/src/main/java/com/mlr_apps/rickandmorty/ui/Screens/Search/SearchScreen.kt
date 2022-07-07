package com.mlr_apps.rickandmorty.ui.Screens.Search

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.ClickableText
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.HighlightOff
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
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
fun SearchCharacter(navController: NavController,viewModel: SearchViewModel = hiltViewModel()) {


    var charactersList by remember{viewModel.characterList}
    val endReached by remember{viewModel.endReached}
    val loadError by remember{viewModel.loadError}
    val isLoading by remember{viewModel.isLoading}
    val isSearching by remember { viewModel.isSearching }


        RickAndMortyTheme {
        Surface(
            modifier = Modifier.fillMaxSize(),
            color = MaterialTheme.colors.background
        ) {
            Column {

                SearchCharacterBar() { nameCharacter ->
                    Log.d("NAMEC", "'$nameCharacter'")
                    if(nameCharacter  != "" ){

                        viewModel.searchCharacter(
                            query = nameCharacter,
                            status = "",
                            gender = ""
                        )
                    }else{
                        charactersList = emptyList()

                    }
                }



                Divider(color = MaterialTheme.colors.surface, thickness = 1.dp)

                Text(
                    text = "SEARCH RESULTS",
                    color = MaterialTheme.colors.onSurface,
                    fontSize = 12.sp,
                    modifier = Modifier.padding(16.dp)
                )

                LazyColumn(
                    verticalArrangement = Arrangement.spacedBy(16.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(start = 16.dp, end = 16.dp),
                    contentPadding = PaddingValues(bottom = 16.dp)
                ) {

                    val itemCount = charactersList.size
                    Log.d("Character", "$itemCount List: ${viewModel.characterList.value.size}")
                    items(itemCount) {

                        if (it >=itemCount - 1 && !isLoading && !endReached){
                            viewModel.addPage()
                        }

                        CharactersEntry(
                            navController = navController,
                            entry = charactersList[it]
                        )
                    }
                }

                Box(
                    contentAlignment = Alignment.TopCenter,
                    modifier = Modifier
                        .fillMaxSize()
                ) {
                    if(isSearching && loadError=="" && charactersList.isEmpty()){
                        CircularProgressIndicator(
                            color = MaterialTheme.colors.primary
                        )
                    }else if (loadError.isNotEmpty()){

                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Image(
                                painter = painterResource(id = R.drawable.xd),
                                contentDescription = "",
                                modifier = Modifier.size(height = 200.dp, width = 300.dp)
                            )
                            Text(
                                text = loadError,
                                color = MaterialTheme.colors.primary,
                                fontSize = 16.sp
                            )
                        }
                    }

                    if (!isSearching && loadError=="" && charactersList.isEmpty()){
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Image(
                                painter = painterResource(id = R.drawable.rickandmortyfamily),
                                contentDescription = "",
                                modifier = Modifier.size(height = 200.dp, width = 300.dp)
                            )
                            Text(
                                text = "Type your favorite character",
                                color = MaterialTheme.colors.primary,
                                fontSize = 16.sp
                            )
                        }
                    }
                }
            }
        }
    }
}



@OptIn(ExperimentalMaterialApi::class)
@Composable
fun SearchCharacterBar(
    hint: String = "",
    onSearch: (String) -> Unit = {}
){
    var text by remember {
        mutableStateOf("")
    }

    var isHintDisplayed by remember {
        mutableStateOf(hint == "")
    }


    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(start = 16.dp, top = 50.dp, end = 16.dp, bottom = 8.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {


        Box(
            Modifier
                .weight(1f)
                .padding(end = 16.dp)){
            BasicTextField(
                value = text,
                onValueChange = {
                    text = it
                    onSearch(it)
                },
                maxLines = 1,
                singleLine = true,
                textStyle = TextStyle(color = Color.Black),
                modifier = Modifier
                    .fillMaxWidth()
                    .shadow(4.dp, CircleShape)
                    .background(Color.White, CircleShape)
                    .padding(horizontal = 20.dp, vertical = 12.dp)
                    .onFocusChanged {
                        isHintDisplayed = !it.isFocused && text.isEmpty()
                    }
            )
            if (isHintDisplayed){
                Text(
                    text = "Search character",
                    color = Color.LightGray,
                    modifier = Modifier.padding(horizontal = 20.dp, vertical = 12.dp)
                )
            }
        }

        IconButton(
            onClick = {
                //hiddenFilter = !hiddenFilter
                onSearch("")
                text=""
            },
        ) {
            Icon(
                imageVector = Icons.Outlined.HighlightOff,
                contentDescription = "Filter bottom"
            )
        }
    }


}


@OptIn(ExperimentalCoilApi::class)
@Composable
fun CharactersEntry(
    navController: NavController,
    entry: RickAndMortyListEntry,
    viewModel: SearchViewModel = hiltViewModel()
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
                        modifier = Modifier.align(Alignment.Center)
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
                                .align(Alignment.CenterVertically)
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
fun FilterContent(){
    val languageOptions: List<String> = listOf("alive", "dead", "unknown")
    val ideOptions: List<String> = listOf("female", "male", "genderless", "unknown")

    Column{
        val selectedStatus = radioGroup(
            radioOptions = languageOptions,
            title = "Filter by status",
            cardBackgroundColor = Color(0xFFFFFAF0)
        )

        val selectedGender = radioGroup(
            radioOptions = ideOptions,
            title = "Filter by gender",
            cardBackgroundColor = Color(0xFFF8F8FF)
        )


        Text(
            text = "Selected : $selectedStatus & $selectedGender",
            fontSize = 22.sp,
            fontStyle = FontStyle.Normal,
            fontWeight = FontWeight.Normal,
            modifier = Modifier
                .padding(bottom = 15.dp)
                .fillMaxWidth(),
            textAlign = TextAlign.Center,
            color = Color(0xFF665D1E)
        )
    }
}













@Composable
fun radioGroup(
    radioOptions: List<String> = listOf(),
    title: String = "",
    cardBackgroundColor: Color = Color(0xFFFEFEFA)
):String{
    if (radioOptions.isNotEmpty()){
        val (selectedOption, onOptionSelected) = remember {
            //mutableStateOf(radioOptions[0])
            mutableStateOf(radioOptions[0])
        }

        Card(
            backgroundColor = cardBackgroundColor,
            modifier = Modifier
                .padding(10.dp)
                .fillMaxWidth(),
            elevation = 8.dp,
            shape = RoundedCornerShape(10.dp),
        ) {
            Column(
                Modifier.padding(10.dp)
            ) {
                Text(
                    text = title,
                    fontStyle = FontStyle.Normal,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(5.dp),
                )

                radioOptions.forEach { item ->
                    Row(
                        Modifier.padding(5.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        RadioButton(
                            selected = (item == selectedOption),
                            onClick = { onOptionSelected(item) }
                        )

                        val annotatedString = buildAnnotatedString {
                            withStyle(
                                style = SpanStyle(fontWeight = FontWeight.Bold)
                            ){ append("  $item  ") }
                        }

                        ClickableText(
                            text = annotatedString,
                            onClick = {
                                onOptionSelected(item)
                            }
                        )
                    }
                }
            }
        }
        return selectedOption
    }else{
        return ""
    }
}



