package my.unishop.user.domain.member.service;

import my.unishop.user.domain.member.dto.MemberRequestDto;
import my.unishop.user.domain.member.dto.MemberResponseDto;

public interface MemberService {
    void signup(MemberRequestDto memberRequestDto);

    boolean verifyEmail(String email, String token);

    MemberResponseDto updateMember(String username, MemberRequestDto memberRequestDto);
}
