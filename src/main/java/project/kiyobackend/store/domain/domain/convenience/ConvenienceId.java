package project.kiyobackend.store.domain.domain.convenience;

import project.kiyobackend.store.domain.domain.category.CategoryId;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.io.Serializable;
import java.util.Objects;

@Embeddable
@Access(AccessType.FIELD)
public class ConvenienceId implements Serializable {

    @Column(name = "convenience_id")
    private Long value;

    protected ConvenienceId() {
    }

    public ConvenienceId(Long value) {
        this.value = value;
    }

    public Long getValue() {
        return value;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ConvenienceId that = (ConvenienceId) o;
        return Objects.equals(value, that.value);
    }

    @Override
    public int hashCode() {
        return value != null ? value.hashCode() : 0;
    }

    public static ConvenienceId of(Long value) {
        return new ConvenienceId(value);
    }
}
