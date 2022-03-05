package de.rebsc.bimtoosm.export

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

import de.rebsc.bimtoosm.data.osm.OSMDataSet
import de.rebsc.bimtoosm.data.osm.OSMNode
import de.rebsc.bimtoosm.data.osm.OSMTag
import de.rebsc.bimtoosm.data.osm.OSMWay
import de.rebsc.bimtoosm.export.Exporter.Companion.exportOSM
import de.rebsc.bimtoosm.utils.math.Point2D
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Disabled
import org.junit.jupiter.api.Test
import java.io.File

internal class ExporterTest {

    // setup test data
    private val tagList = ArrayList<OSMTag>().apply {
        add(OSMTag("indoor", "room"))
        add(OSMTag("level", "0"))
        add(OSMTag("name", "meeting-room"))
    }
    private val nodeList = ArrayList<OSMNode>().apply {
        add(OSMNode(-1, Point2D(8.43659239579, 49.10031359198), tagList))
        add(OSMNode(-4, Point2D(8.43663505998, 49.10042108694)))
        add(OSMNode(-2, Point2D(8.43649824261, 49.10044436511)))
        add(OSMNode(-3, Point2D(8.43645557855, 49.10033687002)))
    }
    private val wayList = ArrayList<OSMWay>().apply {
        add(OSMWay(-62, nodeList, tagList))
        add(OSMWay(-63, nodeList, tagList))
    }
    private val dataset = OSMDataSet(nodeList, wayList)

    private val trueFileAsString = "<?xml version='1.0' encoding='UTF-8'?>" +
            "<osm version='0.6' generator='BIMtoOSM'>" +
            "<note>This file was generated by https://github.com/rebeccasc/BIMtoOSM</note>" +
            "<node id='-1' visible='true' lat='49.10031359198' lon='8.43659239579'>" +
            "<tag k='indoor' v='room' />" +
            "<tag k='level' v='0' />" +
            "<tag k='name' v='meeting-room' />" +
            "</node>" +
            "<node id='-4' visible='true' lat='49.10042108694' lon='8.43663505998' />" +
            "<node id='-2' visible='true' lat='49.10044436511' lon='8.43649824261' />" +
            "<node id='-3' visible='true' lat='49.10033687002' lon='8.43645557855' />" +
            "<way id='-62' visible='true'>" +
            "<nd ref='-1' />" +
            "<nd ref='-4' />" +
            "<nd ref='-2' />" +
            "<nd ref='-3' />" +
            "<tag k='indoor' v='room' />" +
            "<tag k='level' v='0' />" +
            "<tag k='name' v='meeting-room' />" +
            "</way>" +
            "<way id='-63' visible='true'>" +
            "<nd ref='-1' />" +
            "<nd ref='-4' />" +
            "<nd ref='-2' />" +
            "<nd ref='-3' />" +
            "<tag k='indoor' v='room' />" +
            "<tag k='level' v='0' />" +
            "<tag k='name' v='meeting-room' />" +
            "</way>" +
            "</osm>"

    @Test
    fun exportOSMTest() {
        val dir = System.getProperty("user.dir")

        // check if test directory already exists, if not create
        val directoryPath = "$dir/src/test/output".replace("/", File.separator)
        val directory = File(directoryPath)
        if (!directory.exists()) {
            directory.mkdir()
        }

        // export data
        val filename = "$dir/src/test/output/exportTest.osm".replace("/", File.separator)
        exportOSM(filename, dataset, addTimestamp = false)

        // read true data and check equality
        val fileAsString = File(filename).readText(Charsets.UTF_8)
        Assertions.assertEquals(
            trueFileAsString.replace("\\s+".toRegex(), ""),
            fileAsString.replace("\\s".toRegex(), "")
        )

        // clean test directory
        directory.deleteRecursively()
    }
}