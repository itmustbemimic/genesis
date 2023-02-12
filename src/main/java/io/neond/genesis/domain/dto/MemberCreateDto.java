package io.neond.genesis.domain.dto;

import io.neond.genesis.domain.entity.Member;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class MemberCreateDto {
    private String memberId;
    private String password;
    private String name;
    private String nickname;
    private String phone;
    private String birth;
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
