/**
 * Copyright (c) 2002-2011 "Neo Technology,"
 * Network Engine for Objects in Lund AB [http://neotechnology.com]
 *
 * This file is part of Neo4j.
 *
 * Neo4j is free software: you can redistribute it and/or modify
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
 * along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package org.neo4j.visualization.asciidoc;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;

import org.neo4j.graphdb.GraphDatabaseService;
import org.neo4j.visualization.graphviz.AsciiDocStyle;
import org.neo4j.visualization.graphviz.GraphvizWriter;
import org.neo4j.walk.Walker;

public class AsciidocHelper
{

    private static final String ILLEGAL_STRINGS = "[:\\(\\)\t;&/\\\\]"; 
    
    public static String createGraphViz( String title, GraphDatabaseService graph, String identifier )
    {
        OutputStream out = new ByteArrayOutputStream();
        GraphvizWriter writer = new GraphvizWriter(new AsciiDocStyle());
        try
        {
            writer.emit( out, Walker.fullGraph( graph ) );
        }
        catch ( IOException e )
        {
            e.printStackTrace();
        }
        
        String safeTitle = title.replaceAll(ILLEGAL_STRINGS, "");
        
        return "." + title + "\n[\"dot\", \""
               + ( safeTitle + "-" + identifier ).replace( " ", "-" )
               + ".svg\", \"neoviz\"]\n" +
                "----\n" +
                out.toString() +
                "----\n";
    }
    
    public static String createOutputSnippet( final String output )
    {
        return "[source]\n----\n"+output+"\n----\n";
    }
    
    public static String createQueryResultSnippet( final String output )
    {
        return "[queryresult]\n----\n" + output + "\n----\n";
    }

    public static String createCypherSnippet( final String query )
    {
        String result = "[source,cypher]\n----\n"+query.
                replace("start ", "START ").
                replace("where ", "WHERE ").
                replace("match ", "MATCH ").
                replace("return ", "RETURN ").
                replace(" MATCH ", "\nMATCH ").
                replace(" RETURN ", "\nRETURN ").
                replace(" WHERE ", "\nWHERE ").
                replace("where ", "WHERE ")+"\n----\n";
        //cut to max 123 chars for PDF compliance
        String[] tokens = result.split( "\n" );
        String finalRes = "";
        for(String token : tokens) {
            if (token.length() > 123 ) {
                token = token.replaceAll( ", ", ",\n" );
            }
            finalRes += token + "\n";
        }
        return finalRes;
    }
}
