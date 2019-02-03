package com.zive.it.services;

import java.io.UnsupportedEncodingException;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.Map;

import com.atlassian.jwt.*;
import com.atlassian.jwt.core.http.JavaxJwtRequestExtractor;
import com.atlassian.jwt.core.reader.JwtClaimVerifiersBuilder;
import com.atlassian.jwt.core.reader.JwtIssuerSharedSecretService;
import com.atlassian.jwt.core.reader.JwtIssuerValidator;
import com.atlassian.jwt.core.reader.NimbusJwtReaderFactory;
import com.atlassian.jwt.core.writer.*;
import com.atlassian.jwt.exception.JwtIssuerLacksSharedSecretException;
import com.atlassian.jwt.exception.JwtParseException;
import com.atlassian.jwt.exception.JwtUnknownIssuerException;
import com.atlassian.jwt.exception.JwtVerificationException;
import com.atlassian.jwt.httpclient.CanonicalHttpUriRequest;
import com.atlassian.jwt.reader.JwtClaimVerifier;
import com.atlassian.jwt.reader.JwtReaderFactory;
import com.atlassian.jwt.writer.JwtJsonBuilder;
import com.atlassian.jwt.writer.JwtWriterFactory;
import com.zive.it.utils.Constants;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;

@Service
public class JWTService {

    public String createUriWithJwt(HashMap<String,Object> claims)
            throws UnsupportedEncodingException, NoSuchAlgorithmException {
        long issuedAt = System.currentTimeMillis() / 1000L;
        long expiresAt = issuedAt + 86000000L;
        String sharedSecret = "DB4AEF4719809709E560ED8DE2F9C77B886B963B28BA20E9A8A621BBD4ABA400";    //the sharedsecret key received
        //during the app installation handshake
        String method = "GET";
        String baseUrl = "https://" + Constants.enviroment + ".atlassian.net";
        String contextPath = "/";
        String apiPath = "/rest/api/latest/serverInfo";

        JwtJsonBuilder jwtBuilder = new JsonSmartJwtJsonBuilder()
                .issuedAt(issuedAt)
                .expirationTime(expiresAt)
                .issuer(Constants.appKey)
                .claim("context",claims);

        CanonicalHttpUriRequest canonical = new CanonicalHttpUriRequest(method,
                apiPath, contextPath, new HashMap());
        JwtClaimsBuilder.appendHttpRequestClaims(jwtBuilder, canonical);

        JwtWriterFactory jwtWriterFactory = new NimbusJwtWriterFactory();
        String jwtBuild = jwtBuilder.build();
        String jwtToken = jwtWriterFactory.macSigningWriter(SigningAlgorithm.HS256,
                sharedSecret).jsonToJwt(jwtBuild);

		return jwtToken;
    }

    public Jwt verifyRequest(HttpServletRequest request,
                             JwtIssuerValidator issuerValidator,
                             JwtIssuerSharedSecretService issuerSharedSecretService)
            throws UnsupportedEncodingException, NoSuchAlgorithmException,
            JwtVerificationException, JwtIssuerLacksSharedSecretException,
            JwtUnknownIssuerException, JwtParseException {
        JwtReaderFactory jwtReaderFactory = new NimbusJwtReaderFactory(
                issuerValidator, issuerSharedSecretService);
        JavaxJwtRequestExtractor jwtRequestExtractor = new JavaxJwtRequestExtractor();
        CanonicalHttpRequest canonicalHttpRequest
                = jwtRequestExtractor.getCanonicalHttpRequest(request);
        Map<String, ? extends JwtClaimVerifier> requiredClaims = JwtClaimVerifiersBuilder.build(canonicalHttpRequest);
        String jwt = jwtRequestExtractor.extractJwt(request);

        if (null == jwt)
        {
            throw new IllegalArgumentException("Cannot authenticate a request without a JWT token");
        }
        return jwtReaderFactory.getReader(jwt).readAndVerify(jwt, requiredClaims);
    }
}