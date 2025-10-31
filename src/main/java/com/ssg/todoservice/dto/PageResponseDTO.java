package com.ssg.todoservice.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.List;

@Getter
@ToString
public class PageResponseDTO<E> { //확장성을 위해 generic
    private int page;
    private int size;
    private int total;

    //시작 페이지번호
    private int start;
    //끝 페이지번호
    private int end;

    //이전 페이지의존재여부
    private boolean prev;
    //다음 페이지의존재여부
    private boolean next;


    private List<E> dtoList;

    @Builder(builderMethodName = "withAll")
    public PageResponseDTO(PageRequestDTO pageRequestDTO,
                           List<E> dtoList, int total){
        this.page = pageRequestDTO.getPage();
        this.size = pageRequestDTO.getSize();
        this.total = total;
        this.dtoList = dtoList;

        // 페이지 번호를 계산하는 수식
        this.end =   (int)(Math.ceil(this.page / 10.0 )) *  10;
        this.start = this.end- 9;
        int last =  (int)(Math.ceil((total/(double)size)));
        this.end =  end > last ? last: end;
        this.prev = this.start > 1;
        this.next =  total > this.end * this.size;
    }

}
