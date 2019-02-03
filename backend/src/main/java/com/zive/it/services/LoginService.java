package com.zive.it.services;

import com.google.common.io.CharStreams;
import com.zive.it.utils.Constants;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.security.Permission;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;


@Service
public class LoginService {

    public ResponseEntity<HashMap<String,Object>> authenticateUser(String username, String password) throws IOException {
        String stringUrl = Constants.fullAtlasianUrl;

        URL obj = new URL(stringUrl + "/?os_authType=basic");
        HttpURLConnection con = (HttpURLConnection) obj.openConnection();

        con.setRequestMethod("GET");

        con.setRequestProperty("X-Requested-With", "Curl");
        String userpass = username + ":" + password;
        String basicAuth = "Basic " + new String(Base64.getEncoder().encode(userpass.getBytes()));
        con.setRequestProperty("Authorization", basicAuth);
        if (username == null || password == null)
            return new ResponseEntity(HttpStatus.UNAUTHORIZED);
        int responseCode = con.getResponseCode();
        if (responseCode == 401) {
            return new ResponseEntity(HttpStatus.UNAUTHORIZED);
        }
        Document doc = Jsoup.parse(con.getInputStream(),null,stringUrl);
        Elements metaTags = doc.getElementsByTag("meta");
        Map<String, String> metas = new HashMap<>();

        for (Element metaTag : metaTags) {
            String content = metaTag.attr("content");
            String name = metaTag.attr("name");
            metas.put(name,content);
        }

        Object metaUsername = metas.get("ajs-remote-user");
        Object metaAccountId = metas.get("ajs-atlassian-account-id");
        Object metaFullName = metas.get("ajs-remote-user-fullname");
        HashMap<String,Object> claims = new HashMap<>();
        claims.put("username",metaUsername);
        claims.put("account_id",metaAccountId);
        claims.put("display_name",metaFullName);


        con.disconnect();
        if (responseCode == 200) {
                return new ResponseEntity(claims,HttpStatus.OK);
        }
        else
            return new ResponseEntity(claims,HttpStatus.FORBIDDEN);
    }
}
