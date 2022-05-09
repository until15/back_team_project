package com.example.filter;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.example.jwt.JwtUtil;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.filter.OncePerRequestFilter;

@WebFilter(urlPatterns = {""})
public class JwtRequestFilter extends OncePerRequestFilter{

    @Autowired JwtUtil jwtutil;

    @Override
    protected void doFilterInternal(
        HttpServletRequest request, 
        HttpServletResponse response, 
        FilterChain filterChain
        
    ) throws ServletException, IOException {
    	
        try {
            // 토큰 불러오기 
            String token = request.getHeader("TOKEN");
            if(token != null) {
                if(token.length() > 0) {
                    // 토큰을 이용해서 아이디를 추출
                    String username = jwtutil.extractUsername(token);

                    // 토큰 검증
                    System.out.println("jwtRequestFilter : " + token);
                    System.out.println("username : " + username);

                    // 컨트롤러로 이동
                    filterChain.doFilter(request, response);
                    System.out.println("필터 체임 : " + filterChain);
                }
            }
            // 토큰이 없을시 오류 발생
            else {
                throw new Exception("토큰 없음 !");
            }
        }
        // 오류 발생
        catch (Exception e) {
            e.printStackTrace();
            response.sendError(-1, "토큰오류 !");
        }

        
    }
    
}
