package my.unishop.member.dto;

import lombok.Getter;
import lombok.Setter;
import my.unishop.member.entity.Member;
import my.unishop.member.entity.UserRole;

@Getter @Setter
public class MemberRequestDto {
    private String username;
    private String password;
    private String tel;
    private String email;

    private String city;
    private String street;
    private String zipcode;

    private UserRole role = UserRole.USER;

    public MemberRequestDto(String username, String password, String tel, String email, String city, String street, String zipcode, UserRole role) {
        this.username = username;
        this.password = password;
        this.tel = tel;
        this.email = email;
        this.city = city;
        this.street = street;
        this.zipcode = zipcode;
        this.role = role;
    }

    public Member toEntity() {
        return new Member(this);
    }
}
