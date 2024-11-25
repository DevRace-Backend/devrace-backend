package com.devrace.domain.log.controller.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class  EditLogDto {

        private String address;
        private String title;
        private String content;
        private Boolean isPublic;

}
