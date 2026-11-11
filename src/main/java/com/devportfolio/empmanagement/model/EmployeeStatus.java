package com.devportfolio.empmanagement.model;

public enum EmployeeStatus {
	
	ACTIVE("Active"),
	ON_LEAVE("on Leave"),
	INACTIVE("Inactive");
	
	private final String displayName;

	private EmployeeStatus(String displayName) {
		this.displayName = displayName;
	}

	public String getDisplayName() {
		return displayName;
	}
	
	

}
