package io.neond.genesis.service;

import org.springframework.http.ResponseEntity;


public interface AdminService {
    ResponseEntity verifyQrToken(String qrToken);
}
