package esz.dev

interface Material {
    fun scatter(rayIn: Ray, record: HitRecord): ScatterRecord
}
