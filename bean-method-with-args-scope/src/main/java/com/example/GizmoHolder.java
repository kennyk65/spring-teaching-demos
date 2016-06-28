package com.example;

public class GizmoHolder {

	private Gizmo gizmo;
	public GizmoHolder(Gizmo gizmo) {
		this.gizmo = gizmo;
	}
	@Override
	public String toString() {
		return gizmo.toString();
	}
	
	
}
