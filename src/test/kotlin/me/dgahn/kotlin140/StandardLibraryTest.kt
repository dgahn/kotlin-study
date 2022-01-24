package me.dgahn.kotlin140

import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.collections.shouldHaveSize
import io.kotest.matchers.collections.shouldNotContain
import io.kotest.matchers.shouldBe
import io.kotest.matchers.shouldNotBe

@Suppress("ThrowingExceptionFromFinally", "MaxLineLength")
class StandardLibraryTest : FunSpec({

    context("Common exception processing API") {
        // 예외처리에 대한 확장함수가 추가되었습니다.
        test("Throwable.stackTraceToString()") {
            val exception = KotlinNullPointerException()
            val actual = exception.stackTraceToString()
            actual::class.simpleName shouldBe "String"
        }

        // 한번에 여러개의 예외를 발생시킬 수 없기 때문에 특정 예외를 억제시키고 발생시킨 예외에 억제된 예외에 대한 정보를 담을 수 있다.
        test("Throwable.addSuppressed()") {
            val first = KotlinNullPointerException()
            val second = IllegalArgumentException()

            try {
                throw first
            } finally {
                second.addSuppressed(first)
                throw second
            }
        }
    }

    context("New functions for arrays and collections") {
        // 새로운 컬렉션 함수들이 추가되었다.
        test("setOfNotNull()은 Null을 제외하고 set을 만든다.") {
            val actual = setOfNotNull(null, 1, 2, 0, null)
            actual shouldNotContain null
        }

        // 시퀀스는 일종의 코루틴이다.
        // 제너레이터
        /*
        fun genenate(){
           var i = 0
           while(true){
             yield getURLData("/path/i++");
           }
        }
        log(generate()); 1
        log(generate());2
        log(generate());3
        log(generate());4
        log(generate());5
        log(generate());6
        배열로 자연수들어봐
               */
        // 지연 계산을
        test("shuffled()는 시퀀스의 값을 무작위로 섞을 수 있다.") {
            val actual = (0..50).asSequence()
                .map { it * 2 }
                .shuffled()
                .take(5)
                .toList()

            actual shouldHaveSize 5
        }

        test("onEach()을 index와 함께 사용할 수 있다.") {
            var i = 0
            val list = listOf("a", "b", "c", "d")
            val onEachList = list.onEachIndexed { index, element ->
                index shouldBe i
                element shouldBe list[i]
                i++
            }
            onEachList shouldBe list
        }

        test("flatMap()을 index와 함께 사용할 수 있다.") {
            var i = 0
            val list = listOf("hello", "kot", "lin", "world")
            val kotlin = list.flatMapIndexed { index, item ->
                index shouldBe i
                i++
                if (index in 1..2) item.toList() else emptyList()
            }
            kotlin shouldBe listOf('k', 'o', 't', 'l', 'i', 'n')
        }

        test("randomOrNull(), reduceOrNull(), reduceIndexedOrNull()은 null을 리턴할 수 있다.") {
            val empty = emptyList<Int>()
            empty.randomOrNull() shouldBe null
            empty.reduceOrNull { a, b -> a + b } shouldBe null
            empty.reduceIndexedOrNull { i, a, b -> a + b } shouldBe null
        }

        test("fold와 reduce의 중간 값을 모두 저장하는 running 함수가 추가되었습니다.") {
            val numbers = listOf(0, 1, 2, 3, 4, 5)
            val runningReduceSum = numbers.runningReduce { sum, item -> sum + item }
            val runningFoldSum = numbers.runningFold(0) { sum, item -> sum + item }

            val expected = listOf(0, 1, 3, 6, 10, 15)
            runningReduceSum shouldBe expected
            runningFoldSum shouldBe listOf(0) + expected
        }

        test("객체의 특정 요소를 더할 수 있는 sumOf() 함수가 추가되었습니다.") {
            val list = listOf(
                Triple("a", 1, "1"),
                Triple("b", 2, "2"),
                Triple("c", 3, "3")
            )
            val actual = list.sumOf { it.second }
            val expected = 6
            actual shouldBe expected
        }

        test("객체의 특성 요소를 비교할 수 있는 minOf(), maxOf(), minOfWith(), maxOfWith(), 그리고 + OrNull 함수가 추가되었습니다.") {
            val list = listOf(
                Triple("a", 1, "1"),
                Triple("b", 2, "2"),
                Triple("c", 3, "3")
            )

            val actualMin = list.minOf { it.second }
            val expectedMin = 1
            actualMin shouldBe expectedMin

            val actualMax = list.maxOf { it.second }
            val expectedMax = 3
            actualMax shouldBe expectedMax

            val actualMinWith = list.minOfWith({ a, b -> a.second compareTo b.second }) { it }
            val expectedMinWith = Triple("a", 1, "1")
            actualMinWith shouldBe expectedMinWith

            val actualMaxWith = list.maxOfWith({ a, b -> a.second compareTo b.second }) { it }
            val expectedMaxWith = Triple("c", 3, "3")
            actualMaxWith shouldBe expectedMaxWith

            val empty = emptyList<Triple<String, Int, String>>()
            empty.minOfOrNull { it.second } shouldBe null
            empty.maxOfOrNull { it.second } shouldBe null
            empty.minOfWithOrNull({ a, b -> a.second compareTo b.second }) { it } shouldBe null
            empty.maxOfWithOrNull({ a, b -> a.second compareTo b.second }) { it } shouldBe null
        }

        test("array에 shuffle(), onEach(),associateWith(), associateWithTo(), reverse(), sortDescending(), sort(), sortWith() 함수가 추가되었습니다.") {
            val array = arrayOf(1, 2, 3, 4, 5, 6, 7, 8, 9, 10)
            array.shuffle()
            array shouldNotBe arrayOf(1, 2, 3, 4, 5, 6, 7, 8, 9, 10)
            array.sort()
            array.onEach { println(it) } shouldBe array
            array.associateWith { it + 1 }.values shouldBe array.map { it + 1 }
            val mutableMap = mutableMapOf<Int, Int>()
            array.associateWithTo(mutableMap) { it + 1 } shouldBe mutableMap
            array.reverse()
            array shouldBe arrayOf(10, 9, 8, 7, 6, 5, 4, 3, 2, 1)
            array.reverse()
            array.sortDescending()
            array shouldBe arrayOf(10, 9, 8, 7, 6, 5, 4, 3, 2, 1)
            // 기준값이 비교 값보다 클 때 true 임으로 내림차순
            array.sortWith { a, b -> b compareTo a }
            array shouldBe arrayOf(10, 9, 8, 7, 6, 5, 4, 3, 2, 1)
        }

        test("CharArray와 ByteArray를 String으로 변경할 수 있습니다.") {
            val str = "kotlin"
            str.toCharArray().concatToString() shouldBe str
            str.toByteArray().decodeToString() shouldBe str
        }

        // array buffer의 전략을 따라감
        // array deque에 버퍼 사이즈를 정해서 넣는 것이 좋다.
        // 갑자기 터질 수 있음.
        test("ArrayDeque 클래스가 추가되었습니다.") {
            val deque = ArrayDeque(listOf(1, 2, 3))

            deque.addFirst(0)
            deque.addLast(4)

            deque.first() shouldBe 0
            deque.last() shouldBe 4

            deque.removeFirst()
            deque.removeLast()

            deque.first() shouldNotBe 0
            deque.last() shouldNotBe 4
        }
    }

    // 문자열 덧셈
    context("Functions for string manipulations") {
        test("StringBuilder에 set(), setRange(), deleteAt(), deleteRange(), appendRange() 함수등이 추가되었습니다.") {
            val str = StringBuilder("Bye Kotlin 1.3.72").apply {
                deleteRange(0, 3)
                this.toString() shouldBe " Kotlin 1.3.72"
                insertRange(0, "Hello", 0, 5)
                this.toString() shouldBe "Hello Kotlin 1.3.72"
                set(15, '4')
                this.toString() shouldBe "Hello Kotlin 1.4.72"
                setRange(17, 19, "0")
            }.toString()

            str shouldBe "Hello Kotlin 1.4.0"
        }

        test("Appnedable.appendLine()은 appendln()을 대체한다.") {
            buildString {
                appendLine("Hello,")
                appendLine("world")
            } shouldBe """Hello,
                |world
                |
            """.trimMargin()
        }
    }

    context("Bit operations") {
        test("Bit 연산자인 countOneBits(), countTrailingZeroBits(), takeHighestOneBit()등이 추가되었습니다.") {
            // radix는 String을 몇진수로 변경할지에 대한 값
            val number = "1010000".toInt(radix = 2)
            // 이진 표현에서 설정된 비트 수를 계산합니다. 1을 센다.
            number.countOneBits() shouldBe 2
            // 0이 연속적인 최하위 비트수를 센다. 101 뒤에 0이 4개라서 4가 나온다.
            number.countTrailingZeroBits() shouldBe 4
            // 가장 큰 비트의 숫자를 반환한다. 1000000
            number.takeHighestOneBit() shouldBe 64
        }
    }

    context("Deprecations") {
        fun Double.convertShort() = this.toInt().toShort()
        fun Double.convertByte() = this.toInt().toByte()
        fun Float.convertShort() = this.toInt().toShort()
        fun Float.convertByte() = this.toInt().toByte()

        /**
         * toShort() and toByte() of Double and Float
         * 실수 자료형을 더 작은 정수 자료형으로 변경하는데 좁은 값 범위로 인한 예상하지 않은 에러가 발생하여 사용중단되었습니다.
         */
        test("실수형의 toShort(), toByte()은 사용중단 되었다.") {
//            1.0.toShort()
//            1.0.toByte()
//            1.0F.toShort()
//            1.0F.toByte()
        }

        test("실수형을 toShort(), toByte()를 하기위해서는 Int형으로 변경하고 하면 가능하다.") {
            val doubleNumber = 1.0
            val floatNumber = 1.0F

            val shortNumber: Short = 1
            doubleNumber.toInt().toShort() shouldBe shortNumber
            floatNumber.toInt().toShort() shouldBe shortNumber

            doubleNumber.convertShort() shouldBe shortNumber
            floatNumber.convertShort() shouldBe shortNumber

            val byteNumber: Byte = 1
            doubleNumber.toInt().toByte() shouldBe byteNumber
            floatNumber.toInt().toByte() shouldBe byteNumber

            doubleNumber.convertByte() shouldBe byteNumber
            floatNumber.convertByte() shouldBe byteNumber
        }
        /**
         * contains(), indexOf(), and lastIndexOf() on floating-point arrays
         * Float 배열의 확장함수인 contains(), indexOf(), and lastIndexOf() 사용중단되었습니다.
         * IEE 754 표준을 따르지 않은 문제가 있습니다. kotlin은 Total order equality로 계산이 됩니다. Total order equality는 IEE 754와 반대입니다.
         * NaN == NaN -> false
         * -0.0 == 0.0 -> true
         */

        test("float array의 contains(), indexOf(), lastIndexOf()는 사용할 수 없습니다.") {
            val floatList: List<Float> = listOf(0.0F, Float.NaN)
            val floatArray: FloatArray = floatArrayOf(0.0F, Float.NaN)

            floatList.contains(Float.NaN) shouldBe true
            floatList.indexOf(Float.NaN) shouldBe 1
            floatList.lastIndexOf(Float.NaN) shouldBe 1

            // 사용불가 val a:Double = 0.0/0.0   Double.NaN == Double.NaN

//            floatArray.contains(Float.NaN) shouldBe true
//            floatArray.indexOf(Float.NaN) shouldBe 1
//            floatArray.lastIndexOf(Float.NaN) shouldBe 1
        }
        // 20220101123456 <- int overflow
        /**
         * min() and max() collection functions
         * 컬렉션 함수인 min(), max() 함수는 null을 반환할 수 있기 때문에 사용중단되었고 minOrNull(), maxOrNull()로 대체되었습니다.
         */
        test("min(), max()는 xxOrNull()로 사용가능합니다.") {
            val emptyList = emptyList<Int>()

            emptyList.minOrNull() shouldBe null
            emptyList.maxOrNull() shouldBe null
        }
    }
})
