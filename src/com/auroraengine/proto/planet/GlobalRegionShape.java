/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.auroraengine.proto.planet;

import com.auroraengine.math.HDRef;
import com.auroraengine.math.HDVec;
import com.auroraengine.math.LDRef;
import com.auroraengine.math.LDVec;
import java.io.File;
import java.util.Arrays;
import java.util.HashSet;

/**
 *
 * @author Arthur
 */
public class GlobalRegionShape {

	public static GlobalRegionShape getShape(HDVec[] abc,
					int order1, int order2, int order3){
		GlobalRegionShape s = SHAPES.stream().filter((r) ->
			r.order1 == order1 && r.order2 == order2 && Arrays.equals(abc, r.abc)
		).findAny().orElse(null);
		if (s == null) {
			s = new GlobalRegionShape(abc, order1, order2, order3);
			s.generate();
		}
		return s;
	}
	private static final HashSet<GlobalRegionShape> SHAPES = new HashSet<>();

	private GlobalRegionShape(HDVec[] abc, int order1, int order2, int order3) {
		this.abc = abc;
		this.order1 = order1; this.order2 = order2; this.order3 = order3;
		this.global_faces = new Face[order1 * order1];
		this.continental_faces = new Face[order1 * order1 * order2 * order2];
		this.local_faces = new Face[order1 * order1 * order2 * order2 * order3 * order3];
	}
	private final int order1, order2, order3;
	private final HDVec[] abc;

	private final Face[] global_faces, continental_faces, local_faces;

	public int getGlobalOrder() {
		return order1;
	}
	public int getContinentalOrder() {
		return order2;
	}
	public int getLocalOrder() {
		return order3;
	}

	public int getGlobalRegions() {
		return order1 * order1;
	}
	public int getContinentalRegionsPerGlobal() {
		return order2 * order2;
	}
	public int getLocalRegionsPerContinental() {
		return order3 * order3;
	}
	public int getContinentalRegions() {
		return order1 * order1 * order2 * order2;
	}
	public int getLocalRegionsPerGlobal() {
		return order2 * order2 * order3 * order3;
	}
	public int getLocalRegions() {
		return order1 * order1 * order2 * order2 * order3 * order3;
	}

	public Face getGlobalFace(int i) {
		return global_faces[i];
	}
	public Face getContinentalFace(int i) {
		return continental_faces[i];
	}
	public Face getLocalFace(int i) {
		return local_faces[i];
	}

	public void generate() {
		int n = order1, m = order1 * order2, o = order1 * order2 * order3;

		// These are the edge vectors, giving AB, BC, CA divided by order
		HDVec[] ne = new HDVec[]{
			abc[1].toHD().negTranslate(abc[0]).invscale(n),
			abc[2].toHD().negTranslate(abc[1]).invscale(n),
			abc[0].toHD().negTranslate(abc[2]).invscale(n)
		};
		HDVec[] me = new HDVec[]{
			abc[1].toHD().negTranslate(abc[0]).invscale(m),
			abc[2].toHD().negTranslate(abc[1]).invscale(m),
			abc[0].toHD().negTranslate(abc[2]).invscale(m)
		};
		HDVec[] oe = new HDVec[]{
			abc[1].toHD().negTranslate(abc[0]).invscale(o),
			abc[2].toHD().negTranslate(abc[1]).invscale(o),
			abc[0].toHD().negTranslate(abc[2]).invscale(o)
		};

		HDVec[] n_verts = makeVertexes(n, ne);
		HDVec[] m_verts = makeVertexes(m, me);
		HDVec[] o_verts = makeVertexes(o, oe);

		Face[] n_faces = global_faces; int n_order = order2;
		Face[] m_faces = continental_faces; int m_order = order3;
		Face[] o_faces = local_faces; int o_order = 0;
		
		HDVec[][] n_face_vecs = makeFaceVecs(n, n_verts, n_faces, n_order);
		HDVec[][] m_face_vecs = makeFaceVecs(m, m_verts, m_faces, m_order);
		HDVec[][] o_face_vecs = makeFaceVecs(o, o_verts, o_faces, o_order);

		HDVec[][] n_edge_norms = makeEdgeNorms(n, n_verts);
		HDVec[][] m_edge_norms = makeEdgeNorms(m, m_verts);
		HDVec[][] o_edge_norms = makeEdgeNorms(o, o_verts);

		HDVec[][] n_face_overflow = makeFaceOverflow(2*n-1, n, n_verts, n_edge_norms);
		HDVec[][] m_face_overflow = makeFaceOverflow(2*m-1, m, m_verts, m_edge_norms);
		HDVec[][] o_face_overflow = makeFaceOverflow(2*o-1, o, o_verts, o_edge_norms);

		HDRef[] n_global_refs = makeGlobalRefs(n, n_face_vecs);
		HDRef[] n_overflow_refs = makeOverflowRefs(n, n_face_overflow);
		HDRef[] m_global_refs = makeGlobalRefs(m, m_face_vecs);
		HDRef[] m_overflow_refs = makeOverflowRefs(m, m_face_overflow);
		HDRef[] o_global_refs = makeGlobalRefs(o, o_face_vecs);
		HDRef[] o_overflow_refs = makeOverflowRefs(o, o_face_overflow);

		makeIntraRefs(n_global_refs, n_face_vecs, n_faces, n_edge_norms,
						n_face_overflow, n_overflow_refs);
		makeIntraRefs(m_global_refs, m_face_vecs, m_faces, m_edge_norms,
						m_face_overflow, m_overflow_refs);
		makeIntraRefs(o_global_refs, o_face_vecs, o_faces, o_edge_norms,
						o_face_overflow, o_overflow_refs);
		
		makeInterRefs(n_global_refs, n_face_vecs, n_faces,
						m_global_refs, m_face_vecs, m_faces);
		makeInterRefs(m_global_refs, m_face_vecs, m_faces,
						o_global_refs, o_face_vecs, o_faces);
	}

