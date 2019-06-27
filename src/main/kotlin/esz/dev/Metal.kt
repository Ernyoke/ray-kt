package esz.dev

class Metal(val albedo: Vec3, f: Double) : Material {
    var fuzz: Double = f

    init {
        if (fuzz >= 1) {
            fuzz = 1.0
        }
    }

    override fun scatter(rayIn: Ray, record: HitRecord): ScatterRecord {
        val reflected = reflect(Vec3.unitVector(rayIn.direction()), record.normal)
        val scattered = Ray(record.p, reflected + fuzz * randomInUnitSphere())
        return ScatterRecord(albedo, scattered, scattered.direction().dot(record.normal) > 0)
    }

    private fun reflect(v: Vec3, n: Vec3): Vec3 = v - 2 * v.dot(n) * n
}
