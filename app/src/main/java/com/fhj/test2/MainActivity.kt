package com.fhj.test2

import android.R
import android.annotation.SuppressLint
import android.content.res.Resources
import android.graphics.*
import android.graphics.Canvas
import android.graphics.LinearGradient
import android.graphics.Paint
import android.graphics.RadialGradient
import android.graphics.Shader
import android.os.Bundle
import android.os.Environment
import android.util.TypedValue
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.*
import androidx.compose.ui.platform.LocalContext
import androidx.core.graphics.PathParser
import androidx.core.graphics.PathParser.createPathFromPathData
import androidx.core.net.toFile
import com.fhj.test2.theme.PinkTheme
import com.github.megatronking.svg.generator.svg.model.Polygon
import com.github.megatronking.svg.generator.svg.model.Polyline
import com.github.megatronking.svg.generator.svg.utils.StyleUtils
import kotlinx.coroutines.*
import org.xmlpull.v1.XmlPullParser
import org.xmlpull.v1.XmlPullParserFactory
import java.io.*
import javax.xml.parsers.DocumentBuilderFactory
import kotlin.coroutines.CoroutineContext
import kotlin.coroutines.coroutineContext

var loadSvgData: BufferedInputStream? = null
var fileName: String = ""
var fileInputStream: FileInputStream? = null
lateinit var reg: ActivityResultLauncher<String>

class MainActivity : ComponentActivity() {

    @SuppressLint("Recycle", "SoonBlockedPrivateApi")
    override fun onCreate(savedInstanceState: Bundle?) {
        reg = registerForActivityResult(ActivityResultContracts.GetContent()) {
//            MediaStore.Images.Media.DATA
            try {
                loadSvgData = BufferedInputStream(this.contentResolver.openInputStream(it!!))
                if(loadSvgData==null)
                {
                    val str =
                        this.contentResolver.openInputStream(it!!)?.readBytes()?.decodeToString() ?: ""
                    loadSvgData = if (str == "") null else {
                        BufferedInputStream(str.byteInputStream())
                    }
                }
                val file = it.toFile()
                fileInputStream = file.inputStream()
/*                if (fileName == "") {
                    fileName = file.name
                    Log.v("例子", file.path)
                    val cursor = this.contentResolver.query(
                        it!!,
                        arrayOf(MediaStore.Images.Media.DISPLAY_NAME),
                        null,
                        null,
                        null
                    )
                    println(cursor)
                    cursor?.let { cur ->
                        cur.moveToFirst()
                        do {
                            cur.columnNames.forEach {
                                val index = cur.getColumnIndexOrThrow(it)
                                fileName = cur.getString(index)
//                        Log.v("例子", fileName)
                                println(fileName)
                            }
//                println(it.getString(it.getColumnIndexOrThrow(null)))
                        } while (cur.moveToNext())
                    }
                }*/
            } catch (
                e: Exception
            ) {
                println(e)
//                Toast.makeText(this, "请传入正确SVG图", Toast.LENGTH_SHORT).show()
            }

        }
        super.onCreate(savedInstanceState)
        requestPermissions(
            arrayOf(
                android.Manifest.permission.READ_EXTERNAL_STORAGE,
                android.Manifest.permission.WRITE_EXTERNAL_STORAGE
            ), 1
        )
        setContent {
/*            Test2Theme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
//                    Log.v("例子",LocalContext.current.applicationContext.filesDir.absolutePath)
//                    DefaultPreview()

//                    var loadSvgDataRem = remember(){
//                        mutableStateOf(loadSvgData)
//                    }
//                    getImage(reg)
                    QingZhuoDemo()
                }
            }*/
            PinkTheme {
                QingZhuoDemo()
            }
        }

    }
}


fun requestPermission() {

}

@SuppressLint("CoroutineCreationDuringComposition")
@Composable
fun DefaultPreview(state: Int, progress: MutableState<String>) {
    if (loadSvgData == null || state != 2) return
//    testXml(loadSvgData!!)
//    val path = ContextWrapper(LocalContext.current).getExternalFilesDir(Environment.DIRECTORY_PICTURES)?.absolutePath?:LocalContext.current.applicationInfo.publicSourceDir
    val path =
        Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES).absolutePath
//    val path = LocalContext.current.applicationInfo.dataDir
//    Log.v("例子",path)
    val context = LocalContext.current
//    val arr = readXml(loadSvgData!!)
    val arr = readXmlFromXml(loadSvgData!!)
    loadSvgData?.close()
    loadSvgData = null
//    println("开始结算！！")

//    val hashMap = arr[0] as ArrayMap<String, String>

    val keys = arr[0] as ArrayList<String>
    val values = arr[1] as ArrayList<Paint>
    println(keys.size)

//    val width = (arr[3] as String).run { if (contains("px")) substring(0, indexOf("px")) else this }.toInt()

    val height = arr[2] as Int
    val width = arr[3] as Int
    if (height == 0) {
        Toast.makeText(context, "暂不支持", Toast.LENGTH_SHORT).show()
    }
    val rememberCoroutineScope = rememberCoroutineScope()

/*    androidx.compose.foundation.Canvas(modifier = Modifier
        .size(height.dp, width.dp)
        .background(color = Color.Red)
    ) {
        this.drawContext.canvas.scale(0.1f)
//        this.drawContext.canvas.nativeCanvas.bi
        for (i in 0 until keys.size) {
        val methods = keys.elementAt(i)
        val color = values.elementAt(i)
        println(methods+color)
        val path = PathParser().parsePathString(methods).toPath()
//            , color = Color(android.graphics.Color.parseColor(color))
//        drawLine(color = Color.Gray, Offset(0f,0f), Offset(200f,60f))
//        drawPath(path, color = Color(android.graphics.Color.parseColor(color)))
        }
    }*/
//
    val baos = ByteArrayOutputStream()
    val localAnimatedGifEncoder = AnimatedGifEncoder()
    localAnimatedGifEncoder.start(baos) //start
    localAnimatedGifEncoder.setRepeat(3) //设置生成gif的开始播放时间。0为立即开始播放
    localAnimatedGifEncoder.setDelay(200)
    localAnimatedGifEncoder.setQuality(1)
//

    val bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888)
    val canvas = android.graphics.Canvas(bitmap)


    val rememberCoroutineScope1 = androidx.compose.runtime.rememberCoroutineScope()
    rememberCoroutineScope1.launch {
        canvas.run {
            for (i in 0 until keys.size) {
                val methods = keys.elementAt(i)
                val color = values.elementAt(i)
//                Log.v("例子",methods + color)
//            val path = PathParser().parsePathString(methods).toPath()

//            Log.v("例子",path.isEmpty.toString())
//            , color = Color(android.graphics.Color.parseColor(color))
//            drawLine(color = Color.Gray, Offset(1563.518f,1775.975f), Offset(2000f,60f))
//            drawPath(path, color = Color.Gray)

/*                val paint = android.graphics.Paint().apply {
                    this.color = Color(android.graphics.Color.parseColor(color)).toArgb()
                    strokeJoin = Paint.Join.ROUND
                }*/
                val paint = color

                drawPath(androidx.core.graphics.PathParser.createPathFromPathData(methods), paint)
//                canvas.save()
//                bitmap.compress(Bitmap.CompressFormat.JPEG,100, FileOutputStream(File(Environment.DIRECTORY_DCIM+"/svg2.svg")))

                localAnimatedGifEncoder.addFrame(bitmap)
            }
        }
        //
        localAnimatedGifEncoder.finish() //finish
        val gifNamePath = path + "/" + System.currentTimeMillis() + ".gif"
        val file = File(gifNamePath)
        val fos = FileOutputStream(file)
        baos.writeTo(fos)
        baos.flush()
        fos.flush()
        baos.close()
        fos.close()
        progress.value = "加载完成,保存在了" + gifNamePath

        //
//    canvas.save()
//    canvas.restore()
//    bitmap.compress(Bitmap.CompressFormat.JPEG,100, FileOutputStream(File(LocalContext.current.applicationContext.filesDir.absolutePath+"/svg2.svg")))
    }

}

