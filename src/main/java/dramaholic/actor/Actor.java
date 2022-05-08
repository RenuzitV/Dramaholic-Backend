package dramaholic.actor;

import org.hibernate.annotations.GenericGenerator;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import java.io.Serializable;
import java.util.Objects;

@Entity
public class Actor implements Serializable {
    @Id
    @GenericGenerator(name = "id", strategy = "dramaholic.actor.ActorSequenceGen")
    @GeneratedValue(generator = "id")
    @Column(nullable = false)
    private Long id;
    private String name;
    private String image;
    private String character;

    public Actor(Long id, String name, String image, String character) {
        this.id = id;
        this.name = name;
        this.image = image;
        this.character = character;
    }

    public Actor() {

    }

    @Override
    public String toString() {
        return "Actor{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", image='" + image + '\'' +
                ", character='" + character + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Actor actor = (Actor) o;
        return id.equals(actor.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCharacter() {
        return character;
    }

    public void setCharacter(String character) {
        this.character = character;
    }
}
