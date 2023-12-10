package jpql.jpqlList;

import jpql.Member;
import jpql.Team;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import javax.persistence.*;
import javax.transaction.Transactional;
import java.util.List;

@Transactional
public class JpqlTest {

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

    @Test
    @DisplayName("조인 예시")
    void joinTest() {
        tx.begin();
        try {
            // 멤버 생성

            Team team = new Team();
            team.setName("teamA");
            em.persist(team);

            Member member = new Member();
            member.setUsername("member1");
            member.setTeam(team);
            em.persist(member);

            em.flush();
            em.clear();

            // 파라미터 바인딩 사용
            List<Member> resultList = em.createQuery("select m from Member m inner join m.team t", Member.class)
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
