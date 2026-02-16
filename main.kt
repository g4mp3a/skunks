
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.Assertions.assertEquals

data class Load(
  val width: Double,
  val length: Double,
  val height: Double,
  val weight: Double
) {
  val volume: Double = width * length * height
}

class Dispatcher {
    private val BULKY_VOL = 1_000_000.0
    private val DIM_LIMIT = 150.0
    private val WEIGHT_LIMIT = 20.0

    // Predicates as reusable properties
    private val isBulky = { l: Load -> 
        l.volume >= BULKY_VOL || l.width >= DIM_LIMIT || l.length >= DIM_LIMIT || l.height >= DIM_LIMIT 
    }

    private val isHeavy = { l: Load -> l.weight >= WEIGHT_LIMIT }

    fun process(load: Load): String {
        val bulky = isBulky(load)
        val heavy = isHeavy(load)

        return when {
            bulky && heavy -> "REJECTED"
            bulky || heavy -> "SPECIAL"
            else           -> "STANDARD"
        }
    }
}

class DispatcherTest {
    private val dispatcher = Dispatcher()

    @Test
    fun `should categorize small light load as STANDARD`() {
        val load = Load(10.0, 10.0, 10.0, 5.0)
        assertEquals("STANDARD", dispatcher.process(load))
    }

    @Test
    fun `should categorize high volume load as SPECIAL`() {
        val load = Load(100.0, 100.0, 100.0, 5.0) // Vol = 1,000,000
        assertEquals("SPECIAL", dispatcher.process(load))
    }

    @Test
    fun `should categorize long dimension load as SPECIAL`() {
        val load = Load(151.0, 10.0, 10.0, 5.0)
        assertEquals("SPECIAL", dispatcher.process(load))
    }

    @Test
    fun `should categorize heavy load as SPECIAL`() {
        val load = Load(10.0, 10.0, 10.0, 25.0)
        assertEquals("SPECIAL", dispatcher.process(load))
    }

    @Test
    fun `should REJECT load that is both bulky and heavy`() {
        val load = Load(150.0, 10.0, 10.0, 20.0)
        assertEquals("REJECTED", dispatcher.process(load))
    }

    @Test
    fun `should handle boundary values correctly`() {
        // Just under the weight limit
        val load = Load(10.0, 10.0, 10.0, 19.99)
        assertEquals("STANDARD", dispatcher.process(load))
    }
}
