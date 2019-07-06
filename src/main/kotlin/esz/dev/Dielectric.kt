package esz.dev

import java.lang.Math.pow
import kotlin.random.Random

class Dielectric(val refIdx: Double) : Material() {
    override fun scatter(rayIn: Ray, record: HitRecord): ScatterRecord {
        val outwardNormal: Vec3
        val reflected = reflect(rayIn.direction(), record.normal)
        val niOverNt: Double
        val cosine: Double
        if (rayIn.direction().dot(record.normal) > 0) {
            outwardNormal = -record.normal
            niOverNt = refIdx
            cosine = refIdx * rayIn.direction().dot(record.normal) / rayIn.direction().length()
        } else {
            outwardNormal = record.normal
            niOverNt = 1.0 / refIdx
            cosine = -(rayIn.direction().dot(record.normal) / rayIn.direction().length())
        }
        val refractResult = refract(rayIn.direction(), outwardNormal, niOverNt)
        val reflectProb = if (refractResult.first) {
            schlick(cosine, refIdx)
        } else {
            1.0
        }
        val scattered = if (Random.nextDouble() < reflectProb) {
            Ray(record.p, reflected)
        } else {
            Ray(record.p, refractResult.second)
        }
        return ScatterRecord(Vec3(1.0, 1.0, 1.0), scattered)
    }

    private fun schlick(cosine: Double, refIdx: Double): Double {
        val r0 = (1.0 - refIdx) / (1 + refIdx)
        val r = r0 * r0
        return r + (1 - r) * pow(1.0 - cosine, 5.0)
    }
}
