package io.neond.genesis.domain.repository;

import io.neond.genesis.domain.dto.response.SearchNicknameDto;
import io.neond.genesis.domain.dto.response.FullMemberDto;
import io.neond.genesis.domain.entity.Member;
import io.neond.genesis.domain.entity.Role;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface MemberRepository extends JpaRepository<Member, Long> {
    Optional<Member> findByMemberId(String memberId);
    Optional<Member> findByUuid(String uuid);
    boolean existsByMemberId(String memberId);
    boolean existsByNickname(String nickname);
    List<SearchNicknameDto> findByNicknameContainsAndRoles(String nickname, Role role);
    List<SearchNicknameDto> findAllByRoles(Role role);
    List<FullMemberDto> findMembersByRolesNotContains(Role role);
    List<FullMemberDto> findMembersByRolesNotContainsAndNicknameContains(Role role, String nickname);
    List<FullMemberDto> findByRoles(Role role);
    List<FullMemberDto> findByNicknameContainingAndRoles(String nickname, Role role);
}
