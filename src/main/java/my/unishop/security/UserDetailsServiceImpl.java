package my.unishop.security;

import lombok.extern.slf4j.Slf4j;
import my.unishop.member.entity.Member;
import my.unishop.member.repository.MemberRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    private final MemberRepository userRepository;

    public UserDetailsServiceImpl(MemberRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Member user = userRepository.findByUsername(username);
        if (user == null) {
            log.info("사용자를 찾을 수 없습니다. : " + username);
            throw new UsernameNotFoundException(username);
        }
        return new UserDetailsImpl(user);
    }
}