	private void makeInterRefs(HDRef[] n_global_refs, HDVec[][] n_face_vecs, Face[] n_faces,
					HDRef[] m_global_refs, HDVec[][] m_face_vecs, Face[] m_faces) {
		// # Should work - not tested
		for(int i = 0, in = 1; i < n_global_refs.length; i++) {
			HDRef inv = new HDRef(n_global_refs[i]).invert();
			HDRef dirinv = new HDRef(inv)
							.translateGlobally(new HDVec(n_face_vecs[i][3]).negate());
			if(in * in <= i) {
				in++;
			}
			int ia = in * in * order2 * order2;
			boolean b = (ia - i) % 2 == 0; // tri down
			for(int j = 0, jn = 1; j < order2*order2; j++) {
				if(b) {
					if(j >= 2*jn*(order2 - jn)) {
						jn++;
					}
				} else {
					if(jn * jn <= j) {
						jn++;
					}
				}
				
				int ja = ia + j + order2 * (i + (in - 1) * ((jn - 1) * 2 - (in - 1)))
								+ (b ? 1 - order2 : 0);
				
				HDRef ref = dirinv.toHD().translateGlobally(m_face_vecs[i][3])
								.multiplyGlobally(m_global_refs[ja]);
				n_faces[i].to_lower[j] = ref.toLD();
				m_faces[ja].to_upper[0] = ref.invert().toLD();
			}
		}
	}

