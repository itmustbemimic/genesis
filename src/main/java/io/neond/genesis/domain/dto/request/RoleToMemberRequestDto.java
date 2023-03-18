package io.neond.genesis.domain.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class RoleToMemberRequestDto {
    @Schema(example = "itmustbemimic")
    private String memberId;
}
