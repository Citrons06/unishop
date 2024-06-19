package my.unishop.member.dto;

import lombok.Getter;
import lombok.Setter;
import my.unishop.member.entity.Member;

@Getter @Setter
public class MemberResponseDto {
    private Long id;

    private String username;
    private String password;
    private String tel;
    private String email;

    private String city;
    private String street;
    private String zipcode;

    public MemberResponseDto(Member member) {
        this.id = member.getId();
        this.username = member.getUsername();
        this.password = member.getPassword();
        this.tel = member.getTel();
        this.email = member.getEmail();
        this.street = member.getAddress().getStreet();
        this.city = member.getAddress().getCity();
        this.zipcode = member.getAddress().getZipcode();
    }
}
