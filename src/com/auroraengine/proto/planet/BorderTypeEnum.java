/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.auroraengine.proto.planet;

/**
 *
 * @author Arthur
 */
public enum BorderTypeEnum {
	//<editor-fold defaultstate="collapsed" desc="Diagram ABC">
	// B                                                  C
	//  36  37  38  39  40  41  42  43  44  45  46  47  48
	//      25  26  27  28  29  30  31  32  33  34  35
	//          16  17  18  19  20  21  22  23  24
	//              09  10  11  12  13  14  15
	//                  04  05  06  07  08
	//                      01  02  03
	//                          00
	//                          A
	//</editor-fold>
		BA(1) {
		 @Override
		 int neighbour(int order, int ref) {
				// grabs the triangle on the BA edge (the right), with -1
				// referencing the B corner triangle.
				return (order - 1 + (ref + 1) / 2) * (order - 1 + (ref + 1) / 2) +
								(1 - ref) % 2;
		 }
	//<editor-fold defaultstate="collapsed" desc="Diagram BA">
	// B                                                  C
	//  01  02
	//      03  04
	//          05  06
	//              07  08
	//                  09  10
	//                      11  12
	//                          13
	//                          A
	// </editor-fold>
	}, CB(0) {
			@Override
			int neighbour(int order, int ref) {
					// grabs the triangle on the CB edge (the top), with -1
					// referencing the C corner triangle.
					return order * order + ref;
			}
	//<editor-fold defaultstate="collapsed" desc="Diagram CB">
	// B                                                  C
	//  13  12  11  10  09  08  07  06  05  04  03  02  01
	// 
	// 
	// 
	// 
	// 
	// 
	// 
	//                          A
	// </editor-fold>
	}, AC(2) {
			@Override
			int neighbour(int order, int ref) {
					// grabs the triangle on the AC edge (the left), with -1
					// referencing the A corner triangle.
					return (-ref / 2 + 1) * (-ref / 2 + 1) + (ref + 1) % 2 - 1;
			}
	//<editor-fold defaultstate="collapsed" desc="Diagram AC">
	// B                                                  C
	//                                              12  13
	//                                          10  11
	//                                      08  09
	//                                  06  07
	//                              04  05
	//                          02  03
	//                          01
	//                          A
	// </editor-fold>
	};

	public final int to_ref;
	BorderTypeEnum(int to_ref) {
			this.to_ref = to_ref;
	}

	abstract int neighbour(int order, int ref);
}
