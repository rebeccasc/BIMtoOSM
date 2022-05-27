package de.rebsc.bimtoosm.utils.math

import org.junit.jupiter.api.Test
import org.junit.jupiter.api.Assertions.*

internal class BooleanOPTest{

    @Test
    fun differenceTest(){
        val poly1 = ArrayList<Vector3D>().apply {
            add(Vector3D(0.0, 0.0, 0.0))
            add(Vector3D(3.0, 0.0, 0.0))
            add(Vector3D(3.0,6.0, 0.0))
            add(Vector3D(0.0, 6.0, 0.0))
        }
        val poly2 = ArrayList<Vector3D>().apply {
            add(Vector3D(1.0, -1.0, 0.0))
            add(Vector3D(2.0, -1.0, 0.0))
            add(Vector3D(2.0, 2.0, 0.0))
            add(Vector3D(1.0, 2.0, 0.0))
        }
        val difference12 = BooleanOP.difference(poly1, poly2)
        // check difference result size
        assertEquals(8, difference12.size)
        // check for correct result
        assertTrue(difference12[0].equalsVector(Vector3D(3.0, 6.0, 0.0)))
        assertTrue(difference12[1].equalsVector(Vector3D(3.0, 0.0, 0.0)))
        assertTrue(difference12[2].equalsVector(Vector3D(2.0, 0.0, 0.0)))
        assertTrue(difference12[3].equalsVector(Vector3D(2.0, 2.0, 0.0)))
        assertTrue(difference12[4].equalsVector(Vector3D(1.0, 2.0, 0.0)))
        assertTrue(difference12[5].equalsVector(Vector3D(1.0, 0.0, 0.0)))
        assertTrue(difference12[6].equalsVector(Vector3D(0.0, 0.0, 0.0)))
        assertTrue(difference12[7].equalsVector(Vector3D(0.0, 6.0, 0.0)))
    }

    @Test
    fun intersectTest(){
        val poly1 = ArrayList<Vector3D>().apply {
            add(Vector3D(0.0, 0.0, 0.0))
            add(Vector3D(3.0, 0.0, 0.0))
            add(Vector3D(3.0,6.0, 0.0))
            add(Vector3D(0.0, 6.0, 0.0))
        }
        val poly2 = ArrayList<Vector3D>().apply {
            add(Vector3D(1.0, -1.0, 0.0))
            add(Vector3D(2.0, -1.0, 0.0))
            add(Vector3D(2.0, 2.0, 0.0))
            add(Vector3D(1.0, 2.0, 0.0))
        }
        val intersect = BooleanOP.intersect(poly1, poly2)
        // check intersection result size
        assertEquals(4, intersect.size)
        // check for correct result
        assertTrue(intersect[0].equalsVector(Vector3D(2.0, 2.0, 0.0)))
        assertTrue(intersect[1].equalsVector(Vector3D(2.0, 0.0, 0.0)))
        assertTrue(intersect[2].equalsVector(Vector3D(1.0, 0.0, 0.0)))
        assertTrue(intersect[3].equalsVector(Vector3D(1.0, 2.0, 0.0)))
    }

    @Test
    fun unionTest(){
        // TODO implement
    }
}