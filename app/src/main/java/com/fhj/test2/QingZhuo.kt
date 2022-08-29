package com.fhj.test2

import android.annotation.SuppressLint
import android.graphics.drawable.shapes.RectShape
import android.transition.Visibility
import androidx.activity.compose.BackHandler
import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.foundation.*
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.ClickableText
import androidx.compose.material.*
import androidx.compose.material.Icon
import androidx.compose.material.ripple.rememberRipple
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.BiasAlignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.blur
import androidx.compose.ui.draw.scale
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.*
import androidx.compose.ui.graphics.painter.BrushPainter
import androidx.compose.ui.graphics.painter.ColorPainter
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.graphics.vector.VectorPainter
import androidx.compose.ui.input.pointer.pointerHoverIcon
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.center
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Popup
import androidx.compose.ui.window.PopupProperties
import androidx.compose.ui.zIndex
import androidx.core.graphics.toColor
import androidx.navigation.*
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.test2.R
import com.fhj.test2.ui.AboutScreen
import com.fhj.test2.ui.MainScreen
import com.google.accompanist.navigation.animation.AnimatedNavHost
import com.google.accompanist.navigation.animation.composable
import com.google.accompanist.navigation.animation.rememberAnimatedNavController
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch


@Composable
fun Bar() {
    Row(modifier = Modifier.fillMaxWidth()) {
        Text(
            text = "标题",
            textAlign = TextAlign.Left,
            modifier = Modifier.align(BiasAlignment.Vertical(0f))
        )
    }

}

@SuppressLint("UnusedCrossfadeTargetStateParameter", "UnrememberedMutableState",
    "UnusedMaterialScaffoldPaddingParameter"
)
@OptIn(
    ExperimentalMaterial3Api::class, ExperimentalAnimationApi::class,
    ExperimentalMaterialApi::class
)
@Composable
fun QingZhuoDemo() {

    val navController = rememberAnimatedNavController()
    val scaffoldState = rememberScaffoldState()
    var currentPage = remember {
        mutableStateOf(NavDestinationTabs.Main.name)
    }
    val context = LocalContext.current
    var currentString = remember {
        mutableStateOf("hello")
    }

//    popupto-> https://developer.android.com/guide/navigation/navigation-navigate#back-stack

    var showPop = remember {
        mutableStateOf(false)
    }

    val rememberCoroutineScope = rememberCoroutineScope()
    val rememberModalBottomSheetState =
        rememberModalBottomSheetState(initialValue = ModalBottomSheetValue.Hidden)
    mutableStateOf("")

    var currentRoute =
        navController.currentBackStackEntryAsState().value?.destination?.route ?: ""
    Scaffold(bottomBar = {
        //底部导航栏
        BottomNavigation(modifier =Modifier.windowInsetsBottomHeight(
            WindowInsets.navigationBars.add(WindowInsets(bottom = 56.dp))
        )) {
            NavDestinationTabs.values().forEach {
                BottomNavigationItem(
                    selected = currentRoute == it.destinationRoute,
                    onClick = {
                        navController.navigate(it.destinationRoute) {
                            launchSingleTop = true
                            popUpTo("start")//这里start是一个navigation不是一个composable，即不是可视的,backquene里面会显示null,当按下回退按钮时，发现是null会退出软件。
                            {
                                saveState = true
                            }
                            restoreState = true
                        }
                    },
                    icon = {
                        Icon(painter = painterResource(id = it.icon), contentDescription = null)
                    },
                    label = { Text(text = it.title) },
                    alwaysShowLabel = false,
                    selectedContentColor = Color.Yellow,
                    unselectedContentColor = Color.White,
                    modifier = Modifier.navigationBarsPadding()
                )
            }
        }
    }) {
        AnimatedNavHost(
            navController = navController,
            startDestination = "start",
            enterTransition = {
                slideIntoContainer(
                    AnimatedContentScope.SlideDirection.Right,
                    animationSpec = tween(500, easing = LinearEasing)
                ) + fadeIn(animationSpec = tween(1000, easing = LinearEasing))
            },
            exitTransition = {
                slideOutOfContainer(
                    AnimatedContentScope.SlideDirection.Left,
                    animationSpec = tween(500, easing = LinearEasing)
                ) + fadeOut(tween(200, easing = LinearEasing))
            }, modifier = Modifier.fillMaxSize()
        ) {

            navigation(
                startDestination = NavDestinationTabs.Main.destinationRoute,
                route = "start"
            ) {
                //这里的composable 一定要用accompanist里的否则不显示内容
                composable(NavDestinationTabs.Main.destinationRoute) {
                    MainScreen(modifier = Modifier.fillMaxWidth())
                }
                composable(NavDestinationTabs.About.destinationRoute) {
                    AboutScreen(modifier = Modifier)
                }
            }
        }
    }

    /* Box {
        Column(modifier = Modifier.fillMaxSize()) {

            Button(onClick = {
                rememberCoroutineScope.launch {
                    rememberModalBottomSheetState.show()
                }
            }) {

            }
            //通过手势上滑拉出
        BottomDrawer(drawerContent = {
            Column {
                Text(text = "我是底部弹窗")
                Text(text = "我是底部弹窗")
                Text(text = "我是底部弹窗")
                Text(text = "我是底部弹窗")
                Text(text = "我是底部弹窗")
            }
        }) {

        }
*//*

        }

    }

    ModalBottomSheetLayout(sheetState = rememberModalBottomSheetState, sheetContent = {
        Column {
            Text(text = "我是底部弹窗")
            Text(text = "我是底部弹窗")
            Text(text = "我是底部弹窗")
            Text(text = "我是底部弹窗")
            Text(text = "我是底部弹窗")
        }
    }, scrimColor = Color(0f, 0f, 0f, 0f), modifier = Modifier.fillMaxSize()) {

    }*/

/*    AnimatedVisibility(visible = showPop.value, enter = slideInVertically(animationSpec = tween(1000,
        easing = LinearEasing), initialOffsetY = {it}), exit = slideOutVertically(animationSpec = tween(500,
        easing = LinearEasing), targetOffsetY = {it})
    ) {

        ModalBottomSheetLayout(sheetState = rememberModalBottomSheetState , sheetContent ={
            Column {
                Text(text = "我是底部弹窗")
                Text(text = "我是底部弹窗")
                Text(text = "我是底部弹窗")
                Text(text = "我是底部弹窗")
                Text(text = "我是底部弹窗")
            }
        }) {

        }
        Popup(alignment = Alignment.BottomCenter, onDismissRequest = {
            showPop.value = false
        }, properties = PopupProperties(focusable = true)) {

            Card(modifier = Modifier
                .fillMaxWidth()
                .background(Color.Red)) {
                Column(modifier = Modifier.wrapContentWidth()) {
                    Text(text ="我是弹出", modifier = Modifier.wrapContentWidth())
                    Text(text ="我是弹出", modifier = Modifier.wrapContentWidth())
                    Text(text ="我是弹出", modifier = Modifier.wrapContentWidth())
                    Text(text ="我是弹出", modifier = Modifier.wrapContentWidth())
                    Text(text ="我是弹出", modifier = Modifier.wrapContentWidth())
                }
            }
        }
*//*
    }*/

}

