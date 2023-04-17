package io.neond.genesis.domain.dto.response;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class FullMemberDto {
    private String name;
    private String phone;
    private String memberId;
    private String nickname;
    private String registerDate;
    private String uuid;
}
