package de.rebsc.bimtoosm.loader

/******************************************************************************
 * Copyright (C) 2021  de.rebsc
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program. If not, see {@literal<http://www.gnu.org/licenses/>}.
 *****************************************************************************/

import de.rebsc.bimtoosm.exception.BIMtoOSMException
import org.bimserver.plugins.deserializers.DeserializeException
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import java.io.File
import java.io.IOException
import java.net.URL

internal class LoaderTest {

    // Test setup

    // URLs
    private val urlWallWithWindow_ifc2X3 =
        URL("https://raw.githubusercontent.com/rebeccasc/IfcTestFiles/master/ifc2X3/wall/ifcwallstandardcase/wall_single_with_window_IFC2X3.ifc")
    private val urlHouse1_ifc2X3_BC =
        URL("https://raw.githubusercontent.com/rebeccasc/IfcTestFiles/master/ifc2X3/building/house_1_IFC2X3_BC.ifc")
    private val urlKfzHouse_ifc4 =
        URL("https://raw.githubusercontent.com/rebeccasc/IfcTestFiles/master/ifc4/building/kfz_house_IFC4.ifc")
    private val urlHouse1ifc2X3 =
        URL("https://raw.githubusercontent.com/rebeccasc/IfcTestFiles/master/ifc2X3/building/house_1_IFC2X3.ifc")


    @Test
    fun loadFileTest() {
        //------------ test invalid file ------------ //
        // test non-existent ifc file
        Assertions.assertThrows(BIMtoOSMException::class.java) {
            val invalidFilePath = downloadFile(urlWallWithWindow_ifc2X3).name
            Loader.loadIntoModel(invalidFilePath)
        }

        // test invalid file with block comments
        Assertions.assertThrows(DeserializeException::class.java) {
            val filepath = downloadFile(urlHouse1_ifc2X3_BC).path
            Loader.loadIntoModel(filepath)
        }

        // TODO test more invalid ifc file

        //------------ test valid file ------------ //
        // test valid IFC4 file
        Assertions.assertDoesNotThrow {
            urlKfzHouse_ifc4
            val filepath = downloadFile(urlKfzHouse_ifc4).path
            Loader.loadIntoModel(filepath)
        }

        // test valid IFC2X3_TC1 file
        Assertions.assertDoesNotThrow {
            urlKfzHouse_ifc4
            val filepath = downloadFile(urlHouse1ifc2X3).path
            Loader.loadIntoModel(filepath)
        }

        // clean up test directory
        cleanTestDirectory()
    }

    // helper

    @Throws(IOException::class)
    private fun downloadFile(url: URL): File {
        // check if test directory already exists, if not create
        val directoryPath = "${System.getProperty("user.dir")}/src/test/tmp_test".replace("/", File.separator)
        val directory = File(directoryPath)
        if (!directory.exists()) {
            directory.mkdir()
        }
        // download file into test directory
        val tmpFile = File("$directoryPath${File.separator}tmpFileTest")
        try {
            val bytes = url.readBytes()
            tmpFile.writeBytes(bytes)
        } catch (e: IOException) {
            throw IOException("Could not download file $url. Abort test.")
        }
        return tmpFile
    }

    private fun cleanTestDirectory() {
        val directoryPath = "${System.getProperty("user.dir")}/src/test/tmp_test".replace("/", File.separator)
        File(directoryPath).deleteRecursively()
    }

}