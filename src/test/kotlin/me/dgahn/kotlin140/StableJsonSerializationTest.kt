package me.dgahn.kotlin140

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.registerKotlinModule
import io.kotest.core.spec.style.FunSpec
import kotlinx.serialization.Serializable
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

@Serializable
data class Project(val name: String, val language: String, val number: Int)

class StableJsonSerializationTest : FunSpec({

    val mapper = ObjectMapper().registerKotlinModule()

    fun outputRunningTime(name: String, block: () -> Unit) {
        val start = System.currentTimeMillis()
        block.invoke()
        val end = System.currentTimeMillis()
        println("$name : ${end - start}")
    }

    test("Json으로 직렬화할 수 있다.") {
        val totalList = listOf(1_000_000, 40_000, 1)
        totalList.forEach { total ->
            println("total : $total")
            val dataList = (1..total).map {
                Project(it.toString(), it.toString(), it)
            }

            outputRunningTime("kotlinx") {
                dataList.forEach {
                    Json.encodeToString(it)
                }
            }

            outputRunningTime("jackson") {
                dataList.forEach {
                    mapper.writeValueAsString(it)
                }
            }
        }
    }
})
