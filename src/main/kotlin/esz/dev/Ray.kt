package esz.dev

class Ray(val a: Vec3 = Vec3(), val b: Vec3 = Vec3()) {
    fun origin(): Vec3 = a

    fun direction(): Vec3 = b

    fun pointAtParameter(t: Double) = a + b * t
}
