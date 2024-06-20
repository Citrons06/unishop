package my.unishop.member.repository;

import my.unishop.member.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MemberRepository extends JpaRepository<Member, Long> {
    Member findByUsername(String username);
    Member findByMemberEmail(String email);

    boolean existsByUsername(String username);
    boolean existsByMemberEmail(String email);
}
