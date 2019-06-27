package esz.dev

class Camera(
    private val lowerLeftCorner: Vec3 = Vec3(-2.0, -1.0, -1.0),
    private val horizontal: Vec3 = Vec3(4.0, 0.0, 0.0),
    private val vertical: Vec3 = Vec3(0.0, 2.0, 0.0),
    private val origin: Vec3 = Vec3(0.0, 0.0, 0.0)
) {
    fun getRay(u: Double, v: Double): Ray = Ray(origin, lowerLeftCorner + u * horizontal + v * vertical - origin)
}
