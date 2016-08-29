package com.atoz.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.atoz.dao.studentMapper;
import com.atoz.model.student;
import com.atoz.service.IStudentService;
@Service("StudentServiceImpl")
public class StudentServiceImpl implements IStudentService {

	@Autowired
	private studentMapper studentMapper;

	public student getStudentById(int sId) {
		return studentMapper.selectByPrimaryKey(sId);
	}
}
