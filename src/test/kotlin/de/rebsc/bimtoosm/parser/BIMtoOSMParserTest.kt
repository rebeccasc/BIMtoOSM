package de.rebsc.bimtoosm.parser

import de.rebsc.bimtoosm.exception.BIMtoOSMException
import de.rebsc.bimtoosm.geometry.GeometrySolution
import org.junit.Assert.assertThrows
import org.junit.Test


internal class BIMtoOSMParserTest {

    @Test
    fun initTest() {
        assertThrows(BIMtoOSMException::class.java) {
            val invalidConfig = Configuration(
                GeometrySolution.BODY,
                true,
                false,
                -0.1
            )
            BIMtoOSMParser(invalidConfig)
        }

        assertThrows(BIMtoOSMException::class.java) {
            val invalidConfig = Configuration(
                GeometrySolution.BODY,
                false,
                false,
                Double.POSITIVE_INFINITY
            )
            BIMtoOSMParser(invalidConfig)
        }

        val validConfig = Configuration(
            GeometrySolution.BOUNDING_BOX,
            true,
            true,
            0.05
        )
        BIMtoOSMParser(validConfig)
    }

    @Test
    fun parseTest() {
        // TODO fix - implementation for testing only
        val dir = System.getProperty("user.dir")
        val filepath = "$dir/src/test/resources/test1_IFC4.ifc"
        BIMtoOSMParser(Configuration()).parse(filepath)
    }

}