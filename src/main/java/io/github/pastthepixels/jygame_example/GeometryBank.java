package io.github.pastthepixels.jygame_example;

import io.github.espressoengine.Vector2;

import java.awt.Polygon;


public class GeometryBank { // Stores polygons
    double[][] ship_geometry = { // Creates a ship that points down
        {0, 0},
        {40, 20},
        {80, 0},
        {40, 80}
    };
    double[][] hexagon_geometry = {
        {15, 0},
        {45, 0},
        {60, 26},
        {45, 52},
        {15, 52},
        {0, 26},
    };

    public Polygon createPolygon(double[][] geometry, Vector2 enginePosition) {
        Polygon polygon = new Polygon();
        for(int i = 0; i < geometry.length; i++) {
            polygon.addPoint((int)((geometry[i][0]+enginePosition.x)), (int)((geometry[i][1]+enginePosition.y)));
        }
        return polygon;
    }

    public Polygon createPolygon(double[][] geometry, Vector2 enginePosition, double scale) {
        Polygon polygon = new Polygon();
        for(int i = 0; i < geometry.length; i++) {
            polygon.addPoint((int)(geometry[i][0]*scale+enginePosition.x), (int)(geometry[i][1]*scale+enginePosition.y));
        }
        return polygon;
    }
}
