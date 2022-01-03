package me.dgahn.kotlin140

import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.shouldBe

class StandardLibraryTest : FunSpec({

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
            // 사용불가
//            floatArray.contains(Float.NaN) shouldBe true
//            floatArray.indexOf(Float.NaN) shouldBe 1
//            floatArray.lastIndexOf(Float.NaN) shouldBe 1
        }

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
