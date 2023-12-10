package jpql.jpqlList;

import jpql.Member;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import javax.persistence.*;
import javax.transaction.Transactional;
import java.util.List;

@Transactional
public class JpqlPaging {

    EntityManagerFactory emf = Persistence.createEntityManagerFactory("hello");

    EntityManager em = emf.createEntityManager();
    EntityTransaction tx = em.getTransaction();

    @Test
    @DisplayName("페이징 예시")
    void pagingTest() {
        tx.begin();
        try {
            // 멤버 생성
            for (int i = 0; i < 100; i++) {
                Member member = new Member();
                member.setUsername("member" + i);
                member.setAge(i);
                em.persist(member);
            }


            em.flush();
            em.clear();

            // 파라미터 바인딩 사용
            List<Member> resultList = em.createQuery("select m from Member m order by m.age desc", Member.class)
                    .setFirstResult(0)
                    .setMaxResults(10)
                    .getResultList();

            System.out.println("resultList.size() = " + resultList.size());
            for (Member member1 : resultList) {
                System.out.println("member1 = " + member1);
            }


            // 롤백으로 데이터 초기화
            tx.rollback();
        } catch (Exception e) {
            tx.rollback();
        }finally {
            em.close();
        }

    }
}
