package com.fhj.test2.ui

import android.content.Intent
import android.net.Uri
import android.provider.Settings
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.DialogProperties
import com.airbnb.lottie.compose.*
import com.example.test2.R
import com.fhj.test2.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.io.BufferedInputStream
import java.io.File
import java.io.FileInputStream
import java.io.RandomAccessFile

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun MainScreen(modifier: Modifier) {
    val context = LocalContext.current
    var lottieIsPlaying = remember {
        mutableStateOf(true)
    }
    var lottieSpeed = remember {
        mutableStateOf(1f)
    }
    var data = remember {
        mutableStateOf(0)
    }
    var textHint = remember {
        mutableStateOf("")
    }
    var isNeedFill by remember {
        mutableStateOf(true)
    }
    var progressCurrent = remember {
        mutableStateOf(0f)
    }
    var actionErrorDialogVisible by remember {
        mutableStateOf(false)
    }
    val rememberModalBottomSheetState =
        rememberModalBottomSheetState(initialValue = ModalBottomSheetValue.Hidden)
    var rememberLottieComposition =
        rememberLottieComposition(spec = LottieCompositionSpec.Asset("dlf10_8qDRX7nBln.lottie"))
    var animateLottieCompositionAsState =
        animateLottieCompositionAsState(
            composition = rememberLottieComposition.value,
            iterations = LottieConstants.IterateForever,
            isPlaying = lottieIsPlaying.value,
            speed = lottieSpeed.value,
            restartOnPlay = false
        )
    val coroutine = rememberCoroutineScope()
    Column(modifier = modifier) {
        Card(
            shape = RoundedCornerShape(bottomStart = 10.dp, bottomEnd = 10.dp),
            elevation = 8.dp,
            border = BorderStroke(2.dp, Color.Black)
        ) {
            Column {
                Row(horizontalArrangement = Arrangement.SpaceAround) {
                    qingZhuoBox("????????????", Modifier.weight(1f), onClick = {
                        reg.launch("*/*")
                    })
                    qingZhuoBox("??????Gif", Modifier.weight(1f), onClick = {
                        if (data.value == 2) {
                            coroutine.launch(Dispatchers.Default) {
                                actionErrorDialogVisible = true
                            }
                        } else {
                            if (loadSvgData != null) {
                                data.value = 2
                                textHint.value = "????????????,??????????????????????????????????????????????????????????????????????????????"
                                isNeedFill = true
                                    loadSvgData!!.mark((loadSvgData?.available() ?:0)+1)
                                    generatorGifCompose(data, textHint, progressCurrent, isNeedFill)
                            } else {
                                Toast.makeText(context, "???????????????", Toast.LENGTH_SHORT).show()
                            }
                        }
                    })
                }
                Text(text = textHint.value)
                Row(horizontalArrangement = Arrangement.SpaceAround) {
                    qingZhuoBox("???????????????Gif", Modifier.weight(1f), onClick = {
                        if (data.value == 2) {
                            coroutine.launch(Dispatchers.Default) {
                                actionErrorDialogVisible = true
                            }
                        } else {
                            if (loadSvgData != null) {
                                data.value = 2
                                textHint.value = "????????????,??????????????????????????????????????????????????????????????????????????????"
                                isNeedFill = false
                                loadSvgData!!.mark((loadSvgData?.available() ?:0)+1)
                                generatorGifCompose(data, textHint, progressCurrent, isNeedFill)
                            } else {
                                Toast.makeText(context, "?????????svg?????????xml??????", Toast.LENGTH_SHORT)
                                    .show()
                            }
                        }
                    })
                }
                LinearProgressIndicator(
                    progress = progressCurrent.value,
                    modifier = Modifier.fillMaxWidth(),
                    color = Color(parseColor("#FCBE23"))
                )
                if (data.value != 0) {
                    LottieAnimation(composition = rememberLottieComposition.value, progress = {
                        animateLottieCompositionAsState.value
                    }, modifier = Modifier.size(400.dp))
                }
            }
        }
        if(actionErrorDialogVisible)
        {
            AlertDialog(onDismissRequest = { actionErrorDialogVisible = false }, title = {Text(text ="????????????")}, text = { Text(text ="???????????????????????????")}, buttons = {})
        }
    }
}