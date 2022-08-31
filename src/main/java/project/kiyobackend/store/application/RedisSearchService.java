package project.kiyobackend.store.application;

import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ZSetOperations;
import org.springframework.stereotype.Service;
import project.kiyobackend.store.presentation.dto.SearchRankingResponseDto;
import project.kiyobackend.store.presentation.dto.store.RecentKeywordRequest;
import project.kiyobackend.store.presentation.dto.store.RecentSearchResponseDto;
import project.kiyobackend.user.domain.User;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class RedisSearchService {

    private final RedisTemplate<String,String> redisTemplate;

    public void addKeywordToRedis(String keyword) {
        try {
            String keyForRanking = "ranking";
            redisTemplate.opsForZSet().incrementScore(keyForRanking, keyword,1);
            //redisTemplate.expire(keyForRanking, 60, TimeUnit.MINUTES);

        } catch (Exception e) {
            System.out.println(e.toString());
        }
    }

    public List<SearchRankingResponseDto> findKeywordSortedByRank() {


        String keyForRanking = "ranking";
        ZSetOperations<String,String> ZSetOperations = redisTemplate.opsForZSet();
        Set<ZSetOperations.TypedTuple<String>> typedTuples = ZSetOperations.reverseRangeWithScores(keyForRanking, 0, 4);
        return typedTuples.stream().map(s -> new SearchRankingResponseDto(s.getValue(),s.getScore())).collect(Collectors.toList());
    }

    public List<RecentSearchResponseDto> findKeyWordSearchedRecently(User currentUser) {
        String keyForRecent;
        if(currentUser != null) {
            keyForRecent = "user:recent:" + currentUser.getUserId();

            ZSetOperations<String, String> ZSetOperations = redisTemplate.opsForZSet();
            Set<ZSetOperations.TypedTuple<String>> typedTuples = ZSetOperations.reverseRangeWithScores(keyForRecent, 0, 4);
            return typedTuples.stream().map(s -> new RecentSearchResponseDto(s.getValue(), keyForRecent, s.getScore().longValue())).collect(Collectors.toList());
        }
        return null;
    }

    public String removeRecentKeyword(RecentKeywordRequest recentKeywordRequest) {
        ZSetOperations<String, String> ZSetOperations = redisTemplate.opsForZSet();
        ZSetOperations.remove(recentKeywordRequest.getKey(),recentKeywordRequest.getKeyword());
        return "success";
    }
}
