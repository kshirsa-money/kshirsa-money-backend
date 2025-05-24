package com.kshirsa.userservice.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class RefreshToken {

	
	private String userId;
	@Id
	private String token;
	private Instant expirationDate;
	private String deviceId;
	
}
