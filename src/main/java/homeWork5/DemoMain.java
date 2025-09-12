package homeWork5;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;

public class DemoMain {
    public static void main(String[] args) {
        EntityManager em = JpaUtil.emf().createEntityManager();
        EntityTransaction tx = em.getTransaction();

        try {
            tx.begin();
            User u = new User();
            u.setUsername("user_1");

            Note n1 = new Note();
            n1.setText("Перввая заметка");
            u.addNote(n1);

            Note n2 = new Note();
            n2.setText("Вторая заметка");
            u.addNote(n2);

            em.persist(u);
            em.flush();
            tx.commit();

            em.clear();

            tx.begin();
            User loaded = em.createQuery(
                            "select u from User u where u.username = :un", User.class)
                    .setParameter("un", "user_1")
                    .getSingleResult();

            // На этом месте коллекция notes ещё LAZY (не загружена)
            System.out.println("Loaded user id=" + loaded.getId() + ", username=" + loaded.getUsername());

            // Первый доступ к size() вызовет отдельный SELECT по notes
            int notesCount = loaded.getNotes().size();
            System.out.println("Notes count = " + notesCount);
            tx.commit();

            tx.begin();
            loaded.removeNote(loaded.getNotes().get(0));
            em.persist(loaded);
            em.flush();
            tx.commit();

            tx.begin();
            User u2 = new User();
            u2.setUsername("user2");

            u2.addNote(new Note().withText("sdsd"));
            u2.addNote(new Note().withText("sdsdwe3"));

            em.persist(u2);
            em.flush();
            tx.commit();

            tx.begin();
            em.remove(u2);
            tx.commit();

        } finally {
            em.close();
            JpaUtil.close();
        }
    }
}