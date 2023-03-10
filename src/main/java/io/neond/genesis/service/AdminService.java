package io.neond.genesis.service;

import io.neond.genesis.domain.entity.Member;
import org.springframework.http.ResponseEntity;

import java.util.Optional;

public interface AdminService {
    ResponseEntity verifyQrToken(String qrToken);
}
