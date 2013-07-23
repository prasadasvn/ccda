package unit.com.ids.ccda.mixins

import com.ids.ccda.mixins.RequireMapFields
import org.junit.Test
import static org.junit.Assert.*
import static org.junit.Assert.assertTrue
@Mixin(RequireMapFields)
class RequiredMapFieldsTests {

    @Test
    void testNullMap(){
      def map
      def required = ["a"]
      def results = requireForMap(map, required)
      assertEquals(results, ["a":""] )
    }

    @Test
    void testNullRequiredFields(){
        def map = ["a": 1, "b":2]
        def required = []
        def results = requireForMap(map, required)
        assertEquals(results, map )
    }

    @Test
    void testRequireFields(){
        def map = ["a": 1, "b":2]
        def required = ["a", "b", "c", "d"]
        def results = requireForMap(map, required)
        assertEquals(results, ["a": 1, "b":2, "c":"", "d":""] )
    }

    @Test
    void testNullArrayOfMaps(){
        def ary = new ArrayList<LinkedHashMap>()
        def required = ["a"]
        def results = requireForMaps(ary, required)
        assertEquals(results, [] )
    }

    @Test
    void testNullRequiredFieldsWithArrayOfMaps(){
        def map = [["a": 1, "b":2], ["c":3]]
        def required = []
        def results = requireForMaps(map, required)
        assertEquals(results, map )
    }

    @Test
    void testRequireFieldsWithArrayOfMaps(){
        def map = [["a": 1, "b":2], ["c":3]]
        def required = ["a", "b", "c", "d"]
        def results = requireForMaps(map, required)
        assertEquals(results, [["a": 1, "b":2, "c":"", "d":""],["c":3, "a": "", "b":"",  "d":""] ] )
    }

}
