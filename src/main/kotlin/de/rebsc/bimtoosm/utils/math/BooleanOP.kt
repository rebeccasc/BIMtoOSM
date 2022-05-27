package de.rebsc.bimtoosm.utils.math

import com.menecats.polybool.Epsilon
import com.menecats.polybool.PolyBool
import com.menecats.polybool.helpers.PolyBoolHelper.*
import com.menecats.polybool.models.Polygon


class BooleanOP {

    companion object{

        /**
         * Performs boolean difference on polygon1 and polygon2
         * @param polygon1
         * @param polygon2
         * @return Boolean difference of polygon1 and polygon2
         */
        fun difference(polygon1: List<Vector3D>, polygon2: List<Vector3D>): List<Vector3D>{
            if (polygon1.isEmpty()) return ArrayList()
            if (polygon2.isEmpty()) return ArrayList()

            val eps: Epsilon = epsilon()

            val poly1Points = ArrayList<DoubleArray>()
            polygon1.forEach { point ->
                poly1Points.add(point(point.x, point.y))
            }
            val poly1PointsList = ArrayList<List<DoubleArray>>()
            poly1PointsList.add(poly1Points)

            val poly2Points = ArrayList<DoubleArray>()
            polygon2.forEach { point ->
                poly2Points.add(point(point.x, point.y))
            }
            val poly2PointsList = ArrayList<List<DoubleArray>>()
            poly2PointsList.add(poly2Points)

            val poly1 = Polygon(poly1PointsList)
            val poly2 = Polygon(poly2PointsList)
            val difference: Polygon = PolyBool.difference(
                eps,
                poly1,
                poly2
            )

            val result = ArrayList<Vector3D>()
            difference.regions.forEach{ region ->
                region.forEach { point ->
                    result.add(Vector3D(point[0], point[1], 0.0))
                }
            }
            return result
        }

        /**
         * Performs boolean intersection on polygon1 and polygon2
         * @param polygon1
         * @param polygon2
         * @return Boolean intersection of polygon1 and polygon2
         */
        fun intersect(polygon1: List<Vector3D>, polygon2: List<Vector3D>): List<Vector3D>{
            if (polygon1.isEmpty()) return ArrayList()
            if (polygon2.isEmpty()) return ArrayList()

            val eps: Epsilon = epsilon()

            val poly1Points = ArrayList<DoubleArray>()
            polygon1.forEach { point ->
                poly1Points.add(point(point.x, point.y))
            }
            val poly1PointsList = ArrayList<List<DoubleArray>>()
            poly1PointsList.add(poly1Points)

            val poly2Points = ArrayList<DoubleArray>()
            polygon2.forEach { point ->
                poly2Points.add(point(point.x, point.y))
            }
            val poly2PointsList = ArrayList<List<DoubleArray>>()
            poly2PointsList.add(poly2Points)

            val poly1 = Polygon(poly1PointsList)
            val poly2 = Polygon(poly2PointsList)
            val intersect: Polygon = PolyBool.intersect(
                eps,
                poly1,
                poly2
            )

            val result = ArrayList<Vector3D>()
            intersect.regions.forEach{ region ->
                region.forEach { point ->

                    result.add(Vector3D(point[0], point[1], 0.0))
                }
            }
            return result
        }

        /**
         * Performs boolean union on polygon1 and polygon2
         * @param polygon1
         * @param polygon2
         * @return Boolean union of polygon1 and polygon2
         */
        fun union(polygon1: List<Vector3D>, polygon2: List<Vector3D>): List<Vector3D>{
            if (polygon1.isEmpty()) return ArrayList()
            if (polygon2.isEmpty()) return ArrayList()

            val eps: Epsilon = epsilon()

            val poly1Points = ArrayList<DoubleArray>()
            polygon1.forEach { point ->
                poly1Points.add(point(point.x, point.y))
            }
            val poly1PointsList = ArrayList<List<DoubleArray>>()
            poly1PointsList.add(poly1Points)

            val poly2Points = ArrayList<DoubleArray>()
            polygon2.forEach { point ->
                poly2Points.add(point(point.x, point.y))
            }
            val poly2PointsList = ArrayList<List<DoubleArray>>()
            poly2PointsList.add(poly2Points)

            val poly1 = Polygon(poly1PointsList)
            val poly2 = Polygon(poly2PointsList)
            val union: Polygon = PolyBool.difference(
                eps,
                poly1,
                poly2
            )

            val result = ArrayList<Vector3D>()
            union.regions.forEach{ region ->
                region.forEach { point ->
                    result.add(Vector3D(point[0], point[1], 0.0))
                }
            }
            return result
        }
    }

}