package com.atoz.dao;

import com.atoz.model.course;

public interface courseMapper {
    int deleteByPrimaryKey(Integer cId);

    int insert(course record);

    int insertSelective(course record);

    course selectByPrimaryKey(Integer cId);

    int updateByPrimaryKeySelective(course record);

    int updateByPrimaryKey(course record);
}