fun generatorGifCompose(
    state: MutableState<Int>,
    progress: MutableState<String>,
    progressCurrent: MutableState<Float>,
    isNeedFill: Boolean
) {
    if (loadSvgData == null || state.value != 2) return
    progress.value = "正在合成......"
    val job = Job()
    val coroutineScope = CoroutineScope(job)
    coroutineScope.launch(context = Dispatchers.Default) {
        println("generatorGifCompose重组")
        val bitmapList = readFromXml2(loadSvgData!!, isNeedFill)
        canvasSaveToBitMap(bitmapList, progress,progressCurrent)
        state.value = 0
        loadSvgData?.reset()
    }
}

fun readXml(path: InputStream): Array<Serializable> {

    val hashMap = HashMap<String, String>()
    val arrKeys = ArrayList<String>()
    val arrValues = ArrayList<String>()

    val documentBuilder = DocumentBuilderFactory.newInstance().newDocumentBuilder()
    val parse = documentBuilder.parse(path)
    val documentElement = parse.documentElement
    var height = documentElement.getAttribute("height")
    var width = documentElement.getAttribute("width")
    val style =
        documentElement.getElementsByTagName("style").item(0).childNodes.item(0).nodeValue.run {
            replace("\n", "").replace("\t", "")
        }
    try {
        if (height == "") {
            documentElement.getAttribute("viewBox").run {
                val sp = split(" ")
                height = sp[2]
                width = sp[3]
            }
            var item = style.run {
                val list = split(".")
                list.forEach {
                    if (it != "") {
                        hashMap.put(
                            it.substring(0, it.indexOf("{")),
                            it.substring(it.indexOf("#"), it.indexOf(";"))
                        )
                    }
                }
            }
        }
    } catch (e: Exception) {

    }

    val arr = arrayOf(arrKeys, arrValues, height, width)
    val elementsByTagNameNS = documentElement.getElementsByTagName("path")
//    val item = elementsByTagNameNS.item(1).childNodes
    val item = elementsByTagNameNS
    item.apply {
        for (i in 0 until length) {
            item(i).run {
                if (nodeName == "path") {
                    attributes.run {
                        for (j in 0..length) {
                            if (item(j).nodeName == "d") {
                                val key = StringBuffer()
                                item(j).nodeValue.toCharArray().filter {
                                    it != '\t' && it != '\n'
                                }.forEach {
                                    key.append(it)
                                }
                                arrKeys.add(key.toString())
                            } else {
                                val color =
                                    if (item(j).nodeName == "class") hashMap.get(item(1).nodeValue) else if (item(
                                            j
                                        ).nodeName == "color"
                                    ) item(j).nodeValue else ""
                                arrValues.add(
                                    color ?: ""
                                )
                            }
                        }
                    }

                }

            }
        }
    }
    return arr
}

fun readXmlFromXml(path: InputStream): Array<Serializable> {

    val hashMap = HashMap<String, String>()
    val arrKeys = ArrayList<String>()
    val arrValues = ArrayList<Paint>()

    val documentBuilder = DocumentBuilderFactory.newInstance().newDocumentBuilder()
    val parse = documentBuilder.parse(path)
    val documentElement = parse.documentElement
    var height = documentElement.getAttribute("android:viewportWidth").toInt()
    var width = documentElement.getAttribute("android:viewportHeight").toInt()


    val arr = arrayOf(arrKeys, arrValues, height, width)
    val elementsByTagNameNS = documentElement.getElementsByTagName("path")

    val item = elementsByTagNameNS
    item.apply {
        for (i in 0 until length) {
            item(i).run {
                if (nodeName == "path") {
                    val paint = Paint()
                    attributes.run {
                        for (j in 0 until length) {
                            when (item(j).nodeName) {
                                "android:pathData" -> {
                                    arrKeys.add(
                                        item(j).nodeValue.replace("\n", "").replace("\t", "")
                                    )
                                }
                                "android:strokeLineJoin" -> {
                                    paint.strokeJoin = when (item(j).nodeValue) {
                                        "round" -> {
                                            Paint.Join.ROUND
                                        }
                                        "miter" -> {
                                            Paint.Join.MITER
                                        }
                                        "bevel" -> {
                                            Paint.Join.BEVEL
                                        }
                                        else -> null
                                    }
                                }
                                "android:strokeWidth" -> {
                                    paint.strokeWidth = item(j).nodeValue.toFloat()
                                }
                                "android:fillColor" -> {
                                    try {
                                        paint.color =
                                            android.graphics.Color.parseColor(item(j).nodeValue)
                                    } catch (e: Exception) {

                                    }
                                }
                                "android:strokeColor" -> {

                                }
                                "android:strokeLineCap" -> {
                                    paint.strokeCap = when (item(j).nodeValue) {
                                        "round" -> Paint.Cap.ROUND
                                        "square" -> Paint.Cap.SQUARE
                                        "butt" -> Paint.Cap.BUTT
                                        else -> null
                                    }
                                }
                                "android:strokeMiterLimit" -> {
                                    paint.strokeMiter = item(j).nodeValue.toFloat()
                                }
                            }
                        }
                    }
                    arrValues.add(paint)
                }
            }
        }
    }
    return arr
}

