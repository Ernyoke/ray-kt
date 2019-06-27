package esz.dev

class HitableList(val list: List<Hitable>) : Hitable {
    override fun hit(r: Ray, tMin: Double, tMax: Double, rec: HitRecord): Boolean {
        val tempRecord = HitRecord()
        var hitAnything = false
        var closesSoFar = tMax
        for (hitable in list) {
            if (hitable.hit(r, tMin, closesSoFar, tempRecord)) {
                hitAnything = true
                closesSoFar = tempRecord.t
                rec.t = tempRecord.t
                rec.p = tempRecord.p
                rec.normal = tempRecord.normal
                rec.material = tempRecord.material
            }
        }
        return hitAnything
    }

}
