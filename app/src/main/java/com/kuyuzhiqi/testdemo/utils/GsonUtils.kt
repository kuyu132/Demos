package com.kuyuzhiqi.testdemo.utils

import android.content.Context
import com.google.gson.*
import java.io.BufferedReader
import java.io.InputStreamReader

object GsonUtils {

    var gson: Gson
        private set
    private var sJsonParser: JsonParser

    init {
        gson = GsonBuilder().setLenient()//返回的字符串中可能包含特定的字符,如: NUL \0 ,会导致解释出错. 所以尝试使用 Lenient mode
                .create()
        sJsonParser = JsonParser()
    }

    fun toJson(o: Any): String {
        return gson.toJson(o)
    }

    fun convertStringToJsonObject(jsString: String): JsonObject {
        return sJsonParser.parse(jsString).asJsonObject
    }

    fun convertStringToJsonArrsay(jsString: String): JsonArray {
        return sJsonParser.parse(jsString).asJsonArray
    }

    fun <T> mapToJson(map: Map<String, T>): String {
        return gson.toJson(map)
    }

    fun <T> jsonToMap(jsString: String): java.util.HashMap<*, *>? {
        return gson.fromJson(jsString,HashMap::class.java)
    }

    fun loadJsonFromLocal(context: Context, assetFileName: String): String {
        return BufferedReader(InputStreamReader(context.assets.open(assetFileName), "utf-8"))
                .use { it.readText() }
    }
}