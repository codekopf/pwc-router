package com.codekopf.router.utils;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import lombok.experimental.UtilityClass;
import lombok.val;

@UtilityClass
public class AdjacencyMapBuilder {

    public static Map<String, Set<String>> getSimpleAdjacencyMapForTesting() {

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
        //        + North America claster - CAN, MEX, USA
        //        + Europen claster - AUT, CZE, DEU, ESP, FRA, ITA
        //        + Central Asia claster - AFG, KAZ, KGZ, TJK, UZB
        //   (disconnected) JPN, GBR

        val adjacencyMap = new HashMap<String, Set<String>>();
        adjacencyMap.put("AFG", Set.of("UZB", "TJK"));
        adjacencyMap.put("AUT", Set.of("CZE", "ITA", "CHE"));
        adjacencyMap.put("CHE", Set.of("AUT", "ITA", "DEU"));
        adjacencyMap.put("CZE", Set.of("AUT", "DEU"));
        adjacencyMap.put("DEU", Set.of("CZE", "CHE", "FRA"));
        adjacencyMap.put("ESP", Set.of("FRA"));
        adjacencyMap.put("FRA", Set.of("DEU", "ESP"));
        adjacencyMap.put("GBR", Set.of());
        adjacencyMap.put("ITA", Set.of("AUT", "CHE"));
        adjacencyMap.put("JPN", Set.of());
        adjacencyMap.put("KAZ", Set.of("UZB", "KGZ"));
        adjacencyMap.put("KGZ", Set.of("KAZ", "TJK"));
        adjacencyMap.put("TJK", Set.of("KGZ", "AFG"));
        adjacencyMap.put("USA", Set.of("CAN", "MEX"));
        adjacencyMap.put("UZB", Set.of("KAZ", "AFG"));
        return adjacencyMap;
    }

}
