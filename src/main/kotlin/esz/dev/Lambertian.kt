package esz.dev

class Lambertian(val albedo: Vec3) : Material() {
    override fun scatter(rayIn: Ray, record: HitRecord): ScatterRecord {
        val target = record.p + record.normal + randomInUnitSphere()
        val scattered = Ray(record.p, target - record.p)
        return ScatterRecord(albedo, scattered, true)
    }

}
