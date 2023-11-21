package com.example.hltv.ui.screens.singleMatch

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.hltv.ui.common.CommonCard
import com.example.hltv.ui.common.CommonComposable
import com.example.hltv.ui.screens.teamsScreen.RankingScreen
import com.example.hltv.ui.screens.teamsScreen.teamCard

@Composable
fun SingleMatchScreen(){
    val viewModel = SingleMatchViewModel()

    val singlematchimage by viewModel.matches.collectAsState()
    val matchHistory = viewModel.matchResult

    LazyColumn{
            items(matchHistory.size){
                index ->
                CommonCard(
                modifier = Modifier,
                headText = matchHistory[index], 
                    subText = "nothing so far",
                    image = null,
                    bottomBox = {
                        Box{
                            Text(text = "tommy")
                            
                        }
                    }
                    
                )


            }



    }




//teamCard(modifier = Modifier, R = R, text1 = singlematches.toString(), text2 = "test")
}

@Preview(showBackground = true)
@Composable
fun SingleMatchScreenPreview() {
    SingleMatchScreen()

}