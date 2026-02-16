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
        //    |        GBR          /   |      |           JPN
        //   USA                FRA -- CHE -- AUT
        //    |                  |      |
        //   MEX                ESP    ITA
        //
        //   (connected) AUT, CHE, CZE, DEU, ESP, FRA, ITA
        //   (disconnected) JPN, GBR, USA

        val adjacencyMap = new HashMap<String, Set<String>>();
        adjacencyMap.put("AUT", Set.of("CZE", "ITA", "CHE"));
        adjacencyMap.put("CHE", Set.of("AUT", "ITA", "DEU"));
        adjacencyMap.put("CZE", Set.of("AUT", "DEU"));
        adjacencyMap.put("DEU", Set.of("CZE", "CHE", "FRA"));
        adjacencyMap.put("ESP", Set.of("FRA"));
        adjacencyMap.put("FRA", Set.of("DEU", "ESP"));
        adjacencyMap.put("GBR", Set.of());
        adjacencyMap.put("ITA", Set.of("AUT", "CHE"));
        adjacencyMap.put("JPN", Set.of());
        adjacencyMap.put("USA", Set.of("CAN", "MEX"));
        return adjacencyMap;
    }

}
