package my.unishop.member;

import my.unishop.member.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MemberRepository extends JpaRepository<Member, Long> {
    Member findByUsername(String username);
    Member findByEmail(String email);

    boolean existsByUsername(String username);
    boolean existsByEmail(String email);
}
