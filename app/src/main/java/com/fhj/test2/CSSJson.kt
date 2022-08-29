package com.fhj.test2

import java.util.regex.Matcher
import java.util.regex.Pattern

object CSSJson {
    var base = this


    fun String.trim(): String {
        return this.replace(Regex("/^\\s+|\\s+\$/g"), "")
    }


    var selX = "/([^\\s\\;\\{\\}][^\\;\\{\\}]*)\\{/g"
    var endX = "/\\}/g"
    var lineX = "/([^\\;\\{\\}]*)\\;/g"
    var commentX = "/\\/\\*[\\s\\S]*?\\*\\//g"
    var lineAttrX = "/([^\\:]+):([^\\;]*);/"

    // This is used, a concatenation of all above. We use alternation to
    // capture.
    var altX = "(\\/\\*[\\s\\S]*?\\*\\/)|([^\\s\\;\\{\\}][^\\;\\{\\}]*(?=\\{))|(\\})|([^\\;\\{\\}]+\\;(?!\\s*\\*\\/))"

    // Capture groups
    var capComment = 1
    var capSelector = 2
    var capEnd = 3
    var capAttr = 4

//    val pattern = Regex(altX).toPattern()
val pattern = Pattern.compile(altX,Pattern.MULTILINE.or(Pattern.CASE_INSENSITIVE))
    /**
     * Input is css string and current pos, returns JSON object
     *
     * @param cssString
     *            The CSS string.
     * @param args
     *            An optional argument object. ordered: Whether order of
     *            comments and other nodes should be kept in the output. This
     *            will return an object where all the keys are numbers and the
     *            values are objects containing "name" and "value" keys for each
     *            node. comments: Whether to capture comments. split: Whether to
     *            split each comma separated list of selectors.
     */
    fun toJSON(cssString: String,matcherArg: Matcher?=null): CSSNode {
//            var node = {
//                "children"={},
//                "attributes"={}
//            }

        var node = CSSNode()
        var count = 0;

/*            if (typeof args == 'undefined') {
            var args = {
                    ordered: false,
                    comments: false,
                    stripComments: false,
                    split: false
            };
        }*/

/*
            if (args.stripComments) {
                args.comments = false;
                cssString = cssString.replace(commentX, '');
            }
*/
//        Pattern.compile("", Pattern.CASE_INSENSITIVE.or(Pattern.COMMENTS))

        lateinit var matcher:Matcher
        if(matcherArg==null)
        {
             matcher =pattern.matcher(cssString)
        }
        else
        {
            matcher = matcherArg
        }

        var match = matcher.run {
            this.matches()
            val arr = ArrayList<String?>()
            for (i in 0..groupCount()) {
                try {
                    println(group(i))
                    arr.add(group(i))
                } catch (e: Exception) {

                }
            }
            arr

        }
        println(match.size)
        while (match.size != 0) {
            if (match[capSelector] != null) {
                // New node, we recurse
                var name = match[capSelector]?.trim()
                // This will return when we encounter a closing brace
                var newNode = base.toJSON(cssString,matcher)

                var bits = name?.split(',')

                if(bits!=null)
                {
                    for (i in bits) {
                        var sel = i.trim()
                        if (sel in (node.children)) {
                            for (att in newNode.attributes) {
                                if(node.children.contains(sel))
                                {
                                    node.children.put(sel,CSSNode().apply {
                                        attributes.put(att.key,newNode.attributes.get(att.key)?:"")
                                    })
                                }
                                else
                                {
                                    node.children.get(sel)?.attributes?.put(att.key,newNode.attributes.get(att.key)?:"")
                                }
                            }
                        } else {
                            node.children.put(sel,newNode)
                        }
                    }
                }

            } else if (match[capEnd] != null) {
                // Node has finished
                return node
            } else if (match.size>4&&match[capAttr] != null) {
                var line = match[capAttr]?.trim()
                val matcher2 = Regex(lineAttrX).toPattern().matcher(line)
               var attr =  matcher2.run {
                    find()
                    val arr = ArrayList<String?>()
                    for (i in 0 until groupCount()) {
                        try {
                            arr.add(group(i))
                        } catch (e: Exception) {

                        }
                    }
                    arr
                }

                if (attr.size!=0) {
                    // Attribute
                    var name = attr[1]?.trim()
                    var value = attr[2]?.trim()?:""
                    if(name!=null)
                        node.attributes[name] = value

                }
            }
            match = matcher.run {
                this.matches()
                val arr = ArrayList<String?>()
                for (i in 0 until groupCount()) {
                    try {
                        arr.add(group(i))
                    } catch (e: Exception) {

                    }
                }
                arr
            }
        }

        return node;
    }
}