package com.atoz.dao;

import com.atoz.model.student;

public interface studentMapper {
    int deleteByPrimaryKey(Integer sId);

    int insert(student record);

    int insertSelective(student record);

    student selectByPrimaryKey(Integer sId);

    int updateByPrimaryKeySelective(student record);

    int updateByPrimaryKey(student record);
}