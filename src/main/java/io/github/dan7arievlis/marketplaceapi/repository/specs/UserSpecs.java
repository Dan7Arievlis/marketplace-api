package io.github.dan7arievlis.marketplaceapi.repository.specs;

import io.github.dan7arievlis.marketplaceapi.model.User;
import io.github.dan7arievlis.marketplaceapi.model.enums.UserRoles;
import jakarta.persistence.criteria.Expression;
import org.springframework.data.jpa.domain.Specification;

public class UserSpecs {
    public static Specification<User> usernameLike(String username) {
        return (root, query, cb) -> cb.like(cb.upper(root.get("username")), "%" + username.toUpperCase() + "%");
    }

    public static Specification<User> emailEquals(String email) {
        return (root, query, cb) -> cb.equal(root.get("email"), email);
    }

    public static Specification<User> nameLike(String name) {
        return (root, query, cb) -> {
            String term = "%" + name.trim().replaceAll("\\s+", " ").toUpperCase() + "%";

            Expression<String> first = cb.upper(cb.<String>coalesce(root.get("firstName"), ""));
            Expression<String> last = cb.upper(cb.<String>coalesce(root.get("lastName"), ""));

            Expression<String> full = cb.concat(cb.concat(first, " "), last);

            return cb.like(full, term);
        };
    }

    public static Specification<User> addressLike(String address) {
        return(root, query, cb) -> cb.like(cb.upper(root.get("address")), "%" + address.toUpperCase() + "%");
    }

    public static Specification<User> roleEquals(UserRoles role) {
        return (root, query, cb) -> cb.equal(root.get("role"), role);
    }
}
