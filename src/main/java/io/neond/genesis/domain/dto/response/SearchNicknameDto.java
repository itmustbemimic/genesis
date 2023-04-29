package io.neond.genesis.domain.dto.response;

import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class SearchNicknameDto {
    private String nickname;
    private String uuid;

}
