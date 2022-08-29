package com.fhj.test2

import android.content.res.Resources
import android.content.res.TypedArray
import android.graphics.Paint
import android.graphics.Path
import android.util.Xml
import android.webkit.JavascriptInterface
import androidx.core.content.res.TypedArrayUtils
import androidx.core.graphics.PathParser
import androidx.vectordrawable.graphics.drawable.VectorDrawableCompat
import com.github.megatronking.svg.generator.svg.Svg2Vector
import com.github.megatronking.svg.generator.svg.css.CSSParser
import com.github.megatronking.svg.generator.svg.css.CSSParserCallback
import com.github.megatronking.svg.generator.svg.model.Polygon
import com.github.megatronking.svg.generator.svg.model.SvgNode
import com.github.megatronking.svg.generator.svg.utils.StyleUtils
import okhttp3.internal.toHexString
import org.xml.sax.ext.DefaultHandler2
import org.xml.sax.helpers.DefaultHandler
import org.xmlpull.v1.XmlPullParserFactory

import java.io.*
import javax.xml.parsers.DocumentBuilderFactory
import kotlin.collections.HashMap

//val childNodes = parse.documentElement.childNodes
//childNodes.run {
//    for(i in 0 until length)
//    {
//        item(i).run {
//            if(this.nodeName=="path")
//            {
//                println(this.attributes.item(1).nodeValue)
//                println(this.attributes.item(0).nodeValue)
//            }
//        }
//    }
//}
val s = "M647.2,388.8l-58.4-11.6a7.6,7.6,0,0,1-2.2-14.1L638.5,334a7.3,7.3,0,0,0,3.8-7.4l-7-59.1A7.6,7.6,0,0,1,648,261l43.7,40.4a7.5,7.5,0,0,0,8.3,1.3l54-24.9a7.6,7.6,0,0,1,10.1,10.1l-24.9,54a7.5,7.5,0,0,0,1.3,8.3l40.4,43.7a7.6,7.6,0,0,1-6.5,12.7l-59.1-7a7.5,7.5,0,0,0-7.5,3.8l-29,51.9a7.6,7.6,0,0,1-14.1-2.2l-11.6-58.4A7.5,7.5,0,0,0,647.2,388.8Z"
fun main()
{

}


fun testXml(inputStream: InputStream)
{
    val newPullParser = XmlPullParserFactory.newInstance().newPullParser()
    newPullParser.run {
        setInput(inputStream.reader())
        var latest = nextTag()
        var last = 0
        var asAttributeSet = Xml.asAttributeSet(this)
        while(asAttributeSet.attributeCount!=-1){
            if(last!=latest)
            {
                asAttributeSet = Xml.asAttributeSet(this)
                last = latest
            }
            latest = nextTag()
        }
    }
}

fun readXml()
{
    val hashMap = HashMap<String,String>()
    val documentBuilder = DocumentBuilderFactory.newInstance().newDocumentBuilder()
    val parse = documentBuilder.parse(File("app\\src\\main\\res\\drawable\\svg1.xml"))
    val documentElement = parse.documentElement
    val elementsByTagNameNS = documentElement.getElementsByTagName("g")
    val item = elementsByTagNameNS.item(1).childNodes
    item.apply {
        for(i in 0 until length)
        {
            item(i).run {
                if(nodeName == "path")
                {
                    attributes.apply {
                        val key = StringBuffer()
                        item(0).nodeValue.toCharArray().filter {
                            it!='\t'&&it!='\n'
                        }.forEach {
                            key.append(it)
                        }
                        hashMap[key.toString()] = item(1).nodeValue
                    }
                }

            }
        }
    }
    ProcessMethod(hashMap)
}
//@Composable
fun ProcessMethod(hashMap: HashMap<String,String>) {
//val path = Path()

hashMap.forEach{ (methods, color) ->
    val p = ByteArrayInputStream(methods.toByteArray())
    var a = p.read()
    var x = 0f
    var y =0f
//    PathParser().parsePathString(methods).toPath()


//    val nextStart = nextStart("M1642.5,2151.8h1469.2c97.4,0 -86.8,80.5 -86.8,180.6v496.4c0,100 184.2,180.6 86.8,180.6L1642.5,3009.3c-97.4,0 -175.8,-80.5 -175.8,-180.6v-496.4C1466.8,2232.3 1545.1,2151.8 1642.5,2151.8z", 1)
//    println(nextStart)

//    rememberVectorPainter(image = ImageVector.vectorResource(id = R.drawable.ic_____))
//    Canvas(modifier =Modifier){
//        drawPath(PathParser().parsePathString(methods).toPath(), color = Color(android.graphics.Color.parseColor(color)))
//    }


}

}
private fun nextStart(s: String, end: Int): Int {
    var index = end
    var c: Char

    while (index < s.length) {
        c = s[index]
        // Note that 'e' or 'E' are not valid path commands, but could be
        // used for floating point numbers' scientific notation.
        // Therefore, when searching for next command, we should ignore 'e'
        // and 'E'.
        if (((c - 'A') * (c - 'Z') <= 0 || (c - 'a') * (c - 'z') <= 0) &&
            c != 'e' && c != 'E'
        ) {
            return index
        }
        index++
    }
    return index
}
//while (a!=0)
/*    {
        when (Char(a)) {

            'M' -> {
                path.moveTo(lastX, lastY)
            }

            'm' -> {
                path.relativeMoveTo(lastX, lastY)
            }

            'L' -> {
                path.lineTo(lastX, lastY)
            }

            'l' -> {
                path.relativeLineTo(lastX, lastY)
            }

            'H' -> {
                //注意此时y坐标应该是当前位置的y坐标
                pathMeasure.setPath(path.asAndroidPath(), false)

                val position = FloatArray(2)
                val tan = FloatArray(2)

                pathMeasure.getPosTan(pathMeasure.length, position, tan)
                path.lineTo(lastX, position[1])
            }

            'h' -> {
                path.relativeLineTo(lastX, 0f)
            }

            'V' -> {
                //注意此时x坐标应该是当前位置的x坐标
                pathMeasure.setPath(path.asAndroidPath(), false)

                val position = FloatArray(2)
                val tan = FloatArray(2)

                pathMeasure.getPosTan(pathMeasure.length, position, tan)
                path.lineTo(position[0], lastX)
            }

            'v' -> {
                path.relativeLineTo(0f, lastX)
            }

            'C' -> {
                path.cubicTo()
            }
            'c' -> {
                path.relativeCubicTo()
            }

            'z' -> {
                path.close()
            }
        }

        a= p.read()
    }*/


