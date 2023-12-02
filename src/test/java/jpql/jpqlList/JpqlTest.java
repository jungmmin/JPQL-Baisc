package jpql.jpqlList;

import jpql.Member;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import javax.persistence.*;
import javax.transaction.Transactional;
import java.util.List;

@Transactional
class JpqlTest {

    EntityManagerFactory emf = Persistence.createEntityManagerFactory("hello");

    EntityManager em = emf.createEntityManager();
    EntityTransaction tx = em.getTransaction();

    @Test
    @DisplayName("파라미터 바인딩 예시")
    void paramBindingEx() {
        tx.begin();
        try {
            // 멤버 생성
            Member member = new Member();
            member.setUsername("member1");
            member.setAge(10);
            em.persist(member);

            // 파라미터 바인딩 사용
            TypedQuery<Member> query = em.createQuery("select m from Member m where m.username = :username", Member.class);
            query.setParameter("username", "member1");

            // 결과 출력
            List<Member> resultList = query.getResultList();
            System.out.println("===== 파라미터 바인딩 테스트 =====");
            for (Member member1 : resultList) {
                System.out.println("member1 = " + member1.getUsername());
            }
            System.out.println("=============================");

            // 롤백으로 데이터 초기화
            tx.rollback();
        } catch (Exception e) {
            tx.rollback();
        }finally {
            em.close();
        }

    }

}