package esz.dev

import kotlin.math.PI
import kotlin.math.tan

class Camera(lookFrom: Vec3, lookAt: Vec3, vup: Vec3, vFov: Double, aspect: Double) {
    private val origin: Vec3
    private val lowerLeftCorner: Vec3
    private val horizontal: Vec3
    private val vertical: Vec3

    init {
        val theta = vFov * PI / 180.0
        val halfHeight = tan(theta / 2.0)
        val halfWidth = aspect * halfHeight
        origin = lookFrom
        val w = Vec3.unitVector(lookFrom - lookAt)
        val u = Vec3.unitVector(vup.cross(w))
        val v = w.cross(u)
        lowerLeftCorner = origin - halfWidth * u - halfHeight * v - w
        horizontal = 2.0 * halfWidth * u
        vertical = 2.0 * halfHeight * v
    }

    fun getRay(u: Double, v: Double): Ray = Ray(origin, lowerLeftCorner + u * horizontal + v * vertical - origin)
}
