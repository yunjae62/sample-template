package ex.sample.global.response;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties // 아무 필드가 없을 시 직렬화를 위한 어노테이션
public record CommonEmptyRes() {

}