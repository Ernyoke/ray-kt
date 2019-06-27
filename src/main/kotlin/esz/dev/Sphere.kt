package esz.dev

import kotlin.math.sqrt

class Sphere(val center: Vec3, val radius: Double, val material: Material) : Hitable {
    override fun hit(r: Ray, tMin: Double, tMax: Double, rec: HitRecord): Boolean {
        val oc = r.origin() - center
        val a = r.direction().dot(r.direction())
        val b = 2.0 * oc.dot(r.direction())
        val c = oc.dot(oc) - radius * radius
        val discriminant = b * b - 4 * a * c
        if (discriminant > 0) {
            val t1 = (-b - sqrt(discriminant)) / (2.0 * a)
            return if (!isOnSphere(t1, tMin, tMax, rec, r)) {
                val t2 = (-b + sqrt(discriminant)) / (2.0 * a)
                isOnSphere(t2, tMin, tMax, rec, r)
            } else {
                true
            }
        }
        return false
    }

    private fun isOnSphere(t: Double, tMin: Double, tMax: Double, record: HitRecord, r: Ray): Boolean {
        if (t < tMax && t > tMin) {
            record.t = t
            record.p = r.pointAtParameter(record.t)
            record.normal = (record.p - center) / radius
            record.material = material
            return true
        }
        return false
    }

}
