package com.tyiti.easycommerce.util;

public class TreeModel {
	private int id;
	private int parentId;
	private String text;
	private String code;
	private String icon;
	private int status;
	private boolean hasTemplate;
	private Object nodes;
	private Integer treeDepth;

	public String getIcon() {
		return icon;
	}

	public void setIcon(String icon) {
		this.icon = icon;
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	private int rank;

	public int getRank() {
		return rank;
	}

	public void setRank(int rank) {
		this.rank = rank;
	}

	public int getId() {
		return id;
	}

	public int getParentId() {
		return parentId;
	}

	public void setParentId(int parentId) {
		this.parentId = parentId;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}

	public Object getNodes() {
		return nodes;
	}

	public void setNodes(Object nodes) {
		this.nodes = nodes;
	}

	public boolean isHasTemplate() {
		return hasTemplate;
	}

	public void setHasTemplate(boolean hasTemplate) {
		this.hasTemplate = hasTemplate;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public Integer getTreeDepth() {
		return treeDepth;
	}

	public void setTreeDepth(Integer treeDepth) {
		this.treeDepth = treeDepth;
	}

}
