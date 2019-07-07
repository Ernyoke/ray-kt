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

fun randomScene(): Hitable {
    val hitableList = mutableListOf<Hitable>()
    hitableList.add(Sphere(Vec3(0.0, -1000.0, 0.0), 1000.0, Lambertian(Vec3(0.5, 0.5, 0.5))))
    for (a in -11..11) {
        for (b in -11..11) {
            val chooseMat = Random.nextDouble()
            val center = Vec3(a + 0.9 * Random.nextDouble(), 0.2, b + 0.9 * Random.nextDouble())
            if ((center - Vec3(4.0, 0.2, 0.0)).length() > 0.9) {
                if (chooseMat < 0.8) { // diffuse
                    hitableList.add(
                        Sphere(
                            center,
                            0.2,
                            Lambertian(
                                Vec3(
                                    Random.nextDouble() * Random.nextDouble(),
                                    Random.nextDouble() * Random.nextDouble(),
                                    Random.nextDouble() * Random.nextDouble()
                                )
                            )
                        )
                    )
                } else if (chooseMat < 0.95) { // metal
                    hitableList.add(
                        Sphere(
                            center,
                            0.2,
                            Metal(
                                Vec3(
                                    0.5 * (1.0 + Random.nextDouble() * Random.nextDouble()),
                                    0.5 * (1.0 + Random.nextDouble() * Random.nextDouble()),
                                    0.5 * (1.0 + Random.nextDouble() * Random.nextDouble())
                                ),
                                0.5 * Random.nextDouble()
                            )
                        )
                    )
                } else { // glass
                    hitableList.add(Sphere(center, 0.2, Dielectric(1.5)))
                }
            }
        }
    }
    hitableList.add(Sphere(Vec3(0.0, 1.0, 0.0), 1.0, Dielectric(1.5)))
    hitableList.add(Sphere(Vec3(-4.0, 1.0, 0.0), 1.0, Lambertian(Vec3(0.4, 0.2, 0.1))))
    hitableList.add(Sphere(Vec3(4.0, 1.0, 0.0), 1.0, Metal(Vec3(0.7, 0.6, 0.5), 0.0)))

    return HitableList(hitableList)
}

fun main() {
    val nx = 600
    val ny = 400
    val ns = 100

    File("ray.ppm").printWriter().use {
        it.println("P3")
        it.println("$nx $ny")
        it.println(255)
        val world = randomScene()
        val lookFrom = Vec3(12.0, 1.0, 3.0)
        val lookAt = Vec3(1.0, 0.7, -1.0)
        val distToFocus = (lookFrom - lookAt).length()
        val aperture = 0.25
        val camera = Camera(
            lookFrom,
            lookAt,
            Vec3(0.0, 1.0, 0.0),
            20.0,
            nx.toDouble() / ny.toDouble(),
            aperture,
            distToFocus
        )
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

