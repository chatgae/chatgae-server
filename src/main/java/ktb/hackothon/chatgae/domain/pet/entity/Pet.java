package ktb.hackothon.chatgae.domain.pet.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;
import lombok.experimental.FieldDefaults;
import java.time.LocalDate;
import jakarta.persistence.*;

@Entity
@Table(name = "Pets")
@Getter
@Setter
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
    LocalDate regDt;


    @PrePersist
    protected  void onCreate(){
        this.regDt = LocalDate.now();
    }
}
