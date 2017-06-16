package com.briup.run.common.bean;

import java.util.HashSet;
import java.util.Set;

/**
 * Graderecord entity.
 * 
 * @author MyEclipse Persistence Tools
 */

public class Graderecord implements java.io.Serializable {

	// Fields

	private int id;
	private Long minpoint;
	private Long maxpoint;
	private String gradename;
	private String iconpath;
	private Set memberinfos = new HashSet(0);

	// Constructors

	/** default constructor */
	public Graderecord() {
	}

	/** minimal constructor */
	public Graderecord(Long minpoint, Long maxpoint, String gradename,
			String iconpath) {
		this.minpoint = minpoint;
		this.maxpoint = maxpoint;
		this.gradename = gradename;
		this.iconpath = iconpath;
	}

	/** full constructor */
	public Graderecord(Long minpoint, Long maxpoint, String gradename,
			String iconpath, Set memberinfos) {
		this.minpoint = minpoint;
		this.maxpoint = maxpoint;
		this.gradename = gradename;
		this.iconpath = iconpath;
		this.memberinfos = memberinfos;
	}

	// Property accessors

	

	public Long getMinpoint() {
		return this.minpoint;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public void setMinpoint(Long minpoint) {
		this.minpoint = minpoint;
	}

	public Long getMaxpoint() {
		return this.maxpoint;
	}

	public void setMaxpoint(Long maxpoint) {
		this.maxpoint = maxpoint;
	}

	public String getGradename() {
		return this.gradename;
	}

	public void setGradename(String gradename) {
		this.gradename = gradename;
	}

	public String getIconpath() {
		return this.iconpath;
	}

	public void setIconpath(String iconpath) {
		this.iconpath = iconpath;
	}

	public Set getMemberinfos() {
		return this.memberinfos;
	}

	public void setMemberinfos(Set memberinfos) {
		this.memberinfos = memberinfos;
	}

	@Override
	public String toString() {
		return "Graderecord [id=" + id + ", minpoint=" + minpoint + ", maxpoint=" + maxpoint + ", gradename="
				+ gradename + ", iconpath=" + iconpath + ", memberinfos=" + memberinfos + "]";
	}
	
}