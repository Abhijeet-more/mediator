package com.command.mediator.webservice.form;

import javax.persistence.Column;

import com.fasterxml.jackson.annotation.JsonProperty;

public class NeoImageForm {
	
	@JsonProperty("image_name")
	private String imageName;
	
	@JsonProperty("description")
	private String description;
	
	@JsonProperty("mount_path")
	private String mountPath;
	
	@JsonProperty("iso_path")
	private String isoPath;

	public String getImageName() {
		return imageName;
	}

	public void setImageName(String name) {
		this.imageName = name;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getMountPath() {
		return mountPath;
	}

	public void setMountPath(String mountPath) {
		this.mountPath = mountPath;
	}

	public String getIsoPath() {
		return isoPath;
	}

	public void setIsoPath(String isoPath) {
		this.isoPath = isoPath;
	}
	
}