	private void makeIntraRefs(HDRef[] n_global_refs, HDVec[][] n_face_vecs, Face[] n_faces, HDVec[][] n_edge_norms, HDVec[][] n_face_overflow, HDRef[] n_overflow_refs) {
		for (int i = 0; i < n_global_refs.length; i++) {
			HDRef inv = new HDRef(n_global_refs[i]).invert();
			HDRef dirinv = new HDRef(inv)
							.translateGlobally(new HDVec(n_face_vecs[i][3]).negate());
			for (int j = 0; j < 3; j++) {
				n_faces[i].vertexes[j] = inv.transform(new HDVec(n_face_vecs[i][j])
								.negTranslate(n_face_vecs[i][3])).toLD();
				n_faces[i].vertex_normals[j] = inv.transform(new HDVec(n_face_vecs[i][j]))
								.normalise().toLD();
				n_faces[i].edge_normals[j] = inv.transform(new HDVec(n_edge_norms[i][j]))
								.normalise().toLD();

				if (n_faces[i].neighbours[j] >= 0) {
					n_faces[i].to_neighbours[j] = new LDRef[]{
						new HDRef(dirinv)
										.translateGlobally(n_face_vecs[n_faces[i].neighbours[j]][3])
										.multiplyGlobally(n_global_refs[n_faces[i].neighbours[j]]).toLD()
					};
				} else {
					HDRef temp = new HDRef(dirinv)
									.translateGlobally(n_face_overflow[(-n_faces[i].neighbours[j] - 1)][3]);
					n_faces[i].to_neighbours[j] = new LDRef[]{
						new HDRef(temp)
										.multiplyGlobally(n_overflow_refs[(-n_faces[i].neighbours[j] - 1) * 3])
										.toLD(),
						new HDRef(temp)
										.multiplyGlobally(n_overflow_refs[(-n_faces[i].neighbours[j] - 1) * 3 + 1])
										.toLD(),
						new HDRef(temp)
										.multiplyGlobally(n_overflow_refs[(-n_faces[i].neighbours[j] - 1) * 3 + 2])
										.toLD()
					};
				}
				if (n_faces[i].corners[j] >= 0) {
					n_faces[i].to_corners[j] = new LDRef[]{
						new HDRef(dirinv)
										.translateGlobally(n_face_vecs[n_faces[i].corners[j]][3])
										.multiplyGlobally(n_global_refs[n_faces[i].corners[j]])
										.toLD()
					};
				} else if (n_faces[i].corners[j] != Integer.MIN_VALUE) {
					HDRef temp = new HDRef(dirinv)
									.translateGlobally(n_face_overflow[(-n_faces[i].corners[j] - 1)][3]);
					n_faces[i].to_corners[j] = new LDRef[]{
						new HDRef(temp)
										.multiplyGlobally(n_overflow_refs[(-n_faces[i].corners[j] - 1) * 3])
										.toLD(),
						new HDRef(temp)
										.multiplyGlobally(n_overflow_refs[(-n_faces[i].corners[j] - 1) * 3 + 1])
										.toLD(),
						new HDRef(temp)
										.multiplyGlobally(n_overflow_refs[(-n_faces[i].corners[j] - 1) * 3 + 2])
										.toLD()
					};
				}
			}
		}
	}

	private HDRef[] makeOverflowRefs(int n, HDVec[][] n_face_overflow) {
		HDRef[] n_overflow_refs = new HDRef[2*n-1 * 9];
		for (int i = 0; i < n_face_overflow.length; i++) {
			n_overflow_refs[i * 3] = new HDRef(n_face_overflow[i]);
			n_overflow_refs[i * 3 + 1] = new HDRef(n_face_overflow[i][1],
							n_face_overflow[i][2], n_face_overflow[i][0]);
			n_overflow_refs[i * 3 + 2] = new HDRef(n_face_overflow[i][2],
							n_face_overflow[i][0], n_face_overflow[i][1]);
		}
		return n_overflow_refs;
	}

	private HDRef[] makeGlobalRefs(int n, HDVec[][] n_face_vecs) {
		HDRef[] n_global_refs = new HDRef[n * n];
		for (int i = 0; i < n_global_refs.length; i++) {
			n_global_refs[i] = new HDRef(n_face_vecs[i]);
		}
		return n_global_refs;
	}

