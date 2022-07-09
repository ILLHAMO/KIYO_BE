package project.kiyobackend.store.query.dto;

import lombok.Data;

import java.util.List;

@Data
public class StoreRequestDto {
    List<Long> category;
    List<Long> service;
}
