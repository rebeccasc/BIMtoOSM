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
import de.rebsc.bimtoosm.data.osm.OSMWay
import java.io.File
import java.time.LocalDateTime

/**
 * Very basic osm exporter. Nodes and ways support only
 */
class Exporter {

    companion object {

        private const val xml_header = "<?xml version='1.0' encoding='UTF-8'?>"
        private const val osm_opener = "<osm version='0.6' generator='BIMtoOSM'>"
        private const val comment = "<note>This file was generated by https://github.com/rebeccasc/BIMtoOSM</note>"
        private const val osm_closer = "</osm>"
        private const val double_WS = "  "

        /**
         * Exports [0SMDataSet] to .osm file
         * @param filepath to export file
         * @param data to export
         * @param addTimestamp true to add timestamp, else false
         */
        fun exportOSM(filepath: String, data: OSMDataSet, addTimestamp: Boolean) {
            // TODO rather use nio.file.Path as filepath type
            // TODO check if path exists

            val file = File(filepath)

            file.printWriter().use { out ->
                out.println(xml_header)
                out.println(osm_opener)
                out.println(comment)
                if (addTimestamp) out.println("<meta osm_base='${LocalDateTime.now()}'/>")
                data.nodes.forEach { node ->
                    nodeToStringArrayList(node).forEach {
                        out.println(it)
                    }
                }
                data.ways.forEach { way ->
                    wayToStringArrayList(way).forEach {
                        out.println(it)
                    }
                }
                out.println(osm_closer)
            }
        }

        /**
         * Transforms [OSMNode] to string
         * @param node to transform
         * @return osm-string with node information
         */
        private fun nodeToStringArrayList(node: OSMNode): ArrayList<String> {
            val parts = ArrayList<String>()
            if (node.tags.isEmpty()) {
                parts.add("$double_WS<node id='${node.id}' visible='true' lat='${node.y}' lon='${node.x}' />")
            } else {
                parts.add("$double_WS<node id='${node.id}' visible='true' lat='${node.y}' lon='${node.x}'>")
                node.tags.forEach { tag ->
                    parts.add("$double_WS$double_WS<tag k='${tag.k}' v='${tag.v}' />")
                }
                parts.add("$double_WS</node>")
            }
            return parts
        }

        /**
         * Transforms [OSMWay] to string
         * @param way to transform
         * @return osm-string with way information
         */
        private fun wayToStringArrayList(way: OSMWay): ArrayList<String> {
            val parts = ArrayList<String>()
            parts.add("$double_WS<way id='${way.id}' visible='true'>")
            way.points.forEach { point ->
                parts.add("$double_WS$double_WS<nd ref='${point.id}' />")
            }
            way.tags.forEach { tag ->
                parts.add("$double_WS$double_WS<tag k='${tag.k}' v='${tag.v}' />")
            }
            parts.add("$double_WS</way>")
            return parts
        }
    }

}