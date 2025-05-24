package com.kshirsa.userservice.service;

import com.kshirsa.coreservice.exception.CustomException;
import com.kshirsa.coreservice.exception.ErrorCode;
import com.kshirsa.userservice.UserConstants;
import com.kshirsa.userservice.entity.RefreshToken;
import com.kshirsa.userservice.repository.RefreshTokenRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class RefreshTokenService {

    @Value("#{new Integer(${REFRESH.TOKEN.VALIDITY})}")
    public int REFRESH_TOKEN_VALIDITY;

    private final RefreshTokenRepository refreshTokenRepository;

    public RefreshToken getRefreshToken(String userId, String deviceId) {
        RefreshToken newToken = new RefreshToken(userId, UUID.randomUUID().toString(),
                Instant.now().plusSeconds(REFRESH_TOKEN_VALIDITY), deviceId);
        refreshTokenRepository.save(newToken);
        return newToken;
    }

    public RefreshToken retrieveTokenFromDb(String token) throws CustomException {
        return refreshTokenRepository.findByToken(token).orElseThrow(() ->
                new CustomException(ErrorCode.REFRESH_TOKEN_NOT_FOUND.name()));
    }

    public Instant extendTokenTime(String token) throws CustomException {
        RefreshToken rt = retrieveTokenFromDb(token);
        rt.setExpirationDate(Instant.now().plusSeconds(REFRESH_TOKEN_VALIDITY));
        return refreshTokenRepository.save(rt).getExpirationDate();
    }

    public String tokenValidation(String token, String deviceId) throws CustomException {
        RefreshToken refreshToken = retrieveTokenFromDb(token);

        if (refreshToken.getExpirationDate().isAfter(Instant.now())) {          //Checking if token is expired

            if (refreshToken.getDeviceId().equals(deviceId)) {                  //Checking if token is from same device
                return refreshToken.getUserId();
            } else {
                refreshTokenRepository.delete(refreshToken);
                throw new CustomException(ErrorCode.UNKNOWN_DEVICE.name());
            }

        } else {
            refreshTokenRepository.delete(refreshToken);
            throw new CustomException(ErrorCode.REFRESH_TOKEN_EXPIRED.name());
        }
    }

    public void deleteToken(String token) {
        refreshTokenRepository.deleteById(token);
    }

    @Scheduled(cron = UserConstants.FIXED_DELAY)
    public void cleanup() {
        refreshTokenRepository.findAll().forEach(token -> {
            if (token.getExpirationDate().isBefore(Instant.now())) {
                refreshTokenRepository.delete(token);
                log.info("Refresh Token Repo cleanup executed.");
            }
        });
    }
}
