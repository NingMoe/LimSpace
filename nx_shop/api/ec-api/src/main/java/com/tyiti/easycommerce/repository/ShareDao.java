package com.tyiti.easycommerce.repository;

import java.util.List;

import com.tyiti.easycommerce.entity.Share;

public interface ShareDao {

	Integer  addShare(Share share);

	Share selectByShare(Share share);

	List<Share> getListByShare(Share share);

}