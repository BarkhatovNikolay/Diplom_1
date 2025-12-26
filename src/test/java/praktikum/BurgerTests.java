package praktikum;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

@RunWith(Parameterized.class)
public class BurgerTests {

    @Mock
    private Bun mockBun;
    @Mock
    private Ingredient mockIngredient1;
    @Mock
    private Ingredient mockIngredient2;
    private Burger burger;

    private final String bunName;
    private final float bunPrice;
    private final IngredientType ingredientType;
    private final String ingredientName;
    private final float ingredientPrice;

    public BurgerTests(String bunName, float bunPrice, IngredientType ingredientType,
                       String ingredientName, float ingredientPrice) {
        this.bunName = bunName;
        this.bunPrice = bunPrice;
        this.ingredientType = ingredientType;
        this.ingredientName = ingredientName;
        this.ingredientPrice = ingredientPrice;
    }

    @Parameterized.Parameters(name = "Данные: булка {0} цена {1}, ингредиент {3} тип {2} цена {4}")
    public static Object[][] getTestData() {
        return new Object[][] {
                {"black bun", 100f, IngredientType.SAUCE, "hot sauce", 50f},
                {"white bun", 200f, IngredientType.FILLING, "cutlet", 150f}
        };
    }

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        burger = new Burger();

        // моки для булки
        when(mockBun.getName()).thenReturn(bunName);
        when(mockBun.getPrice()).thenReturn(bunPrice);

        // моки ингредиенты
        when(mockIngredient1.getType()).thenReturn(ingredientType);
        when(mockIngredient1.getName()).thenReturn(ingredientName);
        when(mockIngredient1.getPrice()).thenReturn(ingredientPrice);
        when(mockIngredient2.getType()).thenReturn(IngredientType.FILLING);
        when(mockIngredient2.getName()).thenReturn("cheese");
        when(mockIngredient2.getPrice()).thenReturn(80f);
    }


    @Test
    public void testSetBuns() {
        burger.setBuns(mockBun);
        assertSame("Булка должна быть установлена", mockBun, burger.bun);
    }

    @Test
    public void testAddIngredient() {
        burger.addIngredient(mockIngredient1);
        assertEquals("Список ингредиентов должен содержать 1 элемент", 1, burger.ingredients.size());
        assertTrue("Список ингредиентов должен содержать добавленный ингредиент",
                burger.ingredients.contains(mockIngredient1));
    }

    @Test
    public void testRemoveIngredient() {
        burger.addIngredient(mockIngredient1);
        burger.removeIngredient(0);
        assertTrue("После удаления список должен быть пустым", burger.ingredients.isEmpty());
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testRemoveIngredientWithNegativeIndex() {
        burger.removeIngredient(-1);
    }

    @Test
    public void testMoveIngredient() {
        burger.addIngredient(mockIngredient1);
        burger.addIngredient(mockIngredient2);
        burger.moveIngredient(0, 1);
        assertEquals("После перемещения первый элемент должен быть mockIngredient2",
                mockIngredient2, burger.ingredients.get(0));
        assertEquals("После перемещения второй элемент должен быть mockIngredient1",
                mockIngredient1, burger.ingredients.get(1));
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testMoveIngredientWithInvalidIndex() {
        burger.moveIngredient(0, 0); // Пустой список
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testMoveIngredientWithNegativeIndex() {
        burger.addIngredient(mockIngredient1);
        burger.moveIngredient(-1, 0);
    }

    @Test
    public void testGetPrice() {
        burger.setBuns(mockBun);
        burger.addIngredient(mockIngredient1);
        float expectedPrice = (bunPrice * 2) + ingredientPrice;
        float actualPrice = burger.getPrice();
        assertEquals("Цена рассчитана некорректно", expectedPrice, actualPrice, 0.001f);
    }

    @Test(expected = NullPointerException.class)
    public void testGetPriceWithoutBun() {
        burger.getPrice();
    }

    @Test
    public void testGetReceipt() {
        burger.setBuns(mockBun);
        burger.addIngredient(mockIngredient1);
        String receipt = burger.getReceipt();

        assertNotNull("Чек не должен быть null", receipt);
        assertTrue("Чек должен содержать название булки", receipt.contains(bunName));
        assertTrue("Чек должен содержать название ингредиента", receipt.contains(ingredientName));
        assertTrue("Чек должен содержать тип ингредиента в нижнем регистре",
                receipt.contains(ingredientType.toString().toLowerCase()));
        assertTrue("Чек должен содержать слово 'Price'", receipt.contains("Price:"));
    }

    @Test(expected = NullPointerException.class)
    public void testGetReceiptWithoutBun() {
        burger.getReceipt();
    }
}