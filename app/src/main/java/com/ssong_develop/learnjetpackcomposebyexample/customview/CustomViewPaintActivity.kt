package com.ssong_develop.learnjetpackcomposebyexample.customview


import android.os.Bundle
import android.view.MotionEvent
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.input.pointer.pointerInteropFilter


class CustomViewPaintActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            CustomDrawableViewComponent()
        }
    }
}

@Composable
fun CustomDrawableViewComponent() {
    DrawingBoardComposable()
}

data class Paths(
    val x: Float,
    val y: Float
)

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun DrawingBoardComposable() {
    // Reacting to state changes is the core behavior of Compose. You will notice a couple new
    // keywords that are compose related - remember & mutableStateOf.remember() is a helper
    // composable that calculates the value passed to it only during the first composition. It then
    // returns the same value for every subsequent composition. Next, youy can think of
    // mutableStateOf as an observable value where updates to this variable will redraw all
    // the composable functions that access it. We don't need to explicitly subscribe at all. Any
    // compoable that reads its value will be recomposed any time the value
    // changes. This ensures that only the composables that depend on this will be redraw while the
    // res remain unchanged. This ensures efficiency and is a performance optimization. It
    // is inspired from existing frameworks like React.
    val paths = remember { mutableStateListOf<Paths>() }
    // Column is a composable that places its children in a vertical sequence. You
    // can think of it similar to a LinearLayout with the vertical orientation. In addition, we
    // also provide the column with a modifier

    Column(
        modifier = Modifier
            .fillMaxSize()
                // pointerInteropFilter gives you access to MotionEvent's that are received by this
                // composable
            .pointerInteropFilter {
                when (it.actionMasked) {
                    MotionEvent.ACTION_DOWN -> {
                        paths += Paths(it.x,it.y)
                        true
                    }
                    else -> false
                }
            }
    ) {
        Canvas(modifier = Modifier) {
            val p = Path()
            for (path in paths) {
                p.lineTo(path.x,path.y)
                p.moveTo(path.x,path.y)
            }
            drawPath(p, color = Color.Black, style = Stroke(width = 3f, join = StrokeJoin.Round))
        }
    }
}