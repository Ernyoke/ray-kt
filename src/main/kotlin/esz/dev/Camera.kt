package esz.dev

import kotlin.math.PI
import kotlin.math.tan
import kotlin.random.Random

class Camera(
    lookFrom: Vec3,
    lookAt: Vec3,
    vup: Vec3,
    vFov: Double,
    aspect: Double,
    aperture: Double,
    focusDistance: Double
) {
    private val origin: Vec3
    private val lowerLeftCorner: Vec3
    private val horizontal: Vec3
    private val vertical: Vec3
    private val lensRadius: Double = aperture / 2.0
    private val u: Vec3
    private val v: Vec3
    private val w: Vec3

    init {
        val theta = vFov * PI / 180.0
        val halfHeight = tan(theta / 2.0)
        val halfWidth = aspect * halfHeight
        origin = lookFrom
        w = Vec3.unitVector(lookFrom - lookAt)
        u = Vec3.unitVector(vup.cross(w))
        v = w.cross(u)
        lowerLeftCorner = origin - halfWidth * focusDistance * u - halfHeight * focusDistance * v - focusDistance * w
        horizontal = 2.0 * halfWidth * focusDistance * u
        vertical = 2.0 * halfHeight * focusDistance * v
    }

    fun getRay(s: Double, t: Double): Ray {
        val rd: Vec3 = lensRadius * randomInUnitDisk()
        val offset: Vec3 = u * rd.x + v * rd.y
        return Ray(origin + offset, lowerLeftCorner + s * horizontal + t * vertical - origin - offset)
    }

    private fun randomInUnitDisk(): Vec3 {
        var p: Vec3
        do {
            p = 2.0 * Vec3(Random.nextDouble(), Random.nextDouble(), 0.0) - Vec3(1.0, 1.0, 0.0)
        } while (p.dot(p) >= 1.0)
        return p
    }
}
