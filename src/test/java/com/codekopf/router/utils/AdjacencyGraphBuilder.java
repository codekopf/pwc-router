package com.codekopf.router.utils;

import java.util.HashMap;
import java.util.Set;

import com.codekopf.router.model.AdjacencyGraph;

public class AdjacencyGraphBuilder {

    private AdjacencyGraphBuilder() {
        // Private constructor to prevent instantiation
    }

    public static AdjacencyGraph getSimpleAdjacencyGraphForTesting() {

        // Building a small test graph:
        //
        //   CAN                       DEU -- CZE
        //    |        GBR          /   |      |                         JPN
        //   USA                FRA -- CHE -- AUT
        //    |                  |      |
        //   MEX                ESP    ITA               KAZ
        //                                              /  \
        //                                           UZB   KGZ
        //                                            |     |
        //                                            \    TJK
        //                                             \   /
        //                                              AFG
        //
        //   (connected)
        //        + North America cluster - CAN, MEX, USA
        //        + Europen cluster - AUT, CZE, DEU, ESP, FRA, ITA
        //        + Central Asia cluster - AFG, KAZ, KGZ, TJK, UZB
        //   (disconnected) JPN, GBR

        var adjacencyMap = new HashMap<String, Set<String>>();
        adjacencyMap.put("AFG", Set.of("UZB", "TJK"));
        adjacencyMap.put("AUT", Set.of("CHE", "CZE", "ITA"));
        adjacencyMap.put("CAN", Set.of("USA"));
        adjacencyMap.put("CHE", Set.of("AUT", "DEU", "FRA", "ITA"));
        adjacencyMap.put("CZE", Set.of("AUT", "DEU"));
        adjacencyMap.put("DEU", Set.of("CHE", "CZE", "FRA"));
        adjacencyMap.put("ESP", Set.of("FRA"));
        adjacencyMap.put("FRA", Set.of("CHE", "DEU", "ESP"));
        adjacencyMap.put("GBR", Set.of());
        adjacencyMap.put("ITA", Set.of("AUT", "CHE"));
        adjacencyMap.put("JPN", Set.of());
        adjacencyMap.put("KAZ", Set.of("UZB", "KGZ"));
        adjacencyMap.put("KGZ", Set.of("KAZ", "TJK"));
        adjacencyMap.put("MEX", Set.of("USA"));
        adjacencyMap.put("TJK", Set.of("KGZ", "AFG"));
        adjacencyMap.put("USA", Set.of("CAN", "MEX"));
        adjacencyMap.put("UZB", Set.of("KAZ", "AFG"));
        return new AdjacencyGraph(adjacencyMap);
    }

}
