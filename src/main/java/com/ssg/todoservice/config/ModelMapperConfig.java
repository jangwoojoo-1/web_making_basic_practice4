package com.ssg.todoservice.config;

import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration  //해당 클래스가 스프링 빈의 대한 설정을 하는 클래스
public class ModelMapperConfig {

    @Bean // 해당 메서드의 실행 결과로 반환된 객체를 스프링의 빈으로 등록하는 역할
    public ModelMapper getMappper(){
        ModelMapper modelMapper = new ModelMapper();
        modelMapper.getConfiguration().setFieldMatchingEnabled(true)
                .setFieldAccessLevel(org.modelmapper.config.Configuration.AccessLevel.PRIVATE)
                .setMatchingStrategy(MatchingStrategies.STRICT);
        return modelMapper;
    }
}
