package esz.dev

interface Hitable {
    fun hit(r: Ray, tMin: Double, tMax: Double, rec: HitRecord): Boolean
}
