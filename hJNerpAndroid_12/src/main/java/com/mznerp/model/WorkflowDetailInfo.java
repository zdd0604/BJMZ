package com.mznerp.model;

import java.util.List;

public class WorkflowDetailInfo {
	public List<Cell> form;
	public List<List<Cell>> grid;

	@Override
	public String toString() {
		return "WorkflowDetailInfo{" +
				"form=" + form +
				", grid=" + grid +
				'}';
	}
}
