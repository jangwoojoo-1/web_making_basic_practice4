package com.ssg.todoservice.service;

import com.ssg.todoservice.domain.TodoVO;
import com.ssg.todoservice.dto.TodoDTO;
import com.ssg.todoservice.repository.TodoDAO;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Log4j2
public class TodoServiceImpl implements TodoService{
    private final TodoDAO todoDAO;
    private final ModelMapper modelMapper;

    @Override
    @Transactional
    public void register(TodoDTO todoDTO) {
        log.info(modelMapper);
        TodoVO todoVO = modelMapper.map(todoDTO, TodoVO.class);
        log.info(todoVO);
        todoDAO.insert(todoVO);
    }

    @Override
    @Transactional
    public List<TodoDTO> getAll(){
        log.info(modelMapper);
        List<TodoVO> voList = todoDAO.selectAll();
        log.info(voList);
        List<TodoDTO> dtoList = voList.stream().map(TodoVO -> {
            return modelMapper.map(TodoVO, TodoDTO.class);
        }).collect(Collectors.toList());
        return dtoList;
    }

    @Override
    @Transactional
    public TodoDTO getOne(Long tno) {
        TodoVO todoVO = todoDAO.selectOne(tno);
        TodoDTO todoDTO = modelMapper.map(todoVO, TodoDTO.class);
        return todoDTO;
    }

    @Override
    @Transactional
    public void remove(Long tno) {
        todoDAO.delete(tno);
    }

    @Override
    public void modify(TodoDTO todoDTO) {
        TodoVO todoVO = modelMapper.map(todoDTO, TodoVO.class);
        todoDAO.update(todoVO);
    }
}

