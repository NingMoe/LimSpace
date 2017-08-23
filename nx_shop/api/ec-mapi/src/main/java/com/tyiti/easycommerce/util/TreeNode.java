package com.tyiti.easycommerce.util;

import java.util.ArrayList;
import java.util.List;
/**
 * 
* @ClassName: TreeNode 
* @Description:  
* @author hcy 
* @date 2016年5月18日 上午11:29:16
 */
public class TreeNode {
	private Integer id;
	private String text;
	private String glyphicon;
	private String link;
	private int parentId;
	private boolean checked;
	private CheckState state ;
	private String permIds;
 
 


	public String getPermIds() {
		return permIds;
	}


	public void setPermIds(String permIds) {
		this.permIds = permIds;
	}


	public CheckState getState() {
		return state;
	}


	public void setState(CheckState state) {
		this.state = state;
	}


	public boolean isChecked() {
		return checked;
	}


	public void setChecked(boolean checked) {
		this.checked = checked;
	}
	private List nodes=new ArrayList();
	
	public TreeNode() {
		super();
	}
	
	
	public TreeNode(Integer id, String text, String glyphicon, String link,
			int parentId, List nodes) {
		super();
		this.id = id;
		this.text = text;
		this.glyphicon = glyphicon;
		this.link = link;
		this.parentId = parentId;
		this.nodes = nodes;
	}


	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	
	public String getText() {
		return text;
	}


	public void setText(String text) {
		this.text = text;
	}


	public String getGlyphicon() {
		return glyphicon;
	}
	public void setGlyphicon(String glyphicon) {
		this.glyphicon = glyphicon;
	}
	public String getLink() {
		return link;
	}
	public void setLink(String link) {
		this.link = link;
	}
	public int getParentId() {
		return parentId;
	}
	public void setParentId(int parentId) {
		this.parentId = parentId;
	}
	public List getNodes() {
		return nodes;
	}
	public void setNodes(List nodes) {
		this.nodes = nodes;
	}
	

}

