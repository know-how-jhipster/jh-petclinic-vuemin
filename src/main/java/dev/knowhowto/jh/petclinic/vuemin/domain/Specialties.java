package dev.knowhowto.jh.petclinic.vuemin.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.*;
import javax.validation.constraints.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A Specialties.
 */
@Entity
@Table(name = "specialties")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Specialties implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @NotNull
    @Size(max = 32)
    @Column(name = "name", length = 32, nullable = false)
    private String name;

    @ManyToMany
    @JoinTable(
        name = "rel_specialties__vet",
        joinColumns = @JoinColumn(name = "specialties_id"),
        inverseJoinColumns = @JoinColumn(name = "vet_id")
    )
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "specialties" }, allowSetters = true)
    private Set<Vets> vets = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Specialties id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return this.name;
    }

    public Specialties name(String name) {
        this.setName(name);
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Set<Vets> getVets() {
        return this.vets;
    }

    public void setVets(Set<Vets> vets) {
        this.vets = vets;
    }

    public Specialties vets(Set<Vets> vets) {
        this.setVets(vets);
        return this;
    }

    public Specialties addVet(Vets vets) {
        this.vets.add(vets);
        vets.getSpecialties().add(this);
        return this;
    }

    public Specialties removeVet(Vets vets) {
        this.vets.remove(vets);
        vets.getSpecialties().remove(this);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Specialties)) {
            return false;
        }
        return id != null && id.equals(((Specialties) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Specialties{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            "}";
    }
}
