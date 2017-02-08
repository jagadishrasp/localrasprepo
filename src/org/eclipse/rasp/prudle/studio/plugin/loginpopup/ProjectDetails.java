package org.eclipse.rasp.prudle.studio.plugin.loginpopup;

import java.util.Date;

public class ProjectDetails {
	
	private String OwnerEmail ;
	private String ProjectId ;
	private Long OwnerId ;
	private String ProjectName ;
	private String LangId ;
	private Date SubmitionDate ;
	private String FilePath ;
	private String Status ;
	private Integer Source ;
	private String Destination ;
	private String fpath ;
	
	public ProjectDetails(){
		
	}
	
	public String getOwnerEmail() {
		return OwnerEmail;
	}
	public void setOwnerEmail(String ownerEmail) {
		OwnerEmail = ownerEmail;
	}
	public String getProjectId() {
		return ProjectId;
	}
	public void setProjectId(String projectId) {
		ProjectId = projectId;
	}
	public Long getOwnerId() {
		return OwnerId;
	}
	public void setOwnerId(Long ownerId) {
		OwnerId = ownerId;
	}
	public String getProjectName() {
		return ProjectName;
	}
	public void setProjectName(String projectName) {
		ProjectName = projectName;
	}
	public String getLangId() {
		return LangId;
	}
	public void setLangId(String langId) {
		LangId = langId;
	}
	public Date getSubmitionDate() {
		return SubmitionDate;
	}
	public void setSubmitionDate(Date submitionDate) {
		SubmitionDate = submitionDate;
	}
	public String getFilePath() {
		return FilePath;
	}
	public void setFilePath(String filePath) {
		FilePath = filePath;
	}
	public String getStatus() {
		return Status;
	}
	public void setStatus(String status) {
		Status = status;
	}
	public Integer getSource() {
		return Source;
	}
	public void setSource(Integer source) {
		Source = source;
	}
	public String getDestination() {
		return Destination;
	}
	public void setDestination(String destination) {
		Destination = destination;
	}
	public String getFpath() {
		return fpath;
	}
	public void setFpath(String fpath) {
		this.fpath = fpath;
	}

}
