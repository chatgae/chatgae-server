package ktb.hackothon.chatgae.domain.pet.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;
import lombok.experimental.FieldDefaults;
import java.time.LocalDate;
import java.time.LocalDateTime;

import jakarta.persistence.*;

@Entity
@Table(name = "pets")
@Getter
@Setter
@ToString
@FieldDefaults(level = AccessLevel.PRIVATE)
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class Pet {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "pet_id")
    Long petId;

    @JsonProperty("nickname")
    @Column(name = "nickname")
    String nickname;

    @JsonProperty("breed")
    @Column(name = "breed")
    String breed;

    @JsonProperty("gender")
    @Column(name = "gender")
    String gender;

    @JsonProperty("age")
    @Column(name = "age")
    int age;

    @JsonProperty("birthday")
    @Column(name = "birthday")
    LocalDate birthday;

    @Column(name = "profile")
    String profile;

    @Column(name = "unique_number")
    String uniqueNumber;

    @Column(name = "reg_dt")
    LocalDateTime regDt;


    @PrePersist
    protected  void onCreate(){
        this.regDt = LocalDateTime.now();
    }
}
