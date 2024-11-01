package com.devrace.domain.log.controller.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class  EditLogDto {

        private final String address;
        private final String title;
        private final String content;

}
