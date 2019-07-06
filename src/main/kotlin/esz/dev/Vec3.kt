package esz.dev

import java.lang.IndexOutOfBoundsException
import kotlin.math.sqrt

class Vec3(val x: Double = 0.0, val y: Double = 0.0, val z: Double = 0.0) {
    constructor(other: Vec3) : this(other.x, other.y, other.z)

    operator fun unaryPlus() = this

    operator fun unaryMinus() = Vec3(-x, -y, -z)

    operator fun plus(other: Vec3) = Vec3(x + other.x, y + other.y, z + other.z)

    operator fun minus(other: Vec3) = Vec3(x - other.x, y - other.y, z - other.z)

    operator fun times(other: Vec3) = Vec3(x * other.x, y * other.y, z * other.z)

    operator fun times(other: Double) = Vec3(x * other, y * other, z * other)

    operator fun div(other: Vec3) = Vec3(x / other.x, y / other.y, z / other.z)

    operator fun div(other: Double) = Vec3(x / other, y / other, z / other)

    operator fun get(index: Int): Double {
        return when (index) {
            0 -> x
            1 -> y
            2 -> z
            else -> throw IndexOutOfBoundsException()
        }
    }

    fun dot(other: Vec3): Double = x * other.x + y * other.y + z * other.z

    fun cross(other: Vec3): Vec3 =
        Vec3(y * other.z - z * other.y, -(x * other.z - z * other.x), x * other.y - y * other.x)

    fun squaredLength(): Double = x * x + y * y + z * z

    fun length(): Double = sqrt(squaredLength())

    fun makeUnitVector(): Vec3 {
        val k = 1.0 / squaredLength()
        return Vec3(x * k, y * k, z * k)
    }

    companion object {
        fun unitVector(v: Vec3): Vec3 = v / v.length()
    }
}

operator fun Double.times(v: Vec3): Vec3 = Vec3(this * v.x, this * v.y, this * v.z)
