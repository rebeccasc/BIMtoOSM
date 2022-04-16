package de.rebsc.bimtoosm.utils.geometry

import de.rebsc.bimtoosm.utils.math.Vector3D

class Geometry {

    companion object {

        /**
         * Checks if point is located inside a polygon using Jordan curve theorem
         * @param point to check if located in polygon
         * @param polygon
         * @return true or false whether located in polygon or not
         */
        fun isInsidePolygon(point: Vector3D, polygon: List<Vector3D>): Boolean {
            var isInside = false
            for (i in polygon.indices) {
                val j = (i + 1) % polygon.size
                if (polygon[i].y < point.y && polygon[j].y >= point.y || polygon[j].y < point.y && polygon[i].y >= point.y) {
                    if ((point.y - polygon[i].y) * (polygon[j].x - polygon[i].x) < (point.x - polygon[i].x) * (polygon[j].y - polygon[i].y))
                    {
                        isInside = !isInside
                    }
                }
            }
            return isInside
        }
    }
}