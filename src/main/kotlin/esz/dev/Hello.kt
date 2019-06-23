package esz.dev

import java.io.File

fun hitSphere(center: Vec3, radius: Double, r: Ray): Boolean {
    val oc = r.origin() - center
    val a = oc.dot(r.direction())
    val b = 2.0 * oc.dot(r.direction())
    val c = oc.dot(oc) - radius * radius
    val discriminant = b * b - 4 * a * c
    return discriminant > 0
}

fun color(r: Ray): Vec3 {
    if (hitSphere(Vec3(0.0, 0.0, -1.0), 0.5, r)) {
        return Vec3(1.0, 0.0, 0.0)
    }
    val unitDirection = Vec3.unitVector(r.direction())
    val t = 0.5 * (unitDirection.y + 1.0)
    return (1.0 - t) * Vec3(1.0, 1.0, 1.0) + t * Vec3(0.5, 0.7, 1.0)
}

fun main(args: Array<String>) {
    val nx: Int = 200
    val ny: Int = 100

    File("ray.ppm").printWriter().use {
        it.println("P3")
        it.println("$nx $ny")
        it.println(255)
        val lowerLeftCorner = Vec3(-2.0, -1.0, -1.0)
        val horizontal = Vec3(4.0, 0.0, 0.0)
        val vertical = Vec3(0.0, 2.0, 0.0)
        val origin = Vec3(0.0, 0.0, 0.0)
        for (j in ny - 1 downTo 0) {
            for (i in 0 until nx) {
                val u = i.toDouble() / nx.toDouble()
                val v = j.toDouble() / ny.toDouble()
                val r = Ray(origin, lowerLeftCorner + u * horizontal + v * vertical)
                val col = color(r)
                val ir = (255.99 * col.x).toInt()
                val ig = (255.99 * col.y).toInt()
                val ib = (255.99 * col.z).toInt()
                it.println("$ir $ig $ib")
            }
        }
    }
}

