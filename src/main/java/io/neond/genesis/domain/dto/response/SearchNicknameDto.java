package io.neond.genesis.domain.dto.response;

import lombok.Builder;

@Builder
public class SearchNicknameDto {
    private String nickname;
    private String uuid;

}
