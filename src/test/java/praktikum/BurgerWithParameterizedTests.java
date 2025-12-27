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
public class BurgerWithParameterizedTests {

    @Mock
    private Bun mockBun;
    @Mock
    private Ingredient firstMockIngredient;
    @Mock
    private Ingredient secondMockIngredient;
    private Burger burger;

    private final String bunName;
    private final float bunPrice;
    private final IngredientType ingredientType;
    private final String ingredientName;
    private final float ingredientPrice;

    public BurgerWithParameterizedTests(String bunName, float bunPrice, IngredientType ingredientType,
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
        when(firstMockIngredient.getType()).thenReturn(ingredientType);
        when(firstMockIngredient.getName()).thenReturn(ingredientName);
        when(firstMockIngredient.getPrice()).thenReturn(ingredientPrice);

        when(secondMockIngredient.getType()).thenReturn(IngredientType.FILLING);
        when(secondMockIngredient.getName()).thenReturn("cheese");
        when(secondMockIngredient.getPrice()).thenReturn(80f);
    }


    @Test
    public void testGetPrice() {
        burger.setBuns(mockBun);
        burger.addIngredient(firstMockIngredient);
        float expectedPrice = (bunPrice * 2) + ingredientPrice;
        float actualPrice = burger.getPrice();
        assertEquals("Цена рассчитана некорректно", expectedPrice, actualPrice, 0.001f);
    }

    @Test
    public void testGetReceipt() {
        burger.setBuns(mockBun);
        burger.addIngredient(firstMockIngredient);
        String receipt = burger.getReceipt();

        String expectedReceipt = String.format("(==== %s ====)%n", bunName) +
                String.format("= %s %s =%n", ingredientType.toString().toLowerCase(), ingredientName) +
                String.format("(==== %s ====)%n", bunName) +
                String.format("%nPrice: %f%n", (bunPrice * 2) + ingredientPrice);

        assertEquals("Рецепт должен соответствовать формату", expectedReceipt, receipt);
    }
}