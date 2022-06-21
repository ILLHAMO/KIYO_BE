package project.kiyobackend.auth.entity;

import lombok.Getter;

// 일단 구글만 테스트
@Getter
public enum ProviderType {
    GOOGLE,
    LOCAL
}
