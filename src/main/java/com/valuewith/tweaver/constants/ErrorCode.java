package com.valuewith.tweaver.constants;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum ErrorCode {
    NO_FILE_UPLOAD("추가된 파일이 없습니다.", HttpStatus.BAD_REQUEST),
    INVALID_FILE_MEDIA_TYPE("JPG, JPEG, PNG 형식만 가능합니다.", HttpStatus.UNSUPPORTED_MEDIA_TYPE),
    S3_IMAGE_NOT_FOUND("이미지 저장소에서 파일을 찾을 수 없습니다.", HttpStatus.INTERNAL_SERVER_ERROR),
    URL_IS_EMPTY("제공된 URL이 없습니다.", HttpStatus.BAD_REQUEST),
    LOCATION_NAME_NOT_FOUNT("지역 이름은 필수입니다.", HttpStatus.BAD_REQUEST),

    // ======== Auth ========
    // 401
    INVALID_CODE("만료된 코드 입니다.", HttpStatus.UNAUTHORIZED),
    INCORRECT_CODE("인증코드가 다릅니다.", HttpStatus.UNAUTHORIZED),
    INVALID_JWT("잘못된 인증 정보입니다.", HttpStatus.UNAUTHORIZED),
    NOT_A_MEMBER("해당 그룹원이 아닙니다.", HttpStatus.UNAUTHORIZED),
    // 404
    MEMBER_NOT_FOUND("회원을 찾을 수 없습니다.", HttpStatus.NOT_FOUND),
    // 409
    DUPLICATE_EMAIL("중복된 이메일입니다.", HttpStatus.CONFLICT),
    // 502
    FAILURE_SENDING_EMAIL("이메일 전송에 실패하였습니다.", HttpStatus.BAD_GATEWAY),
    FAILURE_GETTING_PROFILE_IMG("프로필 이미지가 없습니다.", HttpStatus.BAD_GATEWAY),

    ;


    private final String description;
    private final HttpStatus httpStatus;

}
