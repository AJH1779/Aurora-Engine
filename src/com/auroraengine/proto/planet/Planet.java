/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.auroraengine.proto.planet;

import com.auroraengine.math.HDVec;
import com.auroraengine.proto.planet.GlobalRegionShape.Face;
import java.math.BigDecimal;

/**
 *
 * @author Arthur
 */
public class Planet {

	public static Planet createSpherical(double radius) {
		final double HALF_EDGE_LENGTH = 0.5257311121191336060256690848478766072854979322433417,
						ORIGIN_TO_CENTRE = 0.7946544722917661229555309283275940420265905883092648,
						VERTEX_TO_CENTRE = 0.6070619982066862230953915814199852573592468604781007,
						MID_EDGE_TO_CENTRE = 0.3035309991033431115476957907099926286796234302390503;

		double arclen = Math.sqrt(
						4 * (VERTEX_TO_CENTRE + MID_EDGE_TO_CENTRE)
						* (VERTEX_TO_CENTRE + MID_EDGE_TO_CENTRE)
						+ (16.0 / 3.0) * ORIGIN_TO_CENTRE * ORIGIN_TO_CENTRE
					) * radius;
		
		// 20 * order1 * order1 is the number of global regions that will exist
		// Global Regions are for very long distance rendering and processing
		int order1 = (int) Math.round(arclen * 0.01);
		
		// order2 must be of the form 2^n where n is an integer.
		// order2 should mean each Continental region size is on the order of the km
		double continent_target_len = 1800.0;
		double order2pow = Math.log(arclen/(continent_target_len*order1))/Math.log(2.0);
		int order2 = (int) Math.pow(2.0, Math.ceil(order2pow));
		
		// order3 must be of the form 2^n where n is an integer
		// order3 should mean each Local region size is on the order of 100 m, for
		// precision purposes.
		double local_target_len = 150.0;
		double order3pow = Math.log(arclen/(local_target_len*order2))/Math.log(2.0);
		int order3 = (int) Math.pow(2.0, Math.ceil(order3pow));
		
		final HDVec[] HDQ_ABC = new HDVec[]{
			new HDVec(-MID_EDGE_TO_CENTRE, ORIGIN_TO_CENTRE, -HALF_EDGE_LENGTH),
			new HDVec(-MID_EDGE_TO_CENTRE, ORIGIN_TO_CENTRE, HALF_EDGE_LENGTH),
			new HDVec(VERTEX_TO_CENTRE, ORIGIN_TO_CENTRE, 0.0)
		};

		final HDVec[][] shapevecs = new HDVec[][]{
			HDQ_ABC, HDQ_ABC, HDQ_ABC, HDQ_ABC, HDQ_ABC,
			HDQ_ABC, HDQ_ABC, HDQ_ABC, HDQ_ABC, HDQ_ABC,
			HDQ_ABC, HDQ_ABC, HDQ_ABC, HDQ_ABC, HDQ_ABC,
			HDQ_ABC, HDQ_ABC, HDQ_ABC, HDQ_ABC, HDQ_ABC
		};

		final int[][] neighbours = new int[][]{
			new int[]{1, 5, 4},
			new int[]{2, 6, 0},
			new int[]{3, 7, 1},
			new int[]{4, 8, 2},
			new int[]{0, 9, 3},
			new int[]{10, 14, 0},
			new int[]{11, 10, 1},
			new int[]{12, 11, 2},
			new int[]{13, 12, 3},
			new int[]{14, 13, 4},
			new int[]{5, 6, 15},
			new int[]{6, 7, 16},
			new int[]{7, 8, 17},
			new int[]{8, 9, 18},
			new int[]{9, 5, 19},
			new int[]{19, 10, 16},
			new int[]{15, 11, 17},
			new int[]{16, 12, 18},
			new int[]{17, 13, 19},
			new int[]{18, 14, 15}
		};

		final BorderTypeEnum[] BORDER_END = new BorderTypeEnum[]{
			BorderTypeEnum.AC, BorderTypeEnum.AC,
			BorderTypeEnum.BA
		}, BORDER_MID = new BorderTypeEnum[]{
			BorderTypeEnum.BA, BorderTypeEnum.CB,
			BorderTypeEnum.CB
		};
		final BorderTypeEnum[][] borders
						= new BorderTypeEnum[][]{
							BORDER_END, BORDER_END, BORDER_END, BORDER_END, BORDER_END,
							BORDER_MID, BORDER_MID, BORDER_MID, BORDER_MID, BORDER_MID,
							BORDER_MID, BORDER_MID, BORDER_MID, BORDER_MID, BORDER_MID,
							BORDER_END, BORDER_END, BORDER_END, BORDER_END, BORDER_END
						};

		return create(shapevecs, neighbours, borders, radius, order1, order2, order3);
	}

