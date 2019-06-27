package esz.dev

data class HitRecord(
    var t: Double = 0.0,
    var p: Vec3 = Vec3(),
    var normal: Vec3 = Vec3(),
    var material: Material? = null
)
