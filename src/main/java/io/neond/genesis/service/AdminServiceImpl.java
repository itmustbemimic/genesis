package io.neond.genesis.service;

import io.neond.genesis.domain.repository.MemberRepository;
import io.neond.genesis.domain.repository.RoleRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
@Slf4j
public class AdminServiceImpl implements AdminService{

    private final MemberRepository memberRepository;
    private final RoleRepository roleRepository;


}