	private HDVec[][] makeFaceOverflow(int eo, int n, HDVec[] n_verts, HDVec[][] n_edge_norms) {
		// Note these overflow vertexes can be misleading. If the adjacent regions
		// do not conform to the same shape as this one, then the overflow will
		// be invalid. There may be a better way of doing this.
		HDVec[][] face_overflow = new HDVec[eo * 3][];
		HDVec[] vertex_overflow = new HDVec[n * 3];
		for (int i = 0, ia = 0; i < n; i++, ia += i) {
			vertex_overflow[i] = new HDVec(n_verts[ia])
							.negate().translate(n_verts[ia + i + 2])
							.reflect(n_edge_norms[i * i][0])
							.translate(n_verts[ia]);
			face_overflow[i * 2] = new HDVec[]{
				vertex_overflow[i],
				n_verts[ia + i + 1],
				n_verts[ia],
				HDVec.getAverage(vertex_overflow[i],
								n_verts[ia + i + 1],
								n_verts[ia])
			};
			
			vertex_overflow[n + i] = new HDVec(n_verts[n_verts.length - 2 * n - 1 + i])
							.negTranslate(n_verts[n_verts.length - n + i])
							.reflect(n_edge_norms[(n - 1) * (n - 1) + 2 * i][1])
							.translate(n_verts[n_verts.length - n + i]);
			face_overflow[i * 2 + eo] = new HDVec[]{
				vertex_overflow[n + i],
				n_verts[n_verts.length - n + i],
				n_verts[n_verts.length - n + i - 1],
				HDVec.getAverage(vertex_overflow[n + i],
								n_verts[n_verts.length - n + i],
								n_verts[n_verts.length - n + i - 1])
			};
			
			vertex_overflow[n * 3 - i - 1] = new HDVec(n_verts[ia + i])
							.negate().translate(n_verts[ia + 2 * i + 1])
							.reflect(n_edge_norms[(i + 1) * (i + 1) - 1][2])
							.translate(n_verts[ia + i]);
			face_overflow[eo * 3 - 2 * i - 1] = new HDVec[]{
				vertex_overflow[n * 3 - i - 1],
				n_verts[ia + i],
				n_verts[ia + 2 * i + 2],
				HDVec.getAverage(vertex_overflow[n * 3 - i - 1],
								n_verts[ia + i],
								n_verts[ia + 2 * i + 2])
			};
		}
		for (int i = 0, ia = 0; i < n - 1; i++, ia += i) {
			face_overflow[i * 2 + 1] = new HDVec[]{
				n_verts[ia + i + 1],
				vertex_overflow[i],
				vertex_overflow[i + 1],
				HDVec.getAverage(n_verts[ia + i + 1],
								vertex_overflow[i],
								vertex_overflow[i + 1])
			};
			face_overflow[2 * n + i * 2] = new HDVec[]{
				n_verts[n_verts.length - n + i],
				vertex_overflow[n + i],
				vertex_overflow[n + i + 1],
				HDVec.getAverage(n_verts[n_verts.length - n + i],
								vertex_overflow[n + i],
								vertex_overflow[n + i + 1])
			};
			face_overflow[eo * 3 - i * 2 - 2] = new HDVec[]{
				n_verts[ia + (2 * i) + 2],
				vertex_overflow[n * 3 - i - 2],
				vertex_overflow[n * 3 - i - 1],
				HDVec.getAverage(n_verts[ia + (2 * i) + 2],
								vertex_overflow[n * 3 - i - 2],
								vertex_overflow[n * 3 - i - 1])
			};
		}
		return face_overflow;
	}

	private HDVec[][] makeEdgeNorms(int n, HDVec[] n_verts) {
		HDVec[][] n_edge_norms = new HDVec[n * n][3];
		for (int i = 0, ia = 0; i < n; i++, ia += i) {
			for (int j = 0, a = i * i; j < i + 1; j++, a += 2) {
				HDVec A = n_verts[ia + j],
								B = n_verts[ia + j + i + 1],
								C = n_verts[ia + j + i + 2];
				
				n_edge_norms[a][0] = new HDVec(B).cross(A).normalise();
				if (j != 0) {
					n_edge_norms[a - 1][0] = new HDVec(n_edge_norms[a][0]).negate();
				}
				n_edge_norms[a][1] = new HDVec(C).cross(B).normalise();
				if (i != n - 1) {
					n_edge_norms[a + 2 * i + 2][1] = new HDVec(n_edge_norms[a][1]).negate();
				}
				n_edge_norms[a][2] = new HDVec(A).cross(C).normalise();
				if (j != i) {
					n_edge_norms[a + 1][2] = new HDVec(n_edge_norms[a][2]).negate();
				}
			}
		}
		return n_edge_norms;
	}