fun readFromXml2(
    input: InputStream,
    isNeedFill: Boolean
): ArrayList<Bitmap> {

    //保存每步的bitmap
    val bitmapList = ArrayList<Bitmap>()

    val decodeToString = input.readBytes().decodeToString()
    val parsePaintString = String(decodeToString.toCharArray())
    //解析渐变色
    val documentBuilder = DocumentBuilderFactory.newInstance().newDocumentBuilder()
    val parse = documentBuilder.parse(decodeToString.byteInputStream())
    var width = 0
    var height = 0
    if (parse.documentElement.getAttribute("viewBox") != "") {
        val split = parse.documentElement.getAttribute("viewBox").split(" ")
        width = split[2].toFloat().toInt()
        height = split[3].toFloat().toInt()
    } else {
        width = parse.documentElement.getAttribute("width").toFloat().toInt()
        height = parse.documentElement.getAttribute("height").toFloat().toInt()
    }
    val bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888)

    val canvas = Canvas(bitmap)
    val gradientHasMap = HashMap<String, Shader>()
    val paintStyleMap = HashMap<String, Paint>()
        //解析全局style
    val styleG = parse.documentElement.getElementsByTagName("style")
    styleG.run {
            for (i in 0 until length) {
                var styleMap = HashMap<String, HashMap<String, String>>()
                var map =
                    HashMap<String, HashMap<String, String>>() as Map<String, MutableMap<String, String>>?
                StyleUtils.fill2Map(this.item(i).textContent, map)
                for ((key, value) in map!!) {
                    val split = key.split(",")
                    for (className in split) {
                        if (styleMap.contains(className)) {
                            styleMap.get(key)?.putAll(value)
                        } else {
                            val hash = HashMap<String, String>()
                            styleMap.put(className, hash)
                            hash.putAll(value)
                        }
                    }

                }
                for ((key, value) in styleMap) {
                    paintStyleMap.put(key, Paint().apply {
                        color = android.graphics.Color.BLACK
//                        this.style = Paint.Style.FILL_AND_STROKE
                        this.isAntiAlias = true
                        for ((paintKey, paintValue) in value) {
                            when (paintKey) {
                                "stroke-width" -> {
                                    if (paintValue.contains("px")) {
                                        strokeWidth =
                                            paintValue.substring(0, paintValue.indexOf("px"))
                                                .toFloat()
                                    } else {
                                        strokeWidth =
                                            paintValue.toFloat()
                                    }
                                }
                                "fill" -> {
                                    if (paintValue != "none") {
                                        if (paintValue.length == 4 && paintValue.contains("#")) {
                                            val chars = paintValue.toCharArray()
                                            val newColor =
                                                "#" + chars[1] + chars[1] + chars[2] + chars[2] + chars[3] + chars[3]
                                            this.color = android.graphics.Color.parseColor(newColor)
                                        } else {
                                            this.color =
                                                android.graphics.Color.parseColor(paintValue)
                                        }
                                    } else {
                                        this.style = Paint.Style.STROKE
                                    }
                                }
                                "stroke-miterlimit" -> {
                                    strokeMiter = paintValue.toFloat()
                                }
                                "stroke-linecap" -> {
                                    when (paintValue) {
                                        "round" -> strokeCap = Paint.Cap.ROUND
                                        "square" -> strokeCap = Paint.Cap.SQUARE
                                        "butt" -> strokeCap = Paint.Cap.BUTT
                                    }
                                }
                                "stroke-linejoin" -> {
                                    when (paintValue) {
                                        "round" -> {
                                            strokeJoin =
                                                Paint.Join.ROUND
                                        }
                                        "miter" -> {
                                            strokeJoin =
                                                Paint.Join.MITER
                                        }
                                        "bevel" -> {
                                            strokeJoin =
                                                Paint.Join.BEVEL
                                        }
                                    }
                                }
                                "stroke" -> {
                                    if (paintValue.length == 4) {
                                        val chars = paintValue.toCharArray()
                                        val newColor =
                                            "#" + chars[1] + chars[1] + chars[2] + chars[2] + chars[3] + chars[3]
                                        this.color = android.graphics.Color.parseColor(newColor)
                                    } else {
                                        this.color = android.graphics.Color.parseColor(paintValue)
                                    }
                                }
                            }
                        }
                        if (!isNeedFill) {
                            this.style = Paint.Style.STROKE
                        }
                    })
                }
            }
        }

    //解析渐变色
    var documentElement = parse.documentElement.getElementsByTagName("g")
    documentElement.run {
        for (i in 0 until length) {
            item(i).run {
                when(nodeName)
                {
                    "g"->{
                        attributes.run {
                            val style = this.getNamedItem("style")?.nodeValue ?: ""
                            val opacity = this.getNamedItem("opacity")?.nodeValue ?: ""
                            var groupPaint = Paint()
                            if(style!="")
                                groupPaint =  style2Paint(StyleUtils.convertStyleString2Map(style) as HashMap<String, String>)
                            childNodes.run {
                                for (i in 0 until length) {
                                    item(i).run {
                                        when(nodeName)
                                        {
                                            "linearGradient"->{
                                                attributes.run {
                                                    val x1 =
                                                        (this.getNamedItem("x1")?.nodeValue ?: "0").format("%.1f").toFloat()
                                                    val y1 =
                                                        (this.getNamedItem("y1")?.nodeValue ?: "0").format("%.1f").toFloat()
                                                    val x2 =
                                                        (this.getNamedItem("x2")?.nodeValue ?: "0").format("%.1f").toFloat()
                                                    val y2 =
                                                        (this.getNamedItem("y2")?.nodeValue ?: "0").format("%.1f").toFloat()
                                                    val colorArr = ArrayList<Int>()
                                                    childNodes.run {
                                                        for (i in 0 until length) {
                                                            if (item(i).nodeName == "stop") {
                                                                val colorStr = item(i).attributes.getNamedItem("stop-color")?.nodeValue?: ""
                                                                if(colorStr == "")
                                                                {
                                                                    val convertStyleString2Map =
                                                                        StyleUtils.convertStyleString2Map(item(i).attributes.getNamedItem("style")?.nodeValue?: "")
                                                                    val stylePaint = style2Paint(convertStyleString2Map as HashMap<String, String>,gradientHasMap)
                                                                    if(stylePaint.alpha == 255)
                                                                    {
                                                                        if(opacity!="")
                                                                        {
                                                                            stylePaint.alpha = (opacity.toFloat()*255).toInt()
                                                                        }
                                                                        else if(groupPaint.alpha != 255)
                                                                        {
                                                                            stylePaint.alpha = groupPaint.alpha
                                                                        }
                                                                        colorArr.add(stylePaint.color)
                                                                    }
                                                                    else
                                                                    {
                                                                        colorArr.add(stylePaint.color)
                                                                    }

                                                                }
                                                                else
                                                                {
                                                                    val paint = Paint()
                                                                    paint.color = parseStr2Color(colorStr)
                                                                    if(opacity!="")
                                                                    {
                                                                        paint.alpha = (opacity.toFloat()*255).toInt()
                                                                    }
                                                                    else if(groupPaint.alpha != 255)
                                                                    {
                                                                        paint.alpha = groupPaint.alpha
                                                                    }
                                                                    colorArr.add(
                                                                        paint.color
                                                                    )
                                                                }

                                                            }
                                                        }

                                                    }
                                                    val positionArr = ArrayList<Float>()
                                                    childNodes.run {
                                                        for (i in 0 until length) {
                                                            if (item(i).nodeName == "stop") {
                                                                positionArr.add(
                                                                    (item(i).attributes.getNamedItem("offset")?.nodeValue
                                                                        ?: "0").format("%.1f").toFloat()
                                                                )
                                                            }
                                                        }
                                                    }
                                                    if(!gradientHasMap.contains("url(" + "#" + getNamedItem("id").nodeValue + ")"))
                                                        gradientHasMap.put(
                                                            "url(" + "#" + getNamedItem("id").nodeValue + ")",
                                                            LinearGradient(
                                                                x1,
                                                                y1,
                                                                x2,
                                                                y2,
                                                                colorArr.toIntArray(),
                                                                positionArr.toFloatArray(),
                                                                Shader.TileMode.CLAMP
                                                            )
                                                        )
                                                }
                                            }
                                            "radialGradient"->{
                                                attributes.run {
                                                    val r =
                                                        (this.getNamedItem("r")?.nodeValue ?: "0").format("%.1f").toFloat()
                                                    val cy =
                                                        (this.getNamedItem("cy")?.nodeValue ?: "0").format("%.1f").toFloat()
                                                    val cx =
                                                        (this.getNamedItem("cx")?.nodeValue ?: "0").format("%.1f").toFloat()
                                                    val colorArr = ArrayList<Int>()
                                                    childNodes.run {
                                                        for (i in 0 until length) {
                                                            if (item(i).nodeName == "stop") {
                                                                val colorStr = item(i).attributes.getNamedItem("stop-color")?.nodeValue?: ""
                                                                if(colorStr == "")
                                                                {
                                                                    val convertStyleString2Map =
                                                                        StyleUtils.convertStyleString2Map(item(i).attributes.getNamedItem("style")?.nodeValue?: "")
                                                                    val stylePaint = style2Paint(convertStyleString2Map as HashMap<String, String>,gradientHasMap)
                                                                    if(stylePaint.alpha == 255)
                                                                    {
                                                                        if(opacity!="")
                                                                        {
                                                                            stylePaint.alpha = (opacity.toFloat()*255).toInt()
                                                                        }
                                                                        else if(groupPaint.alpha != 255)
                                                                        {
                                                                            stylePaint.alpha = groupPaint.alpha
                                                                        }
                                                                        colorArr.add(stylePaint.color)
                                                                    }
                                                                    else
                                                                    {
                                                                        colorArr.add(stylePaint.color)
                                                                    }
                                                                }
                                                                else
                                                                {
                                                                    val paint = Paint()
                                                                    paint.color = parseStr2Color(colorStr)
                                                                    if(opacity!="")
                                                                    {
                                                                        paint.alpha = (opacity.toFloat()*255).toInt()
                                                                    }
                                                                    else if(groupPaint.alpha != 255)
                                                                    {
                                                                        paint.alpha = groupPaint.alpha
                                                                    }
                                                                    colorArr.add(
                                                                        paint.color
                                                                    )
                                                                }

                                                            }
                                                        }

                                                    }
                                                    val positionArr = ArrayList<Float>()
                                                    childNodes.run {
                                                        for (i in 0 until length) {
                                                            if (item(i).nodeName == "stop") {
                                                                positionArr.add(
                                                                    (item(i).attributes.getNamedItem("offset")?.nodeValue
                                                                        ?: "0").format("%.1f").toFloat()
                                                                )
                                                            }
                                                        }
                                                    }
                                                    if(!gradientHasMap.contains("url(" + "#" + getNamedItem("id").nodeValue + ")"))
                                                        gradientHasMap.put(
                                                            "url(" + "#" + getNamedItem("id").nodeValue + ")",
                                                            RadialGradient(
                                                                cx,cy,r,
                                                                colorArr.toIntArray(),
                                                                positionArr.toFloatArray(),
                                                                Shader.TileMode.CLAMP
                                                            )
                                                        )
                                                }
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                    else->{}
                }
            }
        }
    }
    //linearGradient
    documentElement = parse.documentElement.getElementsByTagName("linearGradient")
    documentElement.run {
        for (i in 0 until length) {
            item(i).run {
                when(nodeName)
                {
                    "linearGradient"->{
                        attributes.run {
                            val x1 =
                                (this.getNamedItem("x1")?.nodeValue ?: "0").format("%.1f").toFloat()
                            val y1 =
                                (this.getNamedItem("y1")?.nodeValue ?: "0").format("%.1f").toFloat()
                            val x2 =
                                (this.getNamedItem("x2")?.nodeValue ?: "0").format("%.1f").toFloat()
                            val y2 =
                                (this.getNamedItem("y2")?.nodeValue ?: "0").format("%.1f").toFloat()
                            val colorArr = ArrayList<Int>()
                            childNodes.run {
                                for (i in 0 until length) {
                                    if (item(i).nodeName == "stop") {
                                        val colorStr = item(i).attributes.getNamedItem("stop-color")?.nodeValue?: ""
                                        if(colorStr == "")
                                        {
                                            val convertStyleString2Map =
                                                StyleUtils.convertStyleString2Map(item(i).attributes.getNamedItem("style")?.nodeValue?: "")
                                            val stylePaint = style2Paint(convertStyleString2Map as HashMap<String, String>,gradientHasMap)
                                            colorArr.add(stylePaint.color)
                                        }
                                        else
                                        {
                                            colorArr.add(
                                                android.graphics.Color.parseColor(
                                                    colorStr
                                                )
                                            )
                                        }

                                    }
                                }

                            }
                            val positionArr = ArrayList<Float>()
                            childNodes.run {
                                for (i in 0 until length) {
                                    if (item(i).nodeName == "stop") {
                                        positionArr.add(
                                            (item(i).attributes.getNamedItem("offset")?.nodeValue
                                                ?: "0").format("%.1f").toFloat()
                                        )
                                    }
                                }
                            }
                            if(!gradientHasMap.contains("url(" + "#" + getNamedItem("id").nodeValue + ")"))
                            gradientHasMap.put(
                                "url(" + "#" + getNamedItem("id").nodeValue + ")",
                                LinearGradient(
                                    x1,
                                    y1,
                                    x2,
                                    y2,
                                    colorArr.toIntArray(),
                                    positionArr.toFloatArray(),
                                    Shader.TileMode.CLAMP
                                )
                            )
                        }
                    }
                    "radialGradient"->{
                        attributes.run {
                            val r =
                                (this.getNamedItem("r")?.nodeValue ?: "0").format("%.1f").toFloat()
                            val cy =
                                (this.getNamedItem("cy")?.nodeValue ?: "0").format("%.1f").toFloat()
                            val cx =
                                (this.getNamedItem("cx")?.nodeValue ?: "0").format("%.1f").toFloat()
                            val colorArr = ArrayList<Int>()
                            childNodes.run {
                                for (i in 0 until length) {
                                    if (item(i).nodeName == "stop") {
                                        val colorStr = item(i).attributes.getNamedItem("stop-color")?.nodeValue?: ""
                                        if(colorStr == "")
                                        {
                                            val convertStyleString2Map =
                                                StyleUtils.convertStyleString2Map(item(i).attributes.getNamedItem("style")?.nodeValue?: "")
                                            val stylePaint = style2Paint(convertStyleString2Map as HashMap<String, String>,gradientHasMap)
                                            colorArr.add(stylePaint.color)
                                        }
                                        else
                                        {
                                            colorArr.add(
                                                android.graphics.Color.parseColor(
                                                    colorStr
                                                )
                                            )
                                        }

                                    }
                                }

                            }
                            val positionArr = ArrayList<Float>()
                            childNodes.run {
                                for (i in 0 until length) {
                                    if (item(i).nodeName == "stop") {
                                        positionArr.add(
                                            (item(i).attributes.getNamedItem("offset")?.nodeValue
                                                ?: "0").format("%.1f").toFloat()
                                        )
                                    }
                                }
                            }
                            if(!gradientHasMap.contains("url(" + "#" + getNamedItem("id").nodeValue + ")"))
                            gradientHasMap.put(
                                "url(" + "#" + getNamedItem("id").nodeValue + ")",
                                RadialGradient(
                                   cx,cy,r,
                                    colorArr.toIntArray(),
                                    positionArr.toFloatArray(),
                                    Shader.TileMode.CLAMP
                                )
                            )
                        }
                    }
                    else->{}
                }
            }
        }
    }
    //radialGradient
    documentElement = parse.documentElement.getElementsByTagName("radialGradient")
    documentElement.run {
        for (i in 0 until length) {
            item(i).run {
                when(nodeName)
                {
                    "linearGradient"->{
                        attributes.run {
                            val x1 =
                                (this.getNamedItem("x1")?.nodeValue ?: "0").format("%.1f").toFloat()
                            val y1 =
                                (this.getNamedItem("y1")?.nodeValue ?: "0").format("%.1f").toFloat()
                            val x2 =
                                (this.getNamedItem("x2")?.nodeValue ?: "0").format("%.1f").toFloat()
                            val y2 =
                                (this.getNamedItem("y2")?.nodeValue ?: "0").format("%.1f").toFloat()
                            val colorArr = ArrayList<Int>()
                            childNodes.run {
                                for (i in 0 until length) {
                                    if (item(i).nodeName == "stop") {
                                        val colorStr = item(i).attributes.getNamedItem("stop-color")?.nodeValue?: ""
                                        if(colorStr == "")
                                        {
                                            val convertStyleString2Map =
                                                StyleUtils.convertStyleString2Map(item(i).attributes.getNamedItem("style")?.nodeValue?: "")
                                            val stylePaint = style2Paint(convertStyleString2Map as HashMap<String, String>,gradientHasMap)
                                            colorArr.add(stylePaint.color)
                                        }
                                        else
                                        {
                                            colorArr.add(
                                                android.graphics.Color.parseColor(
                                                    colorStr
                                                )
                                            )
                                        }

                                    }
                                }

                            }
                            val positionArr = ArrayList<Float>()
                            childNodes.run {
                                for (i in 0 until length) {
                                    if (item(i).nodeName == "stop") {
                                        positionArr.add(
                                            (item(i).attributes.getNamedItem("offset")?.nodeValue
                                                ?: "0").format("%.1f").toFloat()
                                        )
                                    }
                                }
                            }
                            if(!gradientHasMap.contains("url(" + "#" + getNamedItem("id").nodeValue + ")"))
                            gradientHasMap.put(
                                "url(" + "#" + getNamedItem("id").nodeValue + ")",
                                LinearGradient(
                                    x1,
                                    y1,
                                    x2,
                                    y2,
                                    colorArr.toIntArray(),
                                    positionArr.toFloatArray(),
                                    Shader.TileMode.CLAMP
                                )
                            )
                        }
                    }
                    "radialGradient"->{
                        attributes.run {
                            val r =
                                (this.getNamedItem("r")?.nodeValue ?: "0").format("%.1f").toFloat()
                            val cy =
                                (this.getNamedItem("cy")?.nodeValue ?: "0").format("%.1f").toFloat()
                            val cx =
                                (this.getNamedItem("cx")?.nodeValue ?: "0").format("%.1f").toFloat()
                            val colorArr = ArrayList<Int>()
                            childNodes.run {
                                for (i in 0 until length) {
                                    if (item(i).nodeName == "stop") {
                                        val colorStr = item(i).attributes.getNamedItem("stop-color")?.nodeValue?: ""
                                        if(colorStr == "")
                                        {
                                            val convertStyleString2Map =
                                                StyleUtils.convertStyleString2Map(item(i).attributes.getNamedItem("style")?.nodeValue?: "")
                                            val stylePaint = style2Paint(convertStyleString2Map as HashMap<String, String>,gradientHasMap)
                                            colorArr.add(stylePaint.color)
                                        }
                                        else
                                        {
                                            colorArr.add(
                                                android.graphics.Color.parseColor(
                                                    colorStr
                                                )
                                            )
                                        }

                                    }
                                }

                            }
                            val positionArr = ArrayList<Float>()
                            childNodes.run {
                                for (i in 0 until length) {
                                    if (item(i).nodeName == "stop") {
                                        positionArr.add(
                                            (item(i).attributes.getNamedItem("offset")?.nodeValue
                                                ?: "0").format("%.1f").toFloat()
                                        )
                                    }
                                }
                            }
                            if(!gradientHasMap.contains("url(" + "#" + getNamedItem("id").nodeValue + ")"))
                            gradientHasMap.put(
                                "url(" + "#" + getNamedItem("id").nodeValue + ")",
                                RadialGradient(
                                    cx,cy,r,
                                    colorArr.toIntArray(),
                                    positionArr.toFloatArray(),
                                    Shader.TileMode.CLAMP
                                )
                            )
                        }
                    }
                    else->{}
                }
            }
        }
    }

    val pullParser = XmlPullParserFactory.newInstance().newPullParser()
    pullParser.setInput(decodeToString.reader())
    pullParser.run {
        var latest = 0
        //全局颜色
        var groupFill = android.graphics.Color.BLACK

        //全局透明度
        var groupOpacity = 255
        var groupPaintList = ArrayList<Paint>()
        var groupPaint = Paint()
        latest = next()
        var displayG = ""
        var displayGNum = 0
        while (latest != XmlPullParser.END_DOCUMENT) {
            if (latest == XmlPullParser.START_TAG) {
                if(displayG=="none")
                {
                    if(name == "g")
                    {
                        displayGNum++
                    }
                    latest = next()
                    break
                }
                val newBitmap = bitmap.copy(Bitmap.Config.ARGB_8888, false)
                bitmapList.add(newBitmap)
                when (name) {
                    "rect" -> {
                        var x = 0f
                        var y = 0f
                        var height = 0f
                        var width = 0f
                        var rx = 0f
                        var ry = 0f
                        var fill = ""
                        var fill_opacity = "1"
                        var classAtt = ""
                        var strokeColor = ""
                        var stylePaint:Paint? = null
                        var paint = Paint()
                        paint.color = groupPaint.color
                        paint.alpha = groupPaint.alpha
                        for (i in 0 until attributeCount) {
                            when (getAttributeName(i)) {
                                "x" -> {
                                    x = getAttributeValue(i).format("%.1f").toFloat()
                                }
                                "y" -> {
                                    y = getAttributeValue(i).format("%.1f").toFloat()
                                }
                                "height" -> {
                                    height = getAttributeValue(i).format("%.1f").toFloat()
                                }
                                "width" -> {
                                    width = getAttributeValue(i).format("%.1f").toFloat()
                                }
                                "rx" -> {
                                    rx = getAttributeValue(i).format("%.1f").toFloat()
                                }
                                "ry" -> {
                                    ry = getAttributeValue(i).format("%.1f").toFloat()
                                }
                                "fill" -> {
                                    fill = getAttributeValue(i)
                                }
                                "fill-opacity" -> {
                                    fill_opacity = getAttributeValue(i)
                                }
                                "class" -> {
                                    classAtt = getAttributeValue(i)
                                }
                                "stroke" -> {
                                    strokeColor = getAttributeValue(i)
                                }
                                "style" -> {
                                    val convertStyleString2Map =
                                        StyleUtils.convertStyleString2Map(getAttributeValue(i))
                                    stylePaint = style2Paint(convertStyleString2Map as HashMap<String, String>,gradientHasMap).styleAndGroupStyle(paint).apply {
                                        if (!isNeedFill) {
                                            this.style = Paint.Style.STROKE
                                        }
                                    }
                                }
                            }
                        }
                        if (ry == 0f) ry = rx
                        else if (rx == 0f) rx = ry
                        if(stylePaint!=null)
                        {
                            canvas.drawRoundRect(
                                x,
                                y,
                                x + width,
                                y + height,
                                rx,
                                ry,
                                stylePaint)
                        }
                        else if (gradientHasMap.contains(fill))
                            canvas.drawRoundRect(
                                x,
                                y,
                                x + width,
                                y + height,
                                rx,
                                ry,
                                paint.apply {
                                    if (!isNeedFill) {
                                        this.style = Paint.Style.STROKE
                                    }
                                    this.isAntiAlias = true
                                    if (fill.contains("url")) {
                                        shader =
                                            gradientHasMap[fill]
                                        alpha = (255 * fill_opacity.toFloat()).toInt()
                                    } else {
                                        if (fill == "none" && strokeColor != "") {
                                            style = Paint.Style.STROKE
                                            if (strokeColor.length == 4) {
                                                val chars = strokeColor.toCharArray()
                                                val newColor =
                                                    "#" + chars[1] + chars[1] + chars[2] + chars[2] + chars[3] + chars[3]
                                                color = android.graphics.Color.parseColor(newColor)
                                            } else {
                                                color =
                                                    android.graphics.Color.parseColor(strokeColor)
                                            }
                                            alpha = (255 * fill_opacity.toFloat()).toInt()
                                        }
                                    }
                                })
                        else {
                            if (paintStyleMap.contains("." + classAtt)) {
                                canvas.drawRoundRect(
                                    x,
                                    y,
                                    x + width,
                                    y + height,
                                    rx,
                                    ry,
                                    paintStyleMap.get("." + classAtt)!!
                                )
                            }
                        }
                    }
                    "path" -> {
                        var pathData = ""
                        val paint = Paint()
                        var fill = ""
                        var classAtt = ""
                        var strokeColor = ""
                        var stylePaint:Paint? = null
                        paint.color = groupPaint.color
                        paint.alpha = groupPaint.alpha
                        for (i in 0 until attributeCount) {
                            when (getAttributeName(i)) {
                                "class" -> {
                                    classAtt = getAttributeValue(i)
                                }
                                "d" -> {
                                    pathData =
                                        getAttributeValue(i).replace("\n", "").replace("\t", "")
                                }
                                "stroke-linejoin" -> {
                                    when (getAttributeValue(i)) {
                                        "round" -> {
                                            paint.strokeJoin =
                                                Paint.Join.ROUND
                                        }
                                        "miter" -> {
                                            paint.strokeJoin =
                                                Paint.Join.MITER
                                        }
                                        "bevel" -> {
                                            paint.strokeJoin =
                                                Paint.Join.BEVEL
                                        }
                                    }
                                }
                                "stroke-linecap" -> {
                                    when (getAttributeValue(i)) {
                                        "round" -> paint.strokeCap = Paint.Cap.ROUND
                                        "square" -> paint.strokeCap = Paint.Cap.SQUARE
                                        "butt" -> paint.strokeCap = Paint.Cap.BUTT
                                    }
                                }
                                "stroke-width" -> {
                                    paint.strokeWidth = getAttributeValue(i).toFloat()
                                }
                                "fill" -> {
                                    fill = getAttributeValue(i)
                                    if (fill.contains("url")) {
                                        paint.run {
                                            shader =
                                                gradientHasMap[fill]
                                        }
                                    } else {
                                        if (fill == "none" && strokeColor != "") {
                                            paint.style = Paint.Style.STROKE
                                            if (strokeColor.length == 4) {
                                                val chars = strokeColor.toCharArray()
                                                val newColor =
                                                    "#" + chars[1] + chars[1] + chars[2] + chars[2] + chars[3] + chars[3]
                                                paint.color =
                                                    android.graphics.Color.parseColor(newColor)
                                            } else {
                                                paint.color =
                                                    android.graphics.Color.parseColor(strokeColor)
                                            }
                                        }
                                    }
                                }
                                "stroke" -> {
                                    strokeColor = getAttributeValue(i)
                                }
                                "style" -> {
                                    val convertStyleString2Map =
                                        StyleUtils.convertStyleString2Map(getAttributeValue(i))
                                    stylePaint = style2Paint(convertStyleString2Map as HashMap<String, String>,gradientHasMap).styleAndGroupStyle(paint).apply {
                                        if (!isNeedFill) {
                                            this.style = Paint.Style.STROKE
                                        }
                                    }
                                }
                            }
                        }
                        val path = createPathFromPathData(pathData)
                        if(stylePaint!=null)
                        {
                            canvas.drawPath(path, stylePaint)
                        }
                        else if (gradientHasMap.contains(fill) || classAtt == "") {
                            if (!isNeedFill) {
                                paint.style = Paint.Style.STROKE
                            }
                            canvas.drawPath(path, paint.apply { this.isAntiAlias = true })
                        } else {
                            if (paintStyleMap.contains("." + classAtt)) {
                                canvas.drawPath(path, paintStyleMap.get("." + classAtt)!!)
                            }
                        }
                    }
                    "ellipse" -> {
                        var cx = 0f
                        var cy = 0f
                        var rx = 0f
                        var ry = 0f
                        var fill = ""
                        var fill_opacity = "1"
                        var strokeColor = ""
                        var classAtt = ""
                        var stylePaint:Paint? = null
                        var paint = Paint()
                        paint.color = groupPaint.color
                        paint.alpha = groupPaint.alpha
                        for (i in 0 until attributeCount) {
                            when (getAttributeName(i)) {
                                "class" -> {
                                    classAtt = getAttributeValue(i)
                                }
                                "cx" -> {
                                    cx = getAttributeValue(i).format("%.1f").toFloat()
                                }
                                "cy" -> {
                                    cy = getAttributeValue(i).format("%.1f").toFloat()
                                }
                                "rx" -> {
                                    rx = getAttributeValue(i).format("%.1f").toFloat()
                                }
                                "ry" -> {
                                    ry = getAttributeValue(i).format("%.1f").toFloat()
                                }
                                "fill" -> {
                                    fill = getAttributeValue(i)
                                }
                                "fill-opacity" -> {
                                    fill_opacity = getAttributeValue(i)
                                }
                                "stroke" -> {
                                    strokeColor = getAttributeValue(i)
                                }
                                "style" -> {
                                    val convertStyleString2Map =
                                        StyleUtils.convertStyleString2Map(getAttributeValue(i))
                                    stylePaint = style2Paint(convertStyleString2Map as HashMap<String, String>,gradientHasMap).styleAndGroupStyle(paint).apply {                         if (!isNeedFill) {
                                        this.style = Paint.Style.STROKE
                                    } }
                                }
                            }
                        }
                        if(stylePaint!=null)
                        {
                            canvas.drawOval(
                                cx - rx,
                                cy - ry,
                                cx + rx,
                                cy + ry,
                                stylePaint
                            )
                        }
                        else if (gradientHasMap.contains(fill) || classAtt == "") {
                            canvas.drawOval(cx - rx, cy - ry, cx + rx, cy + ry, paint.apply {
                                this.isAntiAlias = true
                                if (!isNeedFill) {
                                    this.style = Paint.Style.STROKE
                                }
                                if (fill.contains("url")) {
                                    shader =
                                        gradientHasMap[fill]
                                    alpha = (255 * fill_opacity.toFloat()).toInt()
                                } else {
                                    if (fill != "" && fill != "none")
                                        color = android.graphics.Color.parseColor(fill)
                                    else {
                                        if (fill == "none" && strokeColor != "") {
                                            style = Paint.Style.STROKE
                                            if (strokeColor.length == 4) {
                                                val chars = strokeColor.toCharArray()
                                                val newColor =
                                                    "#" + chars[1] + chars[1] + chars[2] + chars[2] + chars[3] + chars[3]
                                                color = android.graphics.Color.parseColor(newColor)
                                            } else {
                                                color =
                                                    android.graphics.Color.parseColor(strokeColor)
                                            }
                                            alpha = (255 * fill_opacity.toFloat()).toInt()
                                        }
                                    }
                                }
                            })
                        } else {
                            if (paintStyleMap.contains("." + classAtt)) {
                                canvas.drawOval(
                                    cx - rx,
                                    cy - ry,
                                    cx + rx,
                                    cy + ry,
                                    paintStyleMap.get("." + classAtt)!!
                                )
                            }
                        }

                    }
                    "circle" -> {
                        var cx = 0f
                        var cy = 0f
                        var r = 0f
                        var fill = ""
                        var fill_opacity = "1"
                        var classAtt = ""
                        var strokeColor = ""
                        var stylePaint:Paint? = null
                        var paint = Paint()
                        paint.color = groupPaint.color
                        paint.alpha = groupPaint.alpha
                        for (i in 0 until attributeCount) {
                            when (getAttributeName(i)) {
                                "class" -> {
                                    classAtt = getAttributeValue(i)
                                }
                                "cx" -> {
                                    cx = getAttributeValue(i).format("%.1f").toFloat()
                                }
                                "cy" -> {
                                    cy = getAttributeValue(i).format("%.1f").toFloat()
                                }
                                "r" -> {
                                    r = getAttributeValue(i).format("%.1f").toFloat()
                                }
                                "fill" -> {
                                    fill = getAttributeValue(i)
                                }
                                "fill-opacity" -> {
                                    fill_opacity = getAttributeValue(i)
                                }
                                "stroke" -> {
                                    strokeColor = getAttributeValue(i)
                                }
                                "style" -> {
                                    val convertStyleString2Map =
                                        StyleUtils.convertStyleString2Map(getAttributeValue(i))
                                    stylePaint = style2Paint(convertStyleString2Map as HashMap<String, String>,gradientHasMap).styleAndGroupStyle(paint).apply {
                                        if (!isNeedFill) {
                                            this.style = Paint.Style.STROKE
                                        }
                                    }
                                }
                            }

                        }
                        if(stylePaint!=null)
                        {
                            canvas.drawCircle(cx, cy, r, stylePaint)
                        }
                        else if (gradientHasMap.contains(fill) || classAtt == "")
                            canvas.drawCircle(cx, cy, r, paint.apply {
                                this.isAntiAlias = true
                                if (!isNeedFill) {
                                    this.style = Paint.Style.STROKE
                                }
                                if (fill.contains("url")) {
                                    shader =
                                        gradientHasMap[fill]
                                    alpha = (255 * fill_opacity.toFloat()).toInt()
                                } else {
                                    if (fill == "none" && strokeColor != "") {
                                        style = Paint.Style.STROKE
                                        if (strokeColor.length == 4) {
                                            val chars = strokeColor.toCharArray()
                                            val newColor =
                                                "#" + chars[1] + chars[1] + chars[2] + chars[2] + chars[3] + chars[3]
                                            color = android.graphics.Color.parseColor(newColor)
                                        } else {
                                            color =
                                                android.graphics.Color.parseColor(strokeColor)
                                        }
                                        alpha = (255 * fill_opacity.toFloat()).toInt()
                                    }
                                }
                            })
                        else {
                            if (paintStyleMap.contains("." + classAtt)) {
                                canvas.drawCircle(cx, cy, r, paintStyleMap.get("." + classAtt)!!)
                            }
                        }
                    }
                    "polygon" -> {
                        var pointsAtt = ""
                        var fill = ""
                        var fill_opacity = "1"
                        var classAtt = ""
                        var strokeColor = ""
                        var stylePaint:Paint? = null
                        var paint = Paint()
                        paint.color = groupPaint.color
                        paint.alpha = groupPaint.alpha
                        for (i in 0 until attributeCount) {
                            when (getAttributeName(i)) {
                                "class" -> {
                                    classAtt = getAttributeValue(i)
                                }
                                "points" -> {
                                    pointsAtt =
                                        getAttributeValue(i).replace("\n", "").replace("\t", "")
                                }
                                "fill" -> {
                                    fill = getAttributeValue(i)
                                }
                                "fill-opacity" -> {
                                    fill_opacity = getAttributeValue(i)
                                }
                                "stroke" -> {
                                    strokeColor = getAttributeValue(i)
                                }
                                "style" -> {
                                    val convertStyleString2Map =
                                        StyleUtils.convertStyleString2Map(getAttributeValue(i))
                                    stylePaint = style2Paint(convertStyleString2Map as HashMap<String, String>,gradientHasMap).styleAndGroupStyle(paint).apply {
                                        if (!isNeedFill) {
                                            this.style = Paint.Style.STROKE
                                        }
                                    }
                                }
                            }

                        }
                        val polygon = Polygon().apply {
                            this.points = pointsAtt
                            toPath()
                        }
                        if(stylePaint!=null)
                        {
                            canvas.drawPath(
                                PathParser.createPathFromPathData(polygon.pathData),
                                stylePaint
                            )
                        }
                        else if (pointsAtt != "") {
                            if (gradientHasMap.contains(fill) || classAtt == "") {
                                canvas.drawPath(
                                    PathParser.createPathFromPathData(polygon.pathData),
                                    paint.apply {
                                        if (!isNeedFill) {
                                            this.style = Paint.Style.STROKE
                                        }
                                        this.isAntiAlias = true
                                        if (fill.contains("url")) {
                                            shader =
                                                gradientHasMap[fill]
                                            alpha = (255 * fill_opacity.toFloat()).toInt()
                                        } else {
                                            if (fill == "none" && strokeColor != "") {
                                                style = Paint.Style.STROKE
                                                if (strokeColor.length == 4) {
                                                    val chars = strokeColor.toCharArray()
                                                    val newColor =
                                                        "#" + chars[1] + chars[1] + chars[2] + chars[2] + chars[3] + chars[3]
                                                    color =
                                                        android.graphics.Color.parseColor(newColor)
                                                } else {
                                                    color =
                                                        android.graphics.Color.parseColor(
                                                            strokeColor
                                                        )
                                                }
                                                alpha = (255 * fill_opacity.toFloat()).toInt()
                                            }
                                        }
                                    })
                            } else {
                                if (paintStyleMap.contains("." + classAtt)) {
                                    canvas.drawPath(
                                        PathParser.createPathFromPathData(polygon.pathData),
                                        paintStyleMap.get("." + classAtt)!!
                                    )
                                }
                            }
                        }

                    }
                    "polyline" -> {
                        var pointsAtt = ""
                        var fill = ""
                        var fill_opacity = "1"
                        var classAtt = ""
                        var strokeColor = ""
                        var stylePaint:Paint? = null
                        var paint = Paint()
                        paint.color = groupPaint.color
                        paint.alpha = groupPaint.alpha
                        for (i in 0 until attributeCount) {
                            when (getAttributeName(i)) {
                                "class" -> {
                                    classAtt = getAttributeValue(i)
                                }
                                "points" -> {
                                    pointsAtt =
                                        getAttributeValue(i).replace("\n", "").replace("\t", "")
                                }
                                "fill" -> {
                                    fill = getAttributeValue(i)
                                }
                                "fill-opacity" -> {
                                    fill_opacity = getAttributeValue(i)
                                }
                                "stroke" -> {
                                    strokeColor = getAttributeValue(i)
                                }
                                "style" -> {
                                    val convertStyleString2Map =
                                        StyleUtils.convertStyleString2Map(getAttributeValue(i))
                                    stylePaint = style2Paint(convertStyleString2Map as HashMap<String, String>,gradientHasMap).styleAndGroupStyle(paint).apply {
                                        if (!isNeedFill) {
                                            this.style = Paint.Style.STROKE
                                        }
                                    }
                                }
                            }

                        }
                        val polygon = Polyline().apply {
                            this.points = pointsAtt
                            toPath()
                        }
                        if (pointsAtt != "") {
                            if(stylePaint!=null)
                            {
                                canvas.drawPath(
                                    PathParser.createPathFromPathData(polygon.pathData),
                                    stylePaint
                                )
                            }
                            else if (gradientHasMap.contains(fill) || classAtt == "") {
                                canvas.drawPath(
                                    PathParser.createPathFromPathData(polygon.pathData),
                                    paint.apply {
                                        if (!isNeedFill) {
                                            this.style = Paint.Style.STROKE
                                        }
                                        this.isAntiAlias = true
                                        if (fill.contains("url")) {
                                            shader =
                                                gradientHasMap[fill]
                                            alpha = (255 * fill_opacity.toFloat()).toInt()
                                        } else {
                                            if (fill == "none" && strokeColor != "") {
                                                style = Paint.Style.STROKE
                                                if (strokeColor.length == 4) {
                                                    val chars = strokeColor.toCharArray()
                                                    val newColor =
                                                        "#" + chars[1] + chars[1] + chars[2] + chars[2] + chars[3] + chars[3]
                                                    color =
                                                        android.graphics.Color.parseColor(newColor)
                                                } else {
                                                    color =
                                                        android.graphics.Color.parseColor(
                                                            strokeColor
                                                        )
                                                }
                                                alpha = (255 * fill_opacity.toFloat()).toInt()
                                            }
                                        }
                                    })
                            } else {
                                if (paintStyleMap.contains("." + classAtt)) {
                                    canvas.drawPath(
                                        PathParser.createPathFromPathData(polygon.pathData),
                                        paintStyleMap.get("." + classAtt)!!
                                    )
                                }
                            }
                        }

                    }
                    "line" -> {
                        var x1 = 0f
                        var y1 = 0f
                        var x2 = 0f
                        var y2 = 0f
                        var fill = ""
                        var fill_opacity = "1"
                        var classAtt = ""
                        var strokeColor = ""
                        var stylePaint:Paint? = null
                        var paint = Paint()
                        paint.color = groupPaint.color
                        paint.alpha = groupPaint.alpha
                        for (i in 0 until attributeCount) {
                            when (getAttributeName(i)) {
                                "x1" -> {
                                    x1 = getAttributeValue(i).format("%.1f").toFloat()
                                }
                                "y1" -> {
                                    y1 = getAttributeValue(i).format("%.1f").toFloat()
                                }
                                "x2" -> {
                                    x2 = getAttributeValue(i).format("%.1f").toFloat()
                                }
                                "y2" -> {
                                    y2 = getAttributeValue(i).format("%.1f").toFloat()
                                }
                                "fill" -> {
                                    fill = getAttributeValue(i)
                                }
                                "fill-opacity" -> {
                                    fill_opacity = getAttributeValue(i)
                                }
                                "class" -> {
                                    classAtt = getAttributeValue(i)
                                }
                                "stroke" -> {
                                    strokeColor = getAttributeValue(i)
                                }
                                "style" -> {
                                    val convertStyleString2Map =
                                        StyleUtils.convertStyleString2Map(getAttributeValue(i))
                                    stylePaint = style2Paint(convertStyleString2Map as HashMap<String, String>,gradientHasMap).styleAndGroupStyle(paint).apply {
                                        if (!isNeedFill) {
                                            this.style = Paint.Style.STROKE
                                        }
                                    }
                                }
                            }
                        }
                        if(stylePaint!=null)
                        {
                            canvas.drawLine(x1, y1, x2, y2, stylePaint)
                        }
                        else if (gradientHasMap.contains(fill) || classAtt == "")
                            canvas.drawLine(x1, y1, x2, y2, paint.apply {
                                if (!isNeedFill) {
                                    this.style = Paint.Style.STROKE
                                }
                                this.isAntiAlias = true
                                if (fill.contains("url")) {
                                    shader =
                                        gradientHasMap[fill]
                                    alpha = (255 * fill_opacity.toFloat()).toInt()
                                } else {
                                    if (fill == "none" && strokeColor != "") {
                                        style = Paint.Style.STROKE
                                        if (strokeColor.length == 4) {
                                            val chars = strokeColor.toCharArray()
                                            val newColor =
                                                "#" + chars[1] + chars[1] + chars[2] + chars[2] + chars[3] + chars[3]
                                            color = android.graphics.Color.parseColor(newColor)
                                        } else {
                                            color =
                                                android.graphics.Color.parseColor(strokeColor)
                                        }
                                        alpha = (255 * fill_opacity.toFloat()).toInt()
                                    }
                                }
                            })
                        else {
                            if (paintStyleMap.contains("." + classAtt)) {
                                canvas.drawLine(x1, y1, x2, y2, paintStyleMap.get("." + classAtt)!!)
                            }
                        }
                    }
                    "g"->{
                        var style = ""
                        var fill = ""
                        var opacity = ""
                        var classAtt = ""
                        groupPaint = Paint()
                        for (i in 0 until attributeCount) {
                            when (getAttributeName(i)) {
                                "fill" -> {
                                    fill = getAttributeValue(i)
                                    if (gradientHasMap.contains(fill))
                                        {
                                        groupPaint.shader = gradientHasMap[fill]
                                        }
                                        else
                                    {
                                        groupPaint.color = parseStr2Color(fill)
                                    }
                                }
                                "opacity" -> {
                                    groupPaint.alpha =  (getAttributeValue(i).toFloat()*255).toInt()
                                }
                                "style" -> {
                                    val convertStyleString2Map =
                                        StyleUtils.convertStyleString2Map(getAttributeValue(i))
                                    if(convertStyleString2Map.contains("display"))
                                    {
                                        if(convertStyleString2Map.get("display")=="none")
                                        {
                                            displayG = "none"
                                        }
                                    }
                                    groupPaint = style2Paint(convertStyleString2Map as HashMap<String, String>,gradientHasMap)
                                }
                                "class"->{
                                    classAtt = getAttributeValue(i)
                                        if (paintStyleMap.contains("." + classAtt)) {
                                            groupPaint = paintStyleMap["." + classAtt]!!
                                        }
                                }
                                "display"->{
                                    if(getAttributeValue(i)=="none")
                                    {
                                        displayG = "none"
                                    }
                                }
                            }
                        }
                        groupPaintList.add(groupPaint)
                    }
                }
            }
            else if(latest == XmlPullParser.END_TAG && name=="g")
            {
                    if(groupPaintList.size>=1&&displayGNum==0)
                    {
                        groupPaintList.removeLast()
                        if(groupPaintList.size == 0)
                        {
                            groupPaint = Paint()
                        }
                        else
                        {
                            groupPaint = groupPaintList.last()
                        }
                    }
                if(displayGNum==0)
                    displayG = ""
                else
                {
                    displayGNum--
                }
            }
            latest = next()
        }
    }
    val path =
        Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES).absolutePath
    bitmap.compress(Bitmap.CompressFormat.JPEG, 100, FileOutputStream(File("$path/svg2.jpeg")))
    return bitmapList
}

fun canvasSaveToBitMap(bitmapArr: ArrayList<Bitmap>, progress: MutableState<String>,progressCurrent: MutableState<Float>,) {
    val baos = ByteArrayOutputStream()
    val localAnimatedGifEncoder = AnimatedGifEncoder()
    localAnimatedGifEncoder.start(baos) //start
    localAnimatedGifEncoder.setRepeat(1) //设置生成gif的开始播放时间。0为立即开始播放
    localAnimatedGifEncoder.setDelay(200)
    localAnimatedGifEncoder.setQuality(1)

    for ((index,bitmap) in bitmapArr.withIndex()) {
        localAnimatedGifEncoder.addFrame(bitmap)
        progressCurrent.value = index.toFloat()/bitmapArr.size
    }
    val path =
        Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES).absolutePath
    localAnimatedGifEncoder.finish() //finish
    val gifNamePath = path + "/" + System.currentTimeMillis() + ".gif"
    val file = File(gifNamePath)
    val fos = FileOutputStream(file)
    baos.writeTo(fos)
    baos.flush()
    fos.flush()
    baos.close()
    fos.close()
    progress.value = "加载完成,保存在了" + gifNamePath
    bitmapArr.clear()
    progressCurrent.value = 1f
}

fun drawRect() {

}

fun dp2px(dpValue: Float): Float {
    return TypedValue.applyDimension(
        TypedValue.COMPLEX_UNIT_DIP,
        dpValue,
        Resources.getSystem().displayMetrics
    )
}

fun Float.px2Dp(): Float {
    return this / Resources.getSystem().displayMetrics.density
}

fun parseColor(color: String) =
    if (color.length == 4) {
        val chars = color.toCharArray()
        val newColor =
            "#" + chars[1] + chars[1] + chars[2] + chars[2] + chars[3] + chars[3]
        android.graphics.Color.parseColor(newColor)
    } else {
        android.graphics.Color.parseColor(color)
    }

fun style2Paint(map: HashMap<String, String>,gradient:HashMap<String, Shader>?=null):Paint {
    val paint = Paint()

    if(gradient!=null)
    {
        for ((key, value) in map) {
            when (key) {
                "opacity" -> paint.alpha = (value.toFloat() * 255).toInt()
                "fill" -> {
                    if(value == "none")
                    {
                        paint.style = Paint.Style.STROKE
                    }else if(gradient.contains(value))
                    {
                        paint.shader = gradient[value]
                    }
                    else {
                        if(paint.alpha!=255)
                        {
                            val alpha = paint.alpha
                            paint.color = parseStr2Color(value)
                            paint.alpha = alpha
                        }
                        else{
                            paint.color = parseStr2Color(value)
                        }
                    }
                }
                "stroke"->{
                    paint.color = android.graphics.Color.parseColor(value)
                }
                "stop-color"->{
                    paint.color = parseStr2Color(value)
                }
            }
        }
    }
    else
    {
        for ((key, value) in map) {
            when (key) {
                "opacity" -> paint.alpha = (value.toFloat() * 255).toInt()
                "fill" -> {
                    if(value == "none")
                    {
                        paint.style = Paint.Style.STROKE
                    }
                    else {
                        if(paint.alpha!=255)
                        {
                            val alpha = paint.alpha
                            paint.color = parseStr2Color(value)
                            paint.alpha = alpha
                        }
                        else{
                            paint.color = parseStr2Color(value)
                        }
                    }
                }
                "stroke"->{
                    paint.color = android.graphics.Color.parseColor(value)
                }
                "stop-color"->{
                    paint.color = parseStr2Color(value)
                }
            }
        }
    }
    return paint
}
fun parseStr2Color(colorStr:String)=
    if(colorStr!="none")
    {
        if (colorStr.length == 4) {
            val chars = colorStr.toCharArray()
            val newColor =
                "#" + chars[1] + chars[1] + chars[2] + chars[2] + chars[3] + chars[3]
            android.graphics.Color.parseColor(newColor)
        } else {
            android.graphics.Color.parseColor(colorStr)
        }
    }
    else
    {
        android.graphics.Color.BLACK
    }

fun Paint.styleAndGroupStyle(paint: Paint):Paint
{
    if(this.color == android.graphics.Color.BLACK)
    {
        this.color = paint.color
    }
    if(this.alpha == 255)
    {
        this.alpha = paint.alpha
    }
    return this
}