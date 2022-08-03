package project.kiyobackend.store.application;

import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ZSetOperations;
import org.springframework.stereotype.Service;
import project.kiyobackend.store.adapter.presentation.dto.SearchRankingResponseDto;
import project.kiyobackend.store.adapter.presentation.dto.store.RecentKeywordRequest;
import project.kiyobackend.store.adapter.presentation.dto.store.RecentSearchResponseDto;
import project.kiyobackend.user.domain.User;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class RedisSearchService {

    private final RedisTemplate<String,String> redisTemplate;

    public void addKeywordToRedis(User currentUser, String keyword) {
        try {
            // 검색을 하면 해당검색어를 value에 저장하고, score를 1 준다
            String keyForRanking = "user:ranking:"+currentUser.getUserId();
            String keyForRecent = "user:recent:"+currentUser.getUserId();
            redisTemplate.opsForZSet().incrementScore(keyForRanking, keyword,1);
            // 검색을 하면 해당 겁색어를 value에 저장하고, 조회한 나노시간으로 업데이트 한다.
            redisTemplate.opsForZSet().add(keyForRecent,keyword,(double) System.currentTimeMillis());
        } catch (Exception e) {
            System.out.println(e.toString());
        }
    }

    public List<SearchRankingResponseDto> findKeywordSortedByRank(User currentUser) {


        String keyForRanking = "user:ranking:"+currentUser.getUserId();
        ZSetOperations<String,String> ZSetOperations = redisTemplate.opsForZSet();
        Set<ZSetOperations.TypedTuple<String>> typedTuples = ZSetOperations.reverseRangeWithScores(keyForRanking, 0, 4);
        return typedTuples.stream().map(s -> new SearchRankingResponseDto(s.getValue(),s.getScore())).collect(Collectors.toList());
    }

    public List<RecentSearchResponseDto> findKeyWordSearchedRecently(User currentUser) {
        String keyForRecent;
        if(currentUser != null)
        {
             keyForRecent =  "user:recent:"+currentUser.getUserId();
        }
        ZSetOperations<String,String> ZSetOperations = redisTemplate.opsForZSet();
        Set<ZSetOperations.TypedTuple<String>> typedTuples = ZSetOperations.reverseRangeWithScores(keyForRecent, 0, 4);
        return typedTuples.stream().map(s -> new RecentSearchResponseDto(s.getValue(),keyForRecent,s.getScore().longValue())).collect(Collectors.toList());

    }

    public String removeRecentKeyword(RecentKeywordRequest recentKeywordRequest) {
        ZSetOperations<String, String> ZSetOperations = redisTemplate.opsForZSet();
        ZSetOperations.remove(recentKeywordRequest.getKey(),recentKeywordRequest.getKeyword());
        return "success";
    }
}
