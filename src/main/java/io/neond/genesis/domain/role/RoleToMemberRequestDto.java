package io.neond.genesis.domain.role;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class RoleToMemberRequestDto {
    private String memberId;
    private String roleName;
}
