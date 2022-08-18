package project.kiyobackend.store.adapter.presentation.dto;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AccessLevel;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.SliceImpl;
import project.kiyobackend.store.adapter.presentation.dto.store.StoreResponse;

import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true, value = {"pageable"})
public class RestSlice<T> extends SliceImpl<T> {

    @JsonCreator(mode = JsonCreator.Mode.PROPERTIES)
    public RestSlice(@JsonProperty("content") List<T> content,
                     @JsonProperty("size")  int size,
                     @JsonProperty("last") boolean hasNext)
    {
        super(content,PageRequest.ofSize(size),hasNext);
    }

    // 집어넣을때는 이걸로 집어넣으면 되는데, 문제는 그 이후를 말하는 건가??
    public RestSlice(Slice<T> slice)
    {
        super(slice.getContent(),slice.getPageable(),slice.hasNext());
    }



}
