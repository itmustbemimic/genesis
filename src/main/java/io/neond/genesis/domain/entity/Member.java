package io.neond.genesis.domain.entity;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Member {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String uuid;

    @Schema(example = "itmustbemimic")
    private String memberId;

    private String password;

    @Schema(example = "안진기")
    private String name;

    @Schema(example = "혀가입안에갇혔어요")
    private String nickname;

    @Schema(example = "01033332222")
    private String phone;

    @Schema(example = "961112")
    private String birth;

    @Schema(example = "male")
    private String gender;

    @ManyToMany
    private List<Role> roles = new ArrayList<>();

    @OneToOne
    @JoinColumn(unique = true, name = "ticket_id")
    private Ticket ticket;

    private String refreshToken;

    private String registerDate;

    private String fcmToken;

    public void updateNickname(String newNickname) {
        this.nickname = newNickname;
    }

    public void updateRefreshToken(String newToken) {
        this.refreshToken = newToken;
    }
}
