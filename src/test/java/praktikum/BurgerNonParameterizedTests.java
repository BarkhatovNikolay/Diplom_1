package praktikum;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

public class BurgerNonParameterizedTests {
    @Mock
    private Bun mockBun;
    @Mock
    private Ingredient firstMockIngredient;
    @Mock
    private Ingredient secondMockIngredient;

    private Burger burger;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        burger = new Burger();

        //моки с фиксированными значениями
        when(mockBun.getName()).thenReturn("test bun");
        when(mockBun.getPrice()).thenReturn(100f);

        when(firstMockIngredient.getType()).thenReturn(IngredientType.SAUCE);
        when(firstMockIngredient.getName()).thenReturn("hot sauce");
        when(firstMockIngredient.getPrice()).thenReturn(50f);

        when(secondMockIngredient.getType()).thenReturn(IngredientType.FILLING);
        when(secondMockIngredient.getName()).thenReturn("cheese");
        when(secondMockIngredient.getPrice()).thenReturn(80f);
    }

    @Test
    public void testSetBuns() {
        burger.setBuns(mockBun);
        assertSame("Булка должна быть установлена", mockBun, burger.bun);
    }

    @Test
    public void testAddIngredientIncreasesListSize() {
        int initialSize = burger.ingredients.size();
        burger.addIngredient(firstMockIngredient);
        assertEquals("Список ингредиентов должен увеличиться на 1",
                initialSize + 1, burger.ingredients.size());
    }

    @Test
    public void testAddIngredientContainsAddedIngredient() {
        burger.addIngredient(firstMockIngredient);
        assertTrue("Список ингредиентов должен содержать добавленный ингредиент",
                burger.ingredients.contains(firstMockIngredient));
    }

    @Test
    public void testRemoveIngredient() {
        burger.addIngredient(firstMockIngredient);
        burger.removeIngredient(0);
        assertTrue("После удаления список должен быть пустым", burger.ingredients.isEmpty());
    }

    @Test
    public void testRemoveIngredientRemovesCorrectIngredient() {
        burger.addIngredient(firstMockIngredient);
        burger.removeIngredient(0);
        assertFalse("Список ингредиентов не должен содержать удаленный ингредиент",
                burger.ingredients.contains(firstMockIngredient));
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testRemoveIngredientWithNegativeIndex() {
        burger.removeIngredient(-1);
    }

    @Test
    public void testMoveIngredient() {
        burger.addIngredient(firstMockIngredient);
        burger.addIngredient(secondMockIngredient);
        burger.moveIngredient(0, 1);

        List<Object> expected = Arrays.asList(secondMockIngredient, firstMockIngredient);
        assertEquals("После перемещения порядок ингредиентов должен измениться",
                expected, burger.ingredients);
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testMoveIngredientWithInvalidIndex() {
        burger.moveIngredient(0, 0); // Пустой список
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testMoveIngredientWithNegativeIndex() {
        burger.addIngredient(firstMockIngredient);
        burger.moveIngredient(-1, 0);
    }

    @Test(expected = NullPointerException.class)
    public void testGetPriceWithoutBun() {
        burger.getPrice();
    }

    @Test(expected = NullPointerException.class)
    public void testGetReceiptWithoutBun() {
        burger.getReceipt();
    }
}