	private HDVec[][] makeFaceVecs(int n, HDVec[] n_verts, Face[] n_faces, int n_order) {
		// This is creating the faces of the region in two parts, separated depending
		// on orientation.
		HDVec[][] n_face_vecs = new HDVec[n * n][];
		// Create the faces which are aligned with the parent global region.
		// Vertexes are assigned in counter-clockwise fashion.
		// The neighbours are either positive direct references,
		// or negative indirect references.
		for (int i = 0, ia = 0; i < n; i++, ia += i) {
			for (int j = 0, a = i * i; j < i + 1; j++, a += 2) {
				n_face_vecs[a] = new HDVec[]{
					n_verts[ia + j],
					n_verts[ia + j + i + 1],
					n_verts[ia + j + i + 2],
					HDVec.getAverage(n_verts[ia + j],
									n_verts[ia + j + i + 1],
									n_verts[ia + j + i + 2])
				};
				n_faces[a] = new Face(this,
								new int[]{
									(j == 0) ? -(2 * i + 1) : a - 1,
									(i == n - 1) ? -(2 * j + 2 * n) : a + 2 * i + 2,
									(j == i) ? -(6 * n - 2 * i - 3) : a + 1
								},
								new int[]{
									(i == n - 1)
													? ((j == 0) ? Integer.MIN_VALUE : -(2 * n + 2 * j - 2))
													: ((j == 0) ? -(2 * i + 3) : a + 2 * i),
									(i == n - 1)
													? ((j == i) ? Integer.MIN_VALUE : -(2 * n + 2 * j + 2))
													: ((j == i) ? -(6 * n - 2 * i - 5) : a + 2 * i + 4),
									(j == i)
													? ((j == 0) ? Integer.MIN_VALUE : -(6 * n - 2 * i - 1))
													: ((j == 0) ? -(2 * i - 1) : a - 2 * i)
								},
								n_order
				);
			}
			for (int j = 0, a = i * i + 1; j < i; j++, a += 2) {
				n_face_vecs[a] = new HDVec[]{
					n_verts[ia + j + i + 2],
					n_verts[ia + j + 1],
					n_verts[ia + j],
					HDVec.getAverage(n_verts[ia + j + i + 2],
									n_verts[ia + j + 1],
									n_verts[ia + j])
				};
				n_faces[a] = new Face(this,
								new int[]{
									a + 1,
									a - 2 * i,
									a - 1
								},
								new int[]{
									(j == 0) ? - 2 * i : a - 2 * i - 2,
									(i == n - 1) ? -(2 * n + 2 * j + 1) : a + 2 * i + 2,
									(j == i - 1) ? -(6 * n - 2 * i - 2) : a - 2 * i + 2
								},
								n_order
				);
			}
		}
		return n_face_vecs;
	}

	private HDVec[] makeVertexes(int n, HDVec[] ne) {
		// This is transforming the interior vertexes, this might be something to
		// make modifiable in future, although currently this is how I want it to
		// be done.
		// Creates the vertexes of the region. The number is just the n+1th triangle number.
		HDVec[] n_verts = new HDVec[(n + 1) * (n + 2) / 2];
		for (int i = 0, ia = 0; i < n + 1; i++, ia += i) {
			// The vectors are set according to their relative position along AB and BC
			// They are then scaled to touch the surface of the planet.
			HDVec iv = ne[0].toHD().scale(i);
			for (int j = 0; j < i + 1; j++) {
				n_verts[ia + j] = ne[1].toHD().scale(j).translate(iv)
								.translate(abc[0]).normalise();
			}
		}
		return n_verts;
	}

	public static class Face {

		final LDRef to_neighbours[][] = new LDRef[3][],
						to_corners[][] = new LDRef[3][], to_lower[], to_upper[] = new LDRef[0];

		final LDVec[] vertex_normals = new LDVec[3],
						vertexes = new LDVec[3], edge_normals = new LDVec[3];

		final int neighbours[], corners[];

		Face(GlobalRegionShape shape, int[] neighbours, int[] corners, int order) {
			this.shape = shape;
			this.neighbours = neighbours;
			this.corners = corners;
			to_lower = new LDRef[order*order];
		}
		private final GlobalRegionShape shape;
		
		public GlobalRegionShape getGlobalRegionShape() {
			return shape;
		}

		public int getNeighbour(int i) {
			return neighbours[i];
		}

		public int getCorner(int i) {
			return corners[i];
		}

		public LDRef[] toNeighbours(int i) {
			return to_neighbours[i];
		}

		public LDRef[] toCorners(int i) {
			return to_corners[i];
		}

		public LDVec getVertex(int i) {
			return vertexes[i];
		}

		public LDVec getVertexNormal(int i) {
			return vertex_normals[i];
		}

		public LDVec getEdgeNormal(int i) {
			return edge_normals[i];
		}
	}
}
