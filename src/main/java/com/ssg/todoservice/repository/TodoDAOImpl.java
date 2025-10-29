package com.ssg.todoservice.repository;

import com.ssg.todoservice.domain.TodoVO;
import com.ssg.todoservice.mapper.TodoMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class TodoDAOImpl implements TodoDAO{
    private final TodoMapper todoMapper;

    @Override
    public void insert(TodoVO todoVO) {
        todoMapper.insert(todoVO);
    }

    @Override
    public List<TodoVO> selectAll() {
        return todoMapper.selectAll();
    }

    @Override
    public TodoVO selectOne(Long tno) {
        return todoMapper.selectOne(tno);
    }

    @Override
    public void delete(Long tno) {
        todoMapper.delete(tno);
    }

    @Override
    public void update(TodoVO todoVO) {
        todoMapper.update(todoVO);
    }
}
