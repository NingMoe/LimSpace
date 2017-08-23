package com.tyiti.easycommerce.service;

import java.util.List;
import com.tyiti.easycommerce.entity.Area;

public interface AreaService {
	List<Area> getAreasByParentIdAndLevel(Integer parentId, Integer level);
}
