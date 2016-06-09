package com.auroraengine.map.grid;


public class DivisibleGridElement extends GridElement {
	public DivisibleGridElement(Grid<GridElement> grid) {
		super(grid);
		parent = null;
		children = new DivisibleGridElement[1 << grid.getDimension()];
		scale = 0;
	}
	public DivisibleGridElement(DivisibleGridElement parent, int[] pos) {
		super(parent.getGrid());
		this.parent = parent;
		this.children = new DivisibleGridElement[1 << getGrid().getDimension()];
		this.scale = parent.scale + 1;
		this.setPosition(pos, Grid::func_check_unoccupied);
	}
	private DivisibleGridElement parent;
	private final DivisibleGridElement[] children;
	private final int scale;

	public int getScale() {
		return scale;
	}
	public DivisibleGridElement getParent() {
		return parent;
	}
}
