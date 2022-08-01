package project.kiyobackend.store.application;

import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ZSetOperations;
import org.springframework.stereotype.Service;
import project.kiyobackend.store.adapter.presentation.dto.SearchRankingResponseDto;
import project.kiyobackend.store.adapter.presentation.dto.store.RecentSearchResponseDto;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class RedisSearchService {

    private final RedisTemplate<String,String> redisTemplate;

    public void addKeywordToRedis(String keyword) {
        try {
            // 검색을 하면 해당검색어를 value에 저장하고, score를 1 준다
            redisTemplate.opsForZSet().incrementScore("ranking", keyword,1);
            // 검색을 하면 해당 겁색어를 value에 저장하고, 조회한 나노시간으로 업데이트 한다.
            redisTemplate.opsForZSet().add("recent",keyword,(double) System.currentTimeMillis());
        } catch (Exception e) {
            System.out.println(e.toString());
        }
    }

    public List<SearchRankingResponseDto> findKeywordSortedByRank() {


        String key = "ranking";
        ZSetOperations<String,String> ZSetOperations = redisTemplate.opsForZSet();
        Set<ZSetOperations.TypedTuple<String>> typedTuples = ZSetOperations.reverseRangeWithScores(key, 0, 4);
        return typedTuples.stream().map(s -> new SearchRankingResponseDto(s.getValue(),s.getScore())).collect(Collectors.toList());
    }

    public List<RecentSearchResponseDto> findKeyWordSearchedRecently() {
        String key = "recent";
        ZSetOperations<String,String> ZSetOperations = redisTemplate.opsForZSet();
        Set<ZSetOperations.TypedTuple<String>> typedTuples = ZSetOperations.reverseRangeWithScores(key, 0, 4);
        return typedTuples.stream().map(s -> new RecentSearchResponseDto(s.getValue(),s.getScore().longValue())).collect(Collectors.toList());

    }

}
