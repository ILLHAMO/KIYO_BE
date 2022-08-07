package project.kiyobackend.common.util.auth;

import javax.servlet.http.HttpServletRequest;

public class HeaderUtil {

    // JWT는 HTTP HEADER의 Authorization에 들어간다.
    private final static String HEADER_AUTHORIZATION = "Authorization";
    // JWT는 Bearer 라는 접두사를 붙여서 보내진다.
    private final static String TOKEN_PREFIX = "Bearer ";

    public static String getAccessToken(HttpServletRequest request)
    {
        String headerValue = request.getHeader(HEADER_AUTHORIZATION);
        System.out.println("무슨 값이 있찌? : " + headerValue);
        // 만약 Authorization 헤더에 값이 없으면 예외 상황
        if(headerValue == null) {
            return null;
        }


        // 만약 Bearer로 시작한다면
        if(headerValue.startsWith(TOKEN_PREFIX))
        {
            // 전체 문장에서 Bearer 뒤에꺼 파싱해서 JWT값만 빼낸다.
            String substring = headerValue.substring(TOKEN_PREFIX.length());
            if(substring == "null" || substring == null)
            {
                return null;
            }
            return substring;
        }


        return null;
    }
}
