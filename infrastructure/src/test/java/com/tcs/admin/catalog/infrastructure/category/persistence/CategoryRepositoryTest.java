package com.tcs.admin.catalog.infrastructure.category.persistence;

import com.tcs.admin.catalog.domain.category.Category;
import com.tcs.admin.catalog.MySQLGatewayTest;
import org.hibernate.PropertyValueException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;

@MySQLGatewayTest
public class CategoryRepositoryTest {

    @Autowired
    private CategoryRepository categoryRepository;

    @Test
    public void givenInvalidNullName_whenCallSave_thenReturnError() {
        final var expectedPropertyName = "name";
        final var expectedMessage = "not-null property references a null or transient value";

        final var aCategory = Category.newCategory("Movies", "Most watched", true);
        final var anEntity = CategoryJpaEntity.from(aCategory);
        anEntity.setName(null);

        final var actualException = Assertions.assertThrows(
                DataIntegrityViolationException.class,
                () -> categoryRepository.save(anEntity)
        );

        final var actualCause
                = Assertions.assertInstanceOf(PropertyValueException.class, actualException.getCause());

        Assertions.assertEquals(expectedPropertyName, actualCause.getPropertyName());
        Assertions.assertTrue(actualCause.getMessage().contains(expectedMessage));
    }

    @Test
    public void givenInvalidNullCreatedAt_whenCallSave_thenReturnError() {
        final var expectedPropertyName = "createdAt";
        final var expectedMessage = "not-null property references a null or transient value";

        final var aCategory = Category.newCategory("Movies", "Most watched", true);
        final var anEntity = CategoryJpaEntity.from(aCategory);
        anEntity.setCreatedAt(null);

        final var actualException = Assertions.assertThrows(
                DataIntegrityViolationException.class,
                () -> categoryRepository.save(anEntity)
        );

        final var actualCause
                = Assertions.assertInstanceOf(PropertyValueException.class, actualException.getCause());

        Assertions.assertEquals(expectedPropertyName, actualCause.getPropertyName());
        Assertions.assertTrue(actualCause.getMessage().contains(expectedMessage));
    }

    @Test
    public void givenInvalidNullUpdatedAt_whenCallSave_thenReturnError() {
        final var expectedPropertyName = "updatedAt";
        final var expectedMessage = "not-null property references a null or transient value";

        final var aCategory = Category.newCategory("Movies", "Most watched", true);
        final var anEntity = CategoryJpaEntity.from(aCategory);
        anEntity.setUpdatedAt(null);

        final var actualException = Assertions.assertThrows(
                DataIntegrityViolationException.class,
                () -> categoryRepository.save(anEntity)
        );

        final var actualCause
                = Assertions.assertInstanceOf(PropertyValueException.class, actualException.getCause());

        Assertions.assertEquals(expectedPropertyName, actualCause.getPropertyName());
        Assertions.assertTrue(actualCause.getMessage().contains(expectedMessage));
    }
}
