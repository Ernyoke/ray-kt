package esz.dev

import java.io.File
import kotlin.math.sqrt
import kotlin.random.Random

fun randomInUnitSphere(): Vec3 {
    var p: Vec3
    do {
        p = 2.0 * Vec3(Random.nextDouble(), Random.nextDouble(), Random.nextDouble()) - Vec3(1.0, 1.0, 1.0)
    } while (p.squaredLength() >= 1)
    return p
}

fun color(r: Ray, world: Hitable, depth: Int): Vec3 {
    val record = HitRecord()
    return if (world.hit(r, 0.0001, Double.MAX_VALUE, record)) {
        val scatterRecord = record.material?.scatter(r, record)
        return if (depth < 50 && scatterRecord?.res == true) {
            scatterRecord.attenuation * color(scatterRecord.scattered, world, depth + 1)
        } else {
            Vec3()
        }
    } else {
        val unitDirection = Vec3.unitVector(r.direction())
        val t = 0.5 * (unitDirection.y + 1.0)
        (1.0 - t) * Vec3(1.0, 1.0, 1.0) + t * Vec3(0.5, 0.7, 1.0)
    }
}

fun main(args: Array<String>) {
    val nx = 200
    val ny = 100
    val ns = 100

    File("ray.ppm").printWriter().use {
        it.println("P3")
        it.println("$nx $ny")
        it.println(255)
        val world = HitableList(
            listOf(
                Sphere(Vec3(0.0, 0.0, -1.0), 0.5, Lambertian(Vec3(0.8, 0.3, 0.3))),
                Sphere(Vec3(0.0, -100.5, -1.0), 100.0, Lambertian(Vec3(0.8, 0.8, 0.0))),
                Sphere(Vec3(1.0, 0.0, -1.0), 0.5, Metal(Vec3(0.8, 0.6, 0.3), 0.3)),
                Sphere(Vec3(-1.0, 0.0, -1.0), 0.5, Metal(Vec3(0.8, 0.8, 0.8), 1.0))
            )
        )
        val camera = Camera()
        for (j in ny - 1 downTo 0) {
            for (i in 0 until nx) {
                var col = Vec3(0.0, 0.0, 0.0)
                for (s in 0 until ns) {
                    val u = (i.toDouble() + Random.nextDouble()) / nx.toDouble()
                    val v = (j.toDouble() + Random.nextDouble()) / ny.toDouble()
                    var r = camera.getRay(u, v)
                    val p = r.pointAtParameter(2.0)
                    col += color(r, world, 0)
                }
                col /= ns.toDouble()
                col = Vec3(sqrt(col.x), sqrt(col.y), sqrt(col.z))
                val ir = (255.99 * col.x).toInt()
                val ig = (255.99 * col.y).toInt()
                val ib = (255.99 * col.z).toInt()
                it.println("$ir $ig $ib")
            }
        }
    }
}

