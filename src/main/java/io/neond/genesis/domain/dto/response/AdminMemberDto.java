package io.neond.genesis.domain.dto.response;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class AdminMemberDto {
    private String memberId;
    private String name;
    private String nickname;
    private String phone;
    private String uuid;
    private String registerDate;
}
