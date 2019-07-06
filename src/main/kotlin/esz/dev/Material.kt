package esz.dev

import kotlin.math.sqrt

abstract class Material {
    abstract fun scatter(rayIn: Ray, record: HitRecord): ScatterRecord

    protected fun reflect(v: Vec3, n: Vec3): Vec3 = v - 2 * v.dot(n) * n

    protected fun refract(v: Vec3, n: Vec3, niOverNt: Double): Pair<Boolean, Vec3> {
        val uv = Vec3.unitVector(v)
        val dt = uv.dot(n)
        val discriminant = 1.0 - niOverNt * (1 - dt * dt)
        return if (discriminant > 0)
            Pair(true, niOverNt * (uv - n * dt) - n * sqrt(discriminant))
        else Pair(false, Vec3())
    }
}
