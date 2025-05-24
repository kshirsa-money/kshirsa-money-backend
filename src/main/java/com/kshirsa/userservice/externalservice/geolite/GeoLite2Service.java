package com.kshirsa.userservice.externalservice.geolite;

import com.kshirsa.userservice.UserConstants;
import com.maxmind.geoip2.WebServiceClient;
import com.maxmind.geoip2.model.CityResponse;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.net.InetAddress;

@Service
@Slf4j
@RequiredArgsConstructor
public class GeoLite2Service {

    @Value("${GEOLITE.ACCOUNTID}")
    public int geoliteAccId;

    @Value("${GEOLITE.LICENCE.KEY}")
    public String licenseKey;


    public LocationFromIpResponse getClientLocation(HttpServletRequest request) {
        String ip = request.getHeader(UserConstants.CF_CONNECTING_IP);  // Cloudflare-specific headers

        if (ip == null || ip.isEmpty()) {
            ip = request.getHeader(UserConstants.TRUE_CLIENT_IP);       // Cloudflare-specific headers
        }

        if (ip == null || ip.isEmpty()) {
            ip = request.getHeader(UserConstants.X_FORWARDED_FOR);      // Check for X-Forwarded-For
        }

        if (ip == null || ip.isEmpty()) {
            ip = request.getRemoteAddr();                               // If no headers, fall back to getRemoteAddr()
        } else {
            ip = ip.split(",")[0].trim();                         // In case of multiple proxies, the first one is the real client IP
        }

        return getLocation(ip);
    }

    public LocationFromIpResponse getLocation(String ip) {
        try{
            WebServiceClient client = new WebServiceClient.Builder(geoliteAccId, licenseKey)
                    .host("geolite.info").build();

            InetAddress ipAddress = InetAddress.getByName(ip);
            CityResponse response = client.city(ipAddress);

            return new LocationFromIpResponse((response.getCity().getName() + ", " + response.getCountry().getName()),ip);
        } catch (Exception e) {
            log.error("Error while fetching location from IP: {}", ip);
            log.error(e.getMessage(), e);
            return new LocationFromIpResponse("Unknown", ip);
        }
    }
}