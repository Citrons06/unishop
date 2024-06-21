package my.unishop.member.dto;

import lombok.Getter;
import lombok.Setter;
import my.unishop.member.entity.Member;

@Getter @Setter
public class MemberResponseDto {
    private Long id;

    private String username;
    private String password;
    private String memberTel;
    private String memberEmail;

    private String city;
    private String street;
    private String zipcode;

    public MemberResponseDto(Member member) {
        this.id = member.getId();
        this.username = member.getUsername();
        this.password = member.getPassword();
        this.memberTel = member.getMemberTel();
        this.memberEmail = member.getMemberEmail();
        this.street = member.getMemberAddress().getStreet();
        this.city = member.getMemberAddress().getCity();
        this.zipcode = member.getMemberAddress().getZipcode();
    }

    public MemberResponseDto(String mail, String test, String city) {
        this.memberEmail = mail;
        this.username = test;
        this.city = city;
    }
}