	public static Planet create(HDVec[][] shapevecs, int[][] neighbours,
					BorderTypeEnum[][] borders, double scale, int order1, int order2, int order3) {
		if (shapevecs == null) {
			throw new NullPointerException("Shape of regions not provided!");
		}
		if (neighbours == null) {
			throw new NullPointerException("Neighbours not provided!");
		}
		if (borders == null) {
			throw new NullPointerException("Borders not provided!");
		}
		if (shapevecs.length != neighbours.length) {
			throw new IllegalArgumentException
				("The shape and neighbour arrays are of different sizes.");
		}
		if (borders.length != neighbours.length) {
			throw new IllegalArgumentException
				("The border and neighbour arrays are of different sizes.");
		}
		if (shapevecs.length != borders.length) {
			throw new IllegalArgumentException
				("The shape and border arrays are of different sizes.");
		}

		GlobalRegionShape[] shapes = new GlobalRegionShape[neighbours.length];
		for(int i = 0; i < shapes.length; i++) {
			shapes[i] = GlobalRegionShape.getShape(shapevecs[i], order1, order2, order3);
		}
		
		int size = 0;
		for(GlobalRegionShape s : shapes) {
			size += s.getGlobalRegions();
		}
		Planet planet = new Planet(size);
		
		planet.generate(shapes, neighbours, borders);

		return planet;
	}

	public Planet(int size) {
		this.global_regions = new GlobalRegion[size];
	}
	private final GlobalRegion[] global_regions;

	public void generate(GlobalRegionShape[] shapes, int[][] neighbours,
					BorderTypeEnum[][] borders) {
		int[] ias = new int[shapes.length];
		for(int i = 0; i < shapes.length; i++) {
			int ia = (ias[i] = (i == 0 ? 0 : ias[i-1]) + shapes[i].getGlobalRegions());
			for(int j = 0; j < shapes[i].getGlobalRegions(); j++) {
				global_regions[ia + j] = new GlobalRegion(this, shapes[i].getGlobalFace(j), i, j);
			}
		}
		
		// Link the neighbours
		for(int i = 0; i < shapes.length; i++) {
			int ia = ias[i];
			for(int j = 0; j < shapes[i].getGlobalRegions(); j++) {
				// Set Neighbours
				GlobalRegion r = global_regions[ia + j];
				for(int a = 0; a < r.face.neighbours.length; a++) {
					if(r.face.neighbours[a] >= 0) {
						r.neighbours[a] = global_regions[ia + r.face.neighbours[a]];
					} else if (r.face.neighbours[a] != Integer.MIN_VALUE) {
						int an = - r.face.neighbours[a] - 1;
						int ar = an / (2 * shapes[i].getGlobalOrder() - 1);
						r.neighbours[a] = global_regions[
										ias[neighbours[i][ar]] + borders[i][ar].neighbour(
														shapes[i].getGlobalOrder(),
														-a%(2*shapes[i].getGlobalOrder()-1)-1)];
					}
				}
				for(int a = 0; a < r.face.corners.length; a++) {
					if(r.face.corners[a] >= 0) {
						r.corners[a] = global_regions[ia + r.face.corners[a]];
					} else if (r.face.neighbours[a] != Integer.MIN_VALUE) {
						int an = - r.face.corners[a] - 1;
						int ar = an / (2 * shapes[i].getGlobalOrder() - 1);
						r.corners[a] = global_regions[
										ias[neighbours[i][ar]] + borders[i][ar].neighbour(
														shapes[i].getGlobalOrder(),
														-a%(2*shapes[i].getGlobalOrder()-1)-1)];
					}
				}
			}
		}
	}

	public static class GlobalRegion {
		GlobalRegion(Planet planet, Face face, int i, int j) {
			this.planet = planet;
			this.face = face;
			
			regions = new ContinentalRegion[face.getGlobalRegionShape()
							.getContinentalRegionsPerGlobal()];
			
		}
		private final Planet planet;
		private final Face face;
		
		private final GlobalRegion[] neighbours = new GlobalRegion[3],
						corners = new GlobalRegion[3];
		
		private final ContinentalRegion[] regions;
		
		Planet getPlanet() {
			return planet;
		}

		Face getFace() {
			return face;
		}

		ContinentalRegion getContinentalRegion(int i) {
			return regions[i];
		}

		GlobalRegion getNeighbour(int i) {
			return neighbours[i];
		}
		GlobalRegion getCorner(int i) {
			return corners[i];
		}
	}

	private static class ContinentalRegion {
		public ContinentalRegion() {
			
		}
	}
}
