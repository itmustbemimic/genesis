package io.neond.genesis.domain.dto;

import io.neond.genesis.domain.entity.Member;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class MemberCreateDto {

    @Schema(example = "itmustbemimic")
    private String memberId;

    @Schema(example = "1234")
    private String password;

    @Schema(example = "안진기")
    private String name;

    @Schema(example = "혀가입안에갇혔어요")
    private String nickname;

    @Schema(example = "01033338888")
    private String phone;

    @Schema(example = "961112")
    private String birth;

    @Schema(example = "male")
    private String gender;

    public Member toEntity(){
        return Member.builder()
                .uuid(UUID.randomUUID().toString())
                .memberId(memberId)
                .password(password)
                .name(name)
                .nickname(nickname)
                .phone(phone)
                .birth(birth)
                .gender(gender)
                .build();
    }

    public void encodePassword(String encodedPassword) {
        this.password = encodedPassword;
    }
}
