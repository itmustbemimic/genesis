package io.neond.genesis.domain.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class RoleToMemberRequestDto {
    @Schema(example = "7a034705-7207-4791-9d0a-1e8371008b4b")
    private String uuid;
}
