package io.github.dan7arievlis.marketplaceapi.model;

import io.github.dan7arievlis.marketplaceapi.model.enums.UserRoles;
import jakarta.persistence.*;
import jakarta.transaction.Transactional;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.Period;
import java.util.ArrayList;
import java.util.List;

@Entity
@EqualsAndHashCode(callSuper = true, onlyExplicitlyIncluded = true)
@ToString(exclude = {"wishlist", "cart"})
@Data
@Table(name="users")
public class User extends Base {
    @Column(name = "username", length = 30, unique = true, nullable = false)
    private String username;

    @Column(name = "email", length = 80, unique = true, nullable = false)
    private String email;

    @Column(name = "password", length = 400, nullable = false)
    private String password;

    @Column(name = "first_name", length = 30, nullable = false)
    private String firstName;

    @Column(name = "last_name", length = 50, nullable = false)
    private String lastName;

    @Column(name = "birth_date", nullable = false)
    private LocalDate birthDate;

    @Column(name = "balance")
    private BigDecimal balance = BigDecimal.ZERO;

    @Column(name = "address", nullable = false)
    private String address;

    @Column(name = "phone")
    private String phone;

    @Column(name = "role", nullable = false)
    private UserRoles role = UserRoles.USER;

    @OneToOne(mappedBy = "owner", cascade = CascadeType.ALL, orphanRemoval = true)
    private Wishlist wishlist;

    @OneToMany(mappedBy = "owner", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Stock> stocks = new ArrayList<>();

    @OneToOne(mappedBy = "owner", cascade = CascadeType.ALL, orphanRemoval = true)
    private Cart cart;

    public String getFullName() {
        return firstName + " " + lastName;
    }

    public int getAge() {
        return Period.between(birthDate, LocalDate.now()).getYears();
    }

    @Transactional
    public void refund(BigDecimal amount) {
        setBalance(getBalance().add(amount));
    }
}
