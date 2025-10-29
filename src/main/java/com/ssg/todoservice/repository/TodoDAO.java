package com.ssg.todoservice.repository;

import com.ssg.todoservice.domain.TodoVO;

import java.util.List;

public interface TodoDAO {
    void insert(TodoVO todoVO);

    List<TodoVO> selectAll();

    TodoVO selectOne(Long tno);

    void delete(Long tno);

    void update(TodoVO todoVO);
}
