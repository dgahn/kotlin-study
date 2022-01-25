package me.dgahn.kotlin150

import io.kotest.assertions.throwables.shouldThrowExactly
import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.shouldBe
import kotlin.io.path.Path
import kotlin.io.path.pathString
import kotlin.time.Duration
import kotlin.time.ExperimentalTime

@OptIn(ExperimentalTime::class)
@Suppress("ThrowingExceptionFromFinally", "MaxLineLength")
class StandardLibraryTest : FunSpec({

    context("Stable unsigned integer types") {
        // UInt, ULong, UByte, UShort 타입이 추가되었습니다.
    }

    // Java java.nio.file.Path의 확장함수로 Path()가 안정적인 버전이 되었습니다.
    context("Stable Path API") {
        test("Path를 통해서 디렉터리를 경로를 읽을 수 있다.") {
            val resourcePath = this.javaClass.classLoader.getResource(".").path
            val path = Path(resourcePath)
            path.pathString shouldBe resourcePath.dropLast(1)
        }
    }

    // 새로운 산수 라이브러리가 추가되었습니다.
    context("Floored division and the mod operator") {
        test("floorDiv") {
            // 나눴을 때 나머지가 생기면 몫으로부터 더 작은 값을 구한다.
            (-5).floorDiv(2) shouldBe -3
            // 나눴을 때 0에 가까운 값을 구한다.
            -5 / 2 shouldBe -2
        }

        test("mod") {
            // 나눴을 때 나머지를 구해준다.
            (-5) % 3 shouldBe -2
            // ??
            (-5).mod(3) shouldBe 1
        }
    }

    // 시간 단위 클래스인 Duration의 변화가 있습니다.
    context("Duration API changes") {
        test("Duration 클래스의 변화") {
            // 내부적인 값은 Double 대신 Long을 사용한다.
            val zero = Duration.ZERO
            // Double로 표현한 API
            // zero.inMinutes
            // Long으로 표현하기 위한 함수가 추가되었습니다.

            zero.inWholeMinutes shouldBe 0
            // 1.5.0에서는 아래 API를 사용하라고 했지만 1.6.0에서 deprecated 됨
            Duration.microseconds(10)
        }
    }

    // 새로운 컬렉션 함수가 추가되었습니다.
    context("New collections function firstNotNullOf()") {
        test("firstNotNullOf()") {
            val data = listOf("Kotlin", "1.5")
            // 첫번째로 null이 아닌 것을 가져온다.
            data.firstNotNullOf(String::toDoubleOrNull) shouldBe 1.5
            data.firstNotNullOf { it.toDouble() } shouldBe 1.5
            listOf("kotlin").firstNotNullOfOrNull(String::toDoubleOrNull) shouldBe null
        }
    }

    // 유니코드에 따라 Char을 가져올 수 있는 새로운 API가 도입되었습니다.
    context("New API for getting a char category now available in multiplatform code") {
        test("isXXXX()") {
            // 문자인지 체크합니다.
            'a'.isLetter() shouldBe true
            // 숫자인지 체크합니다.
            '1'.isDigit() shouldBe true
            // 숫자 또는 문자인지 테크합니다.
            'a'.isLetterOrDigit() shouldBe true
            '1'.isLetterOrDigit() shouldBe true

            'a'.isLowerCase() shouldBe true
            'A'.isUpperCase() shouldBe true
            'A'.isTitleCase() shouldBe true
        }
    }

    // String 값을 boolean으로 변경하는 새로운 함수가 추가되었습니다.
    context("Strict version of String?.toBoolean()") {
        test("toBoolean") {
            "true".toBoolean() shouldBe true
            "1".toBoolean() shouldBe false
            "True".toBoolean() shouldBe true
        }

        test("toBooleanStrict()") {
            "true".toBooleanStrict() shouldBe true
            "false".toBooleanStrict() shouldBe false
            shouldThrowExactly<IllegalArgumentException> { "1".toBooleanStrict() }
            shouldThrowExactly<IllegalArgumentException> { "True".toBooleanStrict() }
        }

        test("toBooleanStrictOrNull()") {
            "true".toBooleanStrictOrNull() shouldBe true
            "false".toBooleanStrictOrNull() shouldBe false
            "1".toBooleanStrictOrNull() shouldBe null
            "True".toBooleanStrictOrNull() shouldBe null
        }
    }
})
