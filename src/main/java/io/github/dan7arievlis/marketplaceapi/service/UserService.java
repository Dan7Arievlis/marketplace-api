package io.github.dan7arievlis.marketplaceapi.service;

import io.github.dan7arievlis.marketplaceapi.controller.dto.user.ChangePasswordRequestDTO;
import io.github.dan7arievlis.marketplaceapi.exceptions.InvalidFieldException;
import io.github.dan7arievlis.marketplaceapi.model.Cart;
import io.github.dan7arievlis.marketplaceapi.model.User;
import io.github.dan7arievlis.marketplaceapi.model.Wishlist;
import io.github.dan7arievlis.marketplaceapi.model.enums.UserRoles;
import io.github.dan7arievlis.marketplaceapi.repository.UserRepository;
import io.github.dan7arievlis.marketplaceapi.repository.specs.UserSpecs;
import io.github.dan7arievlis.marketplaceapi.security.SecurityService;
import io.github.dan7arievlis.marketplaceapi.service.component.OptionalSpecificationSearch;
import io.github.dan7arievlis.marketplaceapi.validator.UserValidator;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.ReflectionUtils;

import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Stream;

@RequiredArgsConstructor
@Service
public class UserService implements OptionalSpecificationSearch {
    public final UserRepository repository;
    public final UserValidator validator;
    public final SecurityService securityService;
    public final PasswordEncoder encoder;
    public final WishlistService wishlistService;

    public Optional<User> findById(UUID id) {
        return repository.findById(id);
    }

    public Optional<User> findByUsername(String username) {
        return repository.findByUsernameLikeIgnoreCase(username);
    }

    public Optional<User> findByEmail(String email) {
        return repository.findByEmail(email);
    }

    @Transactional
    public void create(User user) {
        restore(user);
        user.setPassword(encoder.encode(user.getPassword()));
        user.setBalance(user.getBalance() == null ? BigDecimal.ZERO : user.getBalance());

        if (user.getWishlist() == null) {
            var wishlist = new Wishlist();
            wishlist.setOwner(user);
            user.setWishlist(wishlist);
        }

        if (user.getCart() == null) {
            var cart = new Cart();
            cart.setOwner(user);
            user.setCart(cart);
        }

        save(user);
        wishlistService.create(user.getWishlist());
    }

    @Transactional
    public void save(User user) {
        validator.validate(user);
        user.setUser(securityService.getLoggedUser());
        repository.save(user);
    }

    public Page<User> search(String username, String email, String name, String address, UserRoles role, Integer page, Integer pageSize) {
        Specification<User> specs = Stream.of(
                optSpec(username, UserSpecs::usernameLike),
                optSpec(email, UserSpecs::emailEquals),
                optSpec(name, UserSpecs::nameLike),
                optSpec(address, UserSpecs::addressLike),
                optSpec(role, UserSpecs::roleEquals)
            ).reduce(Specification.allOf((root, query, cb) ->
                cb.isTrue(root.get("active"))), Specification::and);

        Pageable pageRequest = PageRequest.of(page, pageSize);

        return repository.findAll(specs, pageRequest);
    }

    @Transactional
    public void delete(User user) {
        user.delete();
        wishlistService.delete(user.getWishlist());
        save(user);
    }

    @Transactional
    public void restore(User user) {
        user.restore();
        if (user.getWishlist() != null)
            wishlistService.restore(user.getWishlist());
        save(user);
    }

    @Transactional
    public void fullUpdate(User aux, User user) {
        user.setUsername(aux.getUsername());
        user.setEmail(aux.getEmail());
        user.setFirstName(aux.getFirstName());
        user.setLastName(aux.getLastName());
        user.setAddress(aux.getAddress());
        user.setBirthDate(aux.getBirthDate());
        user.setBalance(aux.getBalance());
        user.setPhone(aux.getPhone());
        user.setRole(aux.getRole());

        update(user);
    }

    @Transactional
    public void update(User user) {
        if (user.getId() == null)
            throw new IllegalArgumentException("Is necessary to have a saved user in db to update it");

        save(user);
    }

    @Transactional
    public void updateFields(User user, Map<String, Object> fields) {
        var blackList = Set.of("id", "createdAt", "updatedAt", "deletedAt", "user", "active", "password");
        fields.forEach((key, value) -> {
            if (blackList.contains(key)) return;
            Field field = ReflectionUtils.findField(User.class, key);
            assert field != null;
            try {
                field.setAccessible(true);
                parseSpecialTypes(field, user, value);
            } catch (IllegalAccessException e) {
                throw new RuntimeException(e);
            }
        });
        save(user);
    }

    private void parseSpecialTypes(Field field, User user, Object value) throws IllegalAccessException {
        if (value == null) {
            field.set(user, null);
            return;
        }

        switch (field.getType().getSimpleName()) {
            case "BigDecimal":
                field.set(user, new BigDecimal(value.toString()));
                break;
            case "LocalDate":
                field.set(user, LocalDate.parse(value.toString()));
                break;
            default:
                field.set(user, value);
        }
    }

    @Transactional
    public void changePassword(User user, ChangePasswordRequestDTO request) {
        if (!encoder.matches(request.currentPassword(), user.getPassword()))
            throw new InvalidFieldException("currentPassword", "Current password don't match");

        if (encoder.matches(request.newPassword(), user.getPassword()))
            throw new InvalidFieldException("newPassword", "New password is equal to current password");

        matchConfirmPassword(request.newPassword(), request.confirmPassword());

        user.setPassword(encoder.encode(request.newPassword()));
        save(user);
    }

    public void matchConfirmPassword(String password, String confirmPassword) {
        if (!password.equals(confirmPassword))
            throw new InvalidFieldException("confirmPassword", "Confirm password don't match");
    }
}
