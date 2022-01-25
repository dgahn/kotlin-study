package me.dgahn.kotlin1430

import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.shouldBe

@Suppress("ThrowingExceptionFromFinally", "MaxLineLength")
class StandardLibraryTest : FunSpec({

    // 로케일에 구애 받지 않은 upper/lowercasing text가 추가되었습니다.
    // 로케일이 적용되는 경우, 터키어일 때 "kotlin"이 터키어 "KOTLİN"이 됩니다.
    // 타이틀 케이스는 Character.getType(ch)에서 제공하는 유형이 TITLECASE_LETTER인 경우다.
    context("Locale-agnostic API for upper/lowercasing text") {
        test("String functions") {
            // 모두 대문자로 변경
            "Needs to be capitalized".uppercase() shouldBe "NEEDS TO BE CAPITALIZED"
            // 모두 소문자로 변경
            "Needs to be capitalized".lowercase() shouldBe "needs to be capitalized"
            // 첫번째만 대문자로 변경
            "needs to be capitalized".replaceFirstChar { it.uppercase() } shouldBe "Needs to be capitalized"
            // 첫번째만 소문자로 변경
            "Needs To Be Capitalized".replaceFirstChar { it.lowercase() } shouldBe "needs To Be Capitalized"
        }

        test("Char Functions") {
            "Needs to be capitalized".map { it.uppercaseChar() }.joinToString("") shouldBe "NEEDS TO BE CAPITALIZED"
            "Needs to be capitalized".map { it.uppercase() }.joinToString("") shouldBe "NEEDS TO BE CAPITALIZED"
            "Needs to be capitalized".map { it.lowercaseChar() }.joinToString("") shouldBe "needs to be capitalized"
            "Needs to be capitalized".map { it.lowercase() }.joinToString("") shouldBe "needs to be capitalized"
            "Needs to be capitalized".map { it.titlecaseChar() }.joinToString("") shouldBe "NEEDS TO BE CAPITALIZED"
            "Needs to be capitalized".map { it.titlecase() }.joinToString("") shouldBe "NEEDS TO BE CAPITALIZED"
        }
    }

    context("Clear Char-to-code and Char-to-digit conversions") {
        test("toInt()") {
            "4".toInt() shouldBe 4
            '4'.toInt() shouldBe 52 // 숫자 4가 아니라 UTF-16 표현으로 인지함
            '4'.digitToInt() shouldBe 4
            Char(52) shouldBe '4'
            '4'.code shouldBe 52
            '1'.digitToInt(2) shouldBe "1".toInt(2)
            '1'.digitToIntOrNull(2) shouldBe "1".toInt(2)
        }
    }
})