enum class NavDestinationTabs(val title: String, val destinationRoute: String, val icon: Int) {
    Main("首页", "main", R.drawable.mainicon),
    About("关于", "about", R.drawable.abouticon)
}

@OptIn(ExperimentalFoundationApi::class)
@SuppressLint("UnusedTransitionTargetStateParameter", "CoroutineCreationDuringComposition")
@Composable
fun qingZhuoBox(msg: String,modifier: Modifier,onClick:()->Unit = {},onLongClick:()->Unit={}) {
    val scaleTransitionTarget = remember {
        mutableStateOf(0.25f)
    }

    val transition = updateTransition(scaleTransitionTarget)
    val scaleTransition = transition.animateFloat(
        transitionSpec = { tween(1000, easing = LinearEasing) }
    ) {
        if (it.value == 1f) 0.25f else 1f
    }
    LaunchedEffect(key1 = Unit) {
        launch {
            while (true) {
                delay(1200)
                if (scaleTransitionTarget.value == 1f) {
                    scaleTransitionTarget.value = 0.25f
                } else {
                    scaleTransitionTarget.value = 1f
                }
            }
        }
    }

    Card(
        modifier = modifier
            .padding(top = 5.dp, start = 5.dp, end = 3.dp)
            .combinedClickable(
                enabled = true,
                onLongClick = onLongClick,
                onClick = onClick,
                indication = rememberRipple(true, color = Color(parseColor("#7CAC67"))),
                interactionSource = remember { MutableInteractionSource() }
            ),
        shape = RoundedCornerShape(15.dp),
        backgroundColor = Color(android.graphics.Color.parseColor("#E8E8E8"))
    ) {

        Row(modifier = Modifier
            .padding(13.dp)
            ) {
            Icon(painter = ColorPainter(Color.Red),
                contentDescription = null,
                tint = Color.Unspecified,
                modifier = Modifier
                    .padding(end = 10.dp)
                    .graphicsLayer {
                        clip = true
                        alpha = 0.5f
                        shape = CircleShape
                        scaleX = scaleTransition.value
                        scaleY = scaleTransition.value
                    })
            Text(text = msg, modifier = Modifier
                .padding(start = 10.dp)
                .align(Alignment.CenterVertically))
        }
    }
}
