package org.ujar.jh.petclinic.vuemin.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.*;
import javax.validation.constraints.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A Vets.
 */
@Entity
@Table(name = "vets")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Vets implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @NotNull
    @Size(max = 32)
    @Column(name = "firstname", length = 32, nullable = false)
    private String firstname;

    @NotNull
    @Size(max = 32)
    @Column(name = "lastname", length = 32, nullable = false)
    private String lastname;

    @ManyToMany(mappedBy = "vets")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "vets" }, allowSetters = true)
    private Set<Specialties> specialties = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Vets id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getFirstname() {
        return this.firstname;
    }

    public Vets firstname(String firstname) {
        this.setFirstname(firstname);
        return this;
    }

    public void setFirstname(String firstname) {
        this.firstname = firstname;
    }

    public String getLastname() {
        return this.lastname;
    }

    public Vets lastname(String lastname) {
        this.setLastname(lastname);
        return this;
    }

    public void setLastname(String lastname) {
        this.lastname = lastname;
    }

    public Set<Specialties> getSpecialties() {
        return this.specialties;
    }

    public void setSpecialties(Set<Specialties> specialties) {
        if (this.specialties != null) {
            this.specialties.forEach(i -> i.removeVet(this));
        }
        if (specialties != null) {
            specialties.forEach(i -> i.addVet(this));
        }
        this.specialties = specialties;
    }

    public Vets specialties(Set<Specialties> specialties) {
        this.setSpecialties(specialties);
        return this;
    }

    public Vets addSpecialty(Specialties specialties) {
        this.specialties.add(specialties);
        specialties.getVets().add(this);
        return this;
    }

    public Vets removeSpecialty(Specialties specialties) {
        this.specialties.remove(specialties);
        specialties.getVets().remove(this);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Vets)) {
            return false;
        }
        return id != null && id.equals(((Vets) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Vets{" +
            "id=" + getId() +
            ", firstname='" + getFirstname() + "'" +
            ", lastname='" + getLastname() + "'" +
            "}";
    }
